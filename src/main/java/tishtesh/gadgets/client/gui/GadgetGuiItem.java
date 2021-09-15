package tishtesh.gadgets.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tishtesh.gadgets.core.Config;

public abstract class GadgetGuiItem {
    protected abstract int getHeight();
    protected int getWidth() {
        return Config.CLIENT.gadgetGuiWidth.get();
    }

    protected abstract void render(MatrixStack matrixStack, Minecraft minecraft, GadgetOverlayGui gui, int x, int y);
}
