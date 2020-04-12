package tishtesh.gadgets.client.gui;

import net.minecraft.client.Minecraft;
import tishtesh.gadgets.core.Config;

public class DepthmeterGuiItem extends GadgetGuiItem{
    @Override
    protected int getHeight() {
        return 20;
    }

    @Override
    protected void render(Minecraft mc, GadgetOverlayGui gui, int x, int y) {
        // Draw depthmeter icon
        if (Config.CLIENT.gadgetGuiIcons.get()) {
            gui.drawTexture(Icons.DepthmeterGuiIcon, x + 1, y + 1, 18, 18);
        }

        int width = Config.CLIENT.gadgetGuiIcons.get() ? getWidth() + 18: getWidth();

        // Get coordinate string
        String depth = String.format("%.0f", mc.getRenderViewEntity().getPosY());

        // Render string
        mc.fontRenderer.drawStringWithShadow(depth, x + 1 + width / 2F - (mc.fontRenderer.getStringWidth(depth)) / 2F,
                y + 1 + (getHeight() / 2F - mc.fontRenderer.FONT_HEIGHT / 2F), GadgetOverlayGui.TextColour);
    }
}
