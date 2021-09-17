package tishtesh.gadgets.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;
import tishtesh.gadgets.core.Config;

import java.util.Arrays;

public class BiometerGuiItem extends GadgetGuiItem {

    @Override
    protected int getHeight() {
        return 20;
    }

    @Override
    protected void render(MatrixStack matrixStack, Minecraft minecraft, GadgetOverlayGui gui, int x, int y) {
        // Get current biome
        BlockPos blockpos = new BlockPos(minecraft.player.blockPosition());
        Biome biome = minecraft.level.getBiome(blockpos);
        ResourceLocation biomeNameResourceLocation = minecraft.level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome);
        // Get biome display name
        String biomeName = WordUtils.capitalizeFully(biomeNameResourceLocation.toString().split(":")[1].replace('_', ' '));

        int width = getWidth();

        // Draw biome icon
        if (Config.CLIENT.gadgetGuiIcons.get()) {
            gui.drawTexture(matrixStack, gui.getBiomeIconResourceLocation(biomeNameResourceLocation), x + 1, y + 1,  18, 18);
            width += 18;
        }

        // Draw biome name

        // If biome name too long (if the name won't fit in the space - the icon, or just the width if icons are disabled)
        if (minecraft.font.width(biomeName) > (Config.CLIENT.gadgetGuiIcons.get() ? getWidth() - 20 : getWidth() - 2)) {
            // Split onto 2 lines
            String[] biomeNameWords = biomeName.split(" ");
            int splitPoint = biomeNameWords.length / 2;
            String firstPart = String.join(" ", Arrays.copyOfRange(biomeNameWords, 0, splitPoint));
            String secondPart = String.join(" ", Arrays.copyOfRange(biomeNameWords, splitPoint, biomeNameWords.length));

            // Render small
            RenderSystem.pushMatrix();
            GL11.glScalef(GadgetOverlayGui.SmallTextSF, GadgetOverlayGui.SmallTextSF, GadgetOverlayGui.SmallTextSF);
            minecraft.font.drawShadow(matrixStack, firstPart, (x + 2 + width / 2F - minecraft.font.width(firstPart) / 2F) / GadgetOverlayGui.SmallTextSF,
                    (y + 1 + (getHeight() * 0.3F - minecraft.font.lineHeight / 2F)) / GadgetOverlayGui.SmallTextSF, GadgetOverlayGui.TextColour);
            minecraft.font.drawShadow(matrixStack, secondPart, (x + 2 + width / 2F - minecraft.font.width(secondPart) / 2F) / GadgetOverlayGui.SmallTextSF,
                    (y + 1 + (getHeight() * 0.7F - minecraft.font.lineHeight / 2F)) / GadgetOverlayGui.SmallTextSF, GadgetOverlayGui.TextColour);
            RenderSystem.popMatrix();
        }
        else {
            // Render normally
            RenderSystem.pushMatrix();
            minecraft.font.drawShadow(matrixStack, biomeName, x + 1 + width / 2F - minecraft.font.width(biomeName) / 2F,
                    y + 1 + (getHeight() / 2F - minecraft.font.lineHeight / 2F), GadgetOverlayGui.TextColour);
            RenderSystem.popMatrix();
        }
    }
}
