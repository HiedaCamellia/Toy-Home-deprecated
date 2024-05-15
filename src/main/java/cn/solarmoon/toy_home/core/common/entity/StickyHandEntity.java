package cn.solarmoon.toy_home.core.common.entity;

import cn.solarmoon.solarmoon_core.api.util.VecUtil;
import cn.solarmoon.toy_home.core.common.registry.THEntities;
import cn.solarmoon.toy_home.core.common.registry.THItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class StickyHandEntity extends Projectile {

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(StickyHandEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Direction> ATTACHED_FACE = SynchedEntityData.defineId(StickyHandEntity.class, EntityDataSerializers.DIRECTION);
    private static final EntityDataAccessor<Boolean> WITHDRAWING = SynchedEntityData.defineId(StickyHandEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<BlockPos>> ATTACHED_POS = SynchedEntityData.defineId(StickyHandEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    private static final EntityDataAccessor<Optional<UUID>> ATTACHED_ENTITY = SynchedEntityData.defineId(StickyHandEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    public static final HashMap<Entity, StickyHandEntity> ENTITY_BOUND = new HashMap<>();

    private int ticksWithdrawing = 0;

    public StickyHandEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public StickyHandEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        super(THEntities.STICKY_HAND.get(), level);
    }

    public StickyHandEntity(LivingEntity entity, Level level) {
        super(THEntities.STICKY_HAND.get(), level);
        setOwner(entity);
        ENTITY_BOUND.put(entity, this);
        setPos(VecUtil.getSpawnPosFrontEntity(entity, 0.5));
    }

    public void tick() {
        Entity entity = this.getOwner();
        if (!this.level().isClientSide) {
            if(entity == null || !entity.isAlive()) {
                this.discard();
            } else if (entity.isShiftKeyDown()) {
                this.setWithdrawing(true);
            }
        }
        if (this.isWithdrawing()) {
            getBack(false);
        } else if (this.level().isClientSide || this.level().hasChunkAt(this.blockPosition())) {

            if (this.getStuckToPos() == null && this.getStuckEntity() == null) {
                findTarget();
            }

            if (getStuckEntity() == null && getStuckToPos() != null) {
                stuckToBlock();
                moveEntityByStuckPos();
            }

            if (getStuckEntity() != null && getStuckToPos() == null && entity != null) {
                if (!getStuckEntity().isRemoved() && getStuckEntity().level().dimension() == this.level().dimension()) {
                    AABB stuckEntityBox = getStuckEntity().getBoundingBox();
                    getBack(stuckEntityBox.getXsize() * stuckEntityBox.getYsize() * stuckEntityBox.getZsize() <= 3);
                } else {
                    this.setStuckEntity(null);
                }
            }

        } else {
            discard();
        }

    }

    private void getBack(boolean retractEntity) {
        if (getOwner() != null) {
            super.tick();
            this.setStuckToPos(null);
            ticksWithdrawing++;
            Vec3 withDrawTo = getOwner().getEyePosition().add(0, -0.2F, 0);
            if (withDrawTo.distanceTo(this.position()) > 1.2F && ticksWithdrawing < 200) {
                Vec3 move = new Vec3(withDrawTo.x - this.getX(), withDrawTo.y - this.getY(), withDrawTo.z - this.getZ());
                Vec3 vector3d = move.normalize().scale(1.2D);
                this.setDeltaMovement(vector3d.scale(0.99));
                if (retractEntity && getStuckEntity() != null) {
                    getStuckEntity().setDeltaMovement(vector3d.scale(0.99));
                }
                double d0 = this.getX() + vector3d.x;
                double d1 = this.getY() + vector3d.y;
                double d2 = this.getZ() + vector3d.z;
                float f = Mth.sqrt((float) (move.x * move.x + move.z * move.z));
                if (!this.level().isClientSide) {
                    this.setYRot(Mth.wrapDegrees((float) (-Mth.atan2(move.x, move.z) * (double) Mth.RAD_TO_DEG)) - 180);
                    this.setXRot((float) (Mth.atan2(move.y, f) * (double) Mth.RAD_TO_DEG));
                    this.yRotO = this.getYRot();
                    this.xRotO = this.getXRot();
                }
                this.setPos(d0, d1, d2);
            } else {
                this.discard();
                if (retractEntity && getStuckEntity() != null) {
                    getStuckEntity().setDeltaMovement(getDeltaMovement().scale(0.1));
                }
            }
        }
    }

    private void findTarget() {
        super.tick();
        Vec3 vector3d = this.getDeltaMovement();
        HitResult raytraceResult = ProjectileUtil.getHitResultOnMoveVector(this, newentity -> true);
        onHit(raytraceResult);
        this.checkInsideBlocks();
        double d0 = this.getX() + vector3d.x;
        double d1 = this.getY() + vector3d.y;
        double d2 = this.getZ() + vector3d.z;
        this.setDeltaMovement(vector3d.scale(0.99));
        if (this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir) && !this.isInWater()) {
            this.setDeltaMovement(Vec3.ZERO);

        } else {
            this.setPos(d0, d1, d2);
        }
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.1F, 0.0D));
        }
    }

    private void stuckToBlock() {
        BlockState state = this.level().getBlockState(this.getStuckToPos());
        Vec3 vec3 = new Vec3(this.getStuckToPos().getX() + 0.5F, this.getStuckToPos().getY() + 0.5F, this.getStuckToPos().getZ() + 0.5F);
        Vec3 offset = new Vec3(this.getAttachmentFacing().getStepX() * 0.55F, this.getAttachmentFacing().getStepY() * 0.55F, this.getAttachmentFacing().getStepZ() * 0.55F);
        this.setPos(vec3.add(offset));
        float targetX = this.getXRot();
        float targetY = this.getYRot();
        switch (this.getAttachmentFacing()) {
            case UP -> targetX = -90;
            case DOWN -> targetX = 90;
            case NORTH -> {
                targetX = 0;
                targetY = 0;
            }
            case EAST -> {
                targetX = 0;
                targetY = 90;
            }
            case SOUTH -> {
                targetX = 0;
                targetY = 180;
            }
            case WEST -> {
                targetX = 0;
                targetY = -90;
            }
        }
        this.setXRot(targetX);
        this.setYRot(targetY);
        if (state.isAir()) {
            this.setWithdrawing(true);
        }
    }

    private void moveEntityByStuckPos() {
        if (getOwner() != null && getOwner().distanceTo(this) > 2) {
            float entitySwing = 1.0F;
            if (getOwner() instanceof LivingEntity living) {
                float detract = living.xxa * living.xxa + living.yya * living.yya + living.zza * living.zza;
                entitySwing -= (float) Math.min(1.0F, Math.sqrt(detract) * 0.333F);
            }
            Vec3 move = new Vec3(this.getX() - getOwner().getX(), this.getY() - (double)getOwner().getEyeHeight() / 2.0D - getOwner().getY(), this.getZ() - getOwner().getZ());
            getOwner().setDeltaMovement(getOwner().getDeltaMovement().add(move.normalize().scale(0.2D * entitySwing)));
            if (!getOwner().onGround()) {
                getOwner().fallDistance = 0.0F;
            }
        }
    }

    private void stuckToEntity() {
        setPos(getStuckEntity().getX(), getStuckEntity().getY(0.8D), getStuckEntity().getZ());
        setXRot(getStuckEntity().getXRot());
        setYRot(getStuckEntity().getYRot());
    }

    public void setOwnerId(UUID id) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(id));
    }

    @Nullable
    public UUID getOwnerId() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }

    @Override
    public void setOwner(@Nullable Entity entity) {
        UUID id = entity == null ? null : entity.getUUID();
        setOwnerId(id);
    }

    @Nullable
    @Override
    public Entity getOwner() {
        UUID id = getOwnerId();
        if (id != null && !this.level().isClientSide) {
            return ((ServerLevel) level()).getEntity(id);
        }
        return getOwnerId() == null ? null : level().getPlayerByUUID(getOwnerId());
    }

    public void setAttachmentFacing(Direction direction){
        this.entityData.set(ATTACHED_FACE, direction);
    }

    public void setStuckToPos(BlockPos harvestedPos) {
        this.entityData.set(ATTACHED_POS, Optional.ofNullable(harvestedPos));
    }

    public void setWithdrawing(boolean withdrawing){
        this.entityData.set(WITHDRAWING, withdrawing);
    }

    public void setStuckEntity(Entity entity) {
        UUID id = entity == null ? null : entity.getUUID();
        this.entityData.set(ATTACHED_ENTITY, Optional.ofNullable(id));
    }

    public Direction getAttachmentFacing() {
        return this.entityData.get(ATTACHED_FACE);
    }

    public BlockPos getStuckToPos() {
        return this.entityData.get(ATTACHED_POS).orElse(null);
    }

    public UUID getStuckEntityId() {
        return this.entityData.get(ATTACHED_ENTITY).orElse(null);
    }

    public boolean isWithdrawing(){
        return this.entityData.get(WITHDRAWING);
    }

    public Entity getStuckEntity() {
        UUID id = getStuckEntityId();
        if (id != null && !this.level().isClientSide) {
            return ((ServerLevel) level()).getEntity(id);
        }
        return null;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (getStuckEntity() == null) {
            setStuckEntity(entityHitResult.getEntity());
        }
        super.onHitEntity(entityHitResult);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        if (getStuckToPos() == null) {
            setDeltaMovement(Vec3.ZERO);
            setStuckToPos(blockHitResult.getBlockPos());
            setAttachmentFacing(blockHitResult.getDirection());
        }
        super.onHitBlock(blockHitResult);
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return super.getBoundingBoxForCulling().move(0, -0.3, 0);
    }

    public ItemStack getItem() {
        return new ItemStack(THItems.STICKY_HAND.get());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (this.getOwner() != null) {
            compound.putUUID("OwnerUUID", getOwner().getUUID());
        }
        super.readAdditionalSaveData(compound);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("OwnerUUID")) {
            this.setOwnerId(compound.getUUID("OwnerUUID"));
        }
        super.addAdditionalSaveData(compound);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OWNER_UUID, Optional.empty());
        this.entityData.define(ATTACHED_FACE, Direction.DOWN);
        this.entityData.define(ATTACHED_POS, Optional.empty());
        this.entityData.define(WITHDRAWING, false);
        this.entityData.define(ATTACHED_ENTITY, Optional.empty());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
