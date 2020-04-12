package tishtesh.gadgets.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.lwjgl.opengl.GL11;
import tishtesh.gadgets.core.Config;

import java.util.Arrays;

public class BiometerGuiItem extends GadgetGuiItem {

    @Override
    protected int getHeight() {
        return 20;
    }

    @Override
    protected void render(Minecraft mc, GadgetOverlayGui gui, int x, int y) {
        // Get current biome
        BlockPos blockpos = new BlockPos(mc.getRenderViewEntity());
        Biome biome = mc.world.getBiome(blockpos);
        String biomeName = biome.getDisplayName().getString();

        // Draw biome icon
        if (Config.CLIENT.gadgetGuiIcons.get()) {
            gui.drawTexture(gui.getBiomeIconResourceLocation(biome), x + 1, y + 1,  18, 18); ;
        }

        int width = Config.CLIENT.gadgetGuiIcons.get() ? getWidth() + 18: getWidth();

        // Draw biome name

        // If biome name too long (if the name won't fit in the space - the icon, or just the width if icons are disabled)
        if (mc.fontRenderer.getStringWidth(biomeName) > (Config.CLIENT.gadgetGuiIcons.get() ? getWidth() - 20 : getWidth() - 2)) {
            // Split onto 2 lines
            String[] biomeNameWords = biomeName.split(" ");
            int splitPoint = biomeNameWords.length / 2;
            String firstPart = String.join(" ", Arrays.copyOfRange(biomeNameWords, 0, splitPoint));
            String secondPart = String.join(" ", Arrays.copyOfRange(biomeNameWords, splitPoint, biomeNameWords.length));

            // Render small
            RenderSystem.pushMatrix();
            GL11.glScalef(GadgetOverlayGui.SmallTextSF, GadgetOverlayGui.SmallTextSF, GadgetOverlayGui.SmallTextSF);
            mc.fontRenderer.drawStringWithShadow(firstPart, (x + 1 + width / 2F - (mc.fontRenderer.getStringWidth(firstPart)) / 2F) / GadgetOverlayGui.SmallTextSF,
                    (y + 1 + (getHeight() * 0.3F - mc.fontRenderer.FONT_HEIGHT / 2F)) / GadgetOverlayGui.SmallTextSF, GadgetOverlayGui.TextColour);
            mc.fontRenderer.drawStringWithShadow(secondPart, (x + 1 + width / 2F - (mc.fontRenderer.getStringWidth(secondPart)) / 2F) / GadgetOverlayGui.SmallTextSF,
                    (y + 1 + (getHeight() * 0.7F - mc.fontRenderer.FONT_HEIGHT / 2F)) / GadgetOverlayGui.SmallTextSF, GadgetOverlayGui.TextColour);
            RenderSystem.popMatrix();
        }
        else {
            // Render normally
            mc.fontRenderer.drawStringWithShadow(biomeName, x + 1 + width / 2F - mc.fontRenderer.getStringWidth(biomeName) / 2F,
                    y + 1 + (getHeight() / 2F - mc.fontRenderer.FONT_HEIGHT / 2F), GadgetOverlayGui.TextColour);
        }
    }
}
