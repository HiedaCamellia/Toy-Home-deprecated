package cn.solarmoon.toy_home.core.network;

import cn.solarmoon.solarmoon_core.api.network.IServerPackHandler;
import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import cn.solarmoon.toy_home.api.util.namespace.NETList;
import cn.solarmoon.toy_home.core.ToyHome;
import cn.solarmoon.toy_home.core.common.item.SlipItem;
import cn.solarmoon.toy_home.core.common.item.ToyGunItem;
import cn.solarmoon.toy_home.core.common.registry.THItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ServerPackHandler implements IServerPackHandler {
    @Override
    public void handle(ServerPlayer player, ServerLevel level, BlockPos pos, ItemStack stack, CompoundTag tag, FluidStack fluidStack, float v, int[] ints, String s, List<ItemStack> list, List<Vec3> list1, String message) {
        switch (message) {
            case NETList.FORTUNE -> {
                ItemStack heldItem = player.getMainHandItem();
                if (heldItem.is(THItems.SLIP.get())) {
                    CompoundTag nbt = heldItem.getOrCreateTag();
                    nbt.putString(SlipItem.CONTENT, s);
                }
            }
        }
    }
}
