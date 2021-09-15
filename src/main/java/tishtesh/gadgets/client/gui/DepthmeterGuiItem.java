package tishtesh.gadgets.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import tishtesh.gadgets.core.Config;

public class DepthmeterGuiItem extends GadgetGuiItem{
    @Override
    protected int getHeight() {
        return 20;
    }

    @Override
    protected void render(MatrixStack matrixStack, Minecraft minecraft, GadgetOverlayGui gui, int x, int y) {
        int width = getWidth();
        if (Config.CLIENT.gadgetGuiIcons.get()) {
            gui.drawTexture(matrixStack, Icons.DepthmeterGuiIcon, x + 1, y + 1, 18, 18);
            width += 18;
        }

        // Get coordinate string
        String depth = String.format("%.0f", minecraft.player.position().y);

        // Render string
        minecraft.font.drawShadow(matrixStack, depth, x + 1 + width / 2F - (minecraft.font.width(depth)) / 2F,
                y + 1 + (getHeight() / 2F - minecraft.font.lineHeight / 2F), GadgetOverlayGui.TextColour);
    }
}
