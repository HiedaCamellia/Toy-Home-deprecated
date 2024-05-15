package cn.solarmoon.toy_home.core.network;

import cn.solarmoon.solarmoon_core.api.network.IClientPackHandler;
import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import cn.solarmoon.toy_home.api.util.namespace.NETList;
import cn.solarmoon.toy_home.core.common.item.ToyGunItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ClientPackHandler implements IClientPackHandler {
    @Override
    public void handle(LocalPlayer player, ClientLevel level, BlockPos pos, ItemStack stack, CompoundTag tag, FluidStack fluidStack, float v, int[] ints, String s, List<ItemStack> list, List<Vec3> list1, String message) {
        switch (message) {

        }
    }
}
