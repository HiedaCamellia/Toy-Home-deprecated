package cn.solarmoon.toy_home.core.client.gui;

import cn.solarmoon.toy_home.api.util.namespace.NETList;
import cn.solarmoon.toy_home.core.ToyHome;
import cn.solarmoon.toy_home.core.common.registry.THItems;
import cn.solarmoon.toy_home.core.common.registry.THNetPacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class SlipEditScreen extends Screen {

    private EditBox editBox;
    private ItemStack slip;

    public SlipEditScreen(ItemStack slip) {
        super(Component.translatable(THItems.SLIP.get().getDescriptionId()));
        this.slip = slip;
    }

    @Override
    protected void init() {
        int editBoxWidth = 150;
        int editBoxHeight = 20;

        // 使box位于屏幕中心
        int editBoxX = width / 2 - editBoxWidth / 2;
        int editBoxY = height / 2 - editBoxHeight / 2;

        this.editBox = new EditBox(font, editBoxX, editBoxY, editBoxWidth, editBoxHeight, Component.literal("233"));
        editBox.setMaxLength(200);
        addWidget(editBox); // 添加组件
        setInitialFocus(editBox); // 设置开启焦点
    }


    @Override
    public boolean keyPressed(int key, int scanCode, int modifier) {
        if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
            setDone();
        }
        return super.keyPressed(key, scanCode, modifier);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        String s = editBox.getValue();
        this.init(minecraft, width, height);
        this.editBox.setValue(s);
    }

    @Override
    public void tick() {
        editBox.tick();
        super.tick();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics);
        editBox.render(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private boolean isValid() {
        return minecraft != null && minecraft.player != null;
    }

    private void setClose() {
        onClose();
    }

    private void onDone() {
        if (isValid()) {
            if (!editBox.getValue().isEmpty()) {
                THNetPacks.SERVER.getSender().send(NETList.FORTUNE, editBox.getValue());
            } else {
                minecraft.player.displayClientMessage(ToyHome.TRANSLATOR.set("message", "slip_empty"), true);
            }
        }
    }

    public void setDone() {
        this.minecraft.setScreen(null);
        onDone();
    }

}
