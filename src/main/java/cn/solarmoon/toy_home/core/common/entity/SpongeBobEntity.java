package cn.solarmoon.toy_home.core.common.entity;

import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import cn.solarmoon.solarmoon_core.api.util.VecUtil;
import cn.solarmoon.toy_home.core.common.registry.THDamageTypes;
import cn.solarmoon.toy_home.core.common.registry.THEntities;
import cn.solarmoon.toy_home.core.common.registry.THItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class SpongeBobEntity extends Projectile {

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(SpongeBobEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Optional<UUID>> ATTACHED_ENTITY = SynchedEntityData.defineId(SpongeBobEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private int stayTick;

    public SpongeBobEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public SpongeBobEntity(LivingEntity entity, Level level) {
        super(THEntities.SPONGE_BOB.get(), level);
        setOwner(entity);
        setPos(VecUtil.getSpawnPosFrontEntity(entity, 0.5));
    }

    public SpongeBobEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        super(THEntities.SPONGE_BOB.get(), level);
    }

    @Override
    public void tick() {
        if (this.level().hasChunkAt(this.blockPosition())) {
            if (getHitEntity() == null) {
                stayTick++;
                findTarget();
                if (stayTick > 200) discard();
            }
            if (getOwner() != null && getHitEntity() instanceof LivingEntity living) {
                if (getHitEntity().hurt(THDamageTypes.SPONGE_BOB_ATTACK.get(level(), this, getOwner()), 1)) {
                    LevelSummonUtil.summonDrop(living.getMainHandItem().copyWithCount(1), level(), living.getPosition(1));
                    living.getMainHandItem().shrink(1);
                    discard();
                }
            }
        } else discard();
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

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        setHitEntity(entityHitResult.getEntity());
        super.onHitEntity(entityHitResult);
    }

    private void findTarget() {
        super.tick();
        HitResult raytraceResult = ProjectileUtil.getHitResultOnMoveVector(this, newentity -> true);
        onHit(raytraceResult);
        Vec3 vector3d = this.getDeltaMovement();
        updateRotation();
        this.checkInsideBlocks();
        double d0 = this.getX() + vector3d.x;
        double d1 = this.getY() + vector3d.y;
        double d2 = this.getZ() + vector3d.z;
        this.setDeltaMovement(vector3d.scale(0.99));
        this.setPos(d0, d1, d2);
    }

    public void setHitEntity(@Nullable Entity entity) {
        UUID id = entity == null ? null : entity.getUUID();
        this.entityData.set(ATTACHED_ENTITY, Optional.ofNullable(id));
    }

    public Entity getHitEntity() {
        UUID id = this.entityData.get(ATTACHED_ENTITY).orElse(null);
        if (id != null && !this.level().isClientSide) {
            return ((ServerLevel) level()).getEntity(id);
        }
        return id == null ? null : level().getPlayerByUUID(id);
    }

    public ItemStack getItem() {
        return new ItemStack(THItems.SPONGE_BOB.get());
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
        this.entityData.define(ATTACHED_ENTITY, Optional.empty());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
