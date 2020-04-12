package tishtesh.gadgets.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tishtesh.gadgets.core.Config;

@OnlyIn(Dist.CLIENT)
public abstract class GadgetGuiItem {
    protected abstract int getHeight();
    protected int getWidth() {
        return Config.CLIENT.gadgetGuiWidth.get();
    }

    protected abstract void render(Minecraft mc, GadgetOverlayGui gui, int x, int y);
}
