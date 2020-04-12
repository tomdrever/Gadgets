package tishtesh.gadgets.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tishtesh.gadgets.core.Config;

public class GuiRenderHandler {
    public final GadgetOverlayGui gui;

    public GuiRenderHandler() {
        gui = new GadgetOverlayGui(Minecraft.getInstance());
    }

    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event) {
        // If the gui is set to hide when the debug info is shown, only render when it isn't shown.
        // Otherwise, just render always
        if (Config.CLIENT.hideWhenDebugShown.get()) {
            if (!Minecraft.getInstance().gameSettings.showDebugInfo)
                gui.render();
        } else {
            gui.render();
        }
    }
}
