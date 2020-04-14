package tishtesh.gadgets.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import tishtesh.gadgets.core.Config;

@OnlyIn(Dist.CLIENT)
public class CompassGuiItem extends GadgetGuiItem {
    @Override
    protected int getHeight() {
        return 22;
    }

    @Override
    protected void render(Minecraft mc, GadgetOverlayGui gui, int x, int y) {
        int width = getWidth();
        if (Config.CLIENT.gadgetGuiIcons.get()) {
            gui.drawTexture(Icons.CompassGuiIcon, x + 1, y + (getHeight() / 2) - 9,  18, 18);
            width += 18;
        }

        // Get the name of the direction the player is facing
        String directionFacing = mc.getRenderViewEntity().getHorizontalFacing().getName();
        // Capitalise first letter
        directionFacing = directionFacing.substring(0, 1).toUpperCase() + directionFacing.substring(1);

        // Draw direction text
        mc.fontRenderer.drawStringWithShadow(directionFacing, x + 1 + width / 2F - mc.fontRenderer.getStringWidth(directionFacing) / 2F,
                y + 3, GadgetOverlayGui.TextColour);

        // Get coordinates
        String coordinates = String.format("%.0f %.0f", mc.getRenderViewEntity().getPosX(), mc.getRenderViewEntity().getPosZ());

        // Draw coordinates (scaled to be smaller)
        RenderSystem.pushMatrix();
        GL11.glScalef(GadgetOverlayGui.SmallTextSF, GadgetOverlayGui.SmallTextSF, GadgetOverlayGui.SmallTextSF);
        mc.fontRenderer.drawStringWithShadow(coordinates, (x + 2 + width / 2F - mc.fontRenderer.getStringWidth(coordinates) / 2F) / GadgetOverlayGui.SmallTextSF,
                (y + 1 + getHeight() * 0.55F) / GadgetOverlayGui.SmallTextSF, GadgetOverlayGui.TextColour);
        RenderSystem.popMatrix();
    }
}
