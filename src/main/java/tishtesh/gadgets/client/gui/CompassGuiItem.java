package tishtesh.gadgets.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import tishtesh.gadgets.core.Config;

public class CompassGuiItem extends GadgetGuiItem {
    @Override
    protected int getHeight() {
        return 22;
    }

    @Override
    protected void render(PoseStack stack, Minecraft minecraft, GadgetOverlayGui gui, int x, int y) {
        int width = getWidth();
        if (Config.CLIENT.gadgetGuiIcons.get()) {
            gui.drawTexture(stack, Icons.CompassGuiIcon, x + 1, y + (getHeight() / 2) - 9,  18, 18);
            width += 18;
        }

        // Get the name of the direction the player is facing
        String directionFacing = minecraft.player.getDirection().getName();
        // Capitalise first letter
        directionFacing = directionFacing.substring(0, 1).toUpperCase() + directionFacing.substring(1);

        // Draw direction text
        stack.pushPose();
        minecraft.font.drawShadow(stack, directionFacing, x + 1 + width / 2F - minecraft.font.width(directionFacing) / 2F,
                y + 3, GadgetOverlayGui.TextColour);
        stack.popPose();

        // Get coordinates
        String coordinates = String.format("%.0f %.0f", minecraft.player.position().x, minecraft.player.position().z);

        // Draw coordinates (scaled to be smaller)
        stack.pushPose();
        stack.scale(GadgetOverlayGui.SmallTextSF, GadgetOverlayGui.SmallTextSF, GadgetOverlayGui.SmallTextSF);
        minecraft.font.drawShadow(stack, coordinates, (x + 2 + width / 2F - minecraft.font.width(coordinates) / 2F) / GadgetOverlayGui.SmallTextSF,
                (y + 1 + getHeight() * 0.55F) / GadgetOverlayGui.SmallTextSF, GadgetOverlayGui.TextColour);
        stack.popPose();
    }
}
