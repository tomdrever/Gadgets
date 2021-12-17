package tishtesh.gadgets.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import tishtesh.gadgets.core.Config;

public class DepthmeterGuiItem extends GadgetGuiItem{
    @Override
    protected int getHeight() {
        return 20;
    }

    @Override
    protected void render(PoseStack stack, Minecraft minecraft, GadgetOverlayGui gui, int x, int y) {
        int width = getWidth();
        if (Config.CLIENT.gadgetGuiIcons.get()) {
            gui.drawTexture(stack, Icons.DepthmeterGuiIcon, x + 1, y + 1, 18, 18);
            width += 18;
        }

        // Get coordinate string
        String depth = String.format("%.0f", minecraft.player.position().y);

        // Render string
        stack.pushPose();
        minecraft.font.drawShadow(stack, depth, x + 1 + width / 2F - (minecraft.font.width(depth)) / 2F,
                y + 1 + (getHeight() / 2F - minecraft.font.lineHeight / 2F), GadgetOverlayGui.TextColour);
        stack.popPose();
    }
}
