package tishtesh.gadgets.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;
import tishtesh.gadgets.core.Config;

public class ClockGuiItem extends GadgetGuiItem{

    @Override
    protected int getHeight() {
        return 22;
    }

    @Override
    protected void render(PoseStack stack, Minecraft minecraft, GadgetOverlayGui gui, int x, int y) {
        int width = getWidth();
        if (Config.CLIENT.gadgetGuiIcons.get()) {
            ResourceLocation icon;
            switch (minecraft.player.level.dimension().location().toString()) {
                case "the_nether":
                    icon = Icons.NetherClockGuiIcon;
                    break;

                case "the_end":
                    icon = Icons.EndClockGuiIcon;
                    break;

                default:
                    icon = Icons.OverworldClockGuiIcon;
                    break;
            }
            gui.drawTexture(stack, icon, x + 1, y + (getHeight() / 2) - 9, 18, 18);
            width += 18;
        }

        // Get time details
        float dayTime = minecraft.level.getDayTime() / 24000F;
        float hours = ((dayTime - ((int) dayTime)) * 24) + 6;
        if (hours > 24)
            hours -= 24;
        int minutes = (int) ((hours - (int) hours ) * 60f);

        String timeString = Config.CLIENT.clockGadget24Hour.get() ? formatTime24Hour((int) hours, minutes) : formatTime12Hour((int) hours, minutes);

        // Get day
        long day = minecraft.level.getDayTime() / 24000L;
        String dayString = String.format("Day %d", day);

        // Render time string
        minecraft.font.drawShadow(stack, timeString, x + 1 + width / 2F - minecraft.font.width(timeString) / 2F,
                y + 3, GadgetOverlayGui.TextColour);

        // Render day (smaller)
        stack.pushPose();
        //GL11.glScalef(GadgetOverlayGui.SmallTextSF, GadgetOverlayGui.SmallTextSF, GadgetOverlayGui.SmallTextSF);
        minecraft.font.drawShadow(stack, dayString, (x + 2 + width / 2F - minecraft.font.width(dayString) / 2F) / GadgetOverlayGui.SmallTextSF,
                (y + 1 + getHeight() * 0.55F) / GadgetOverlayGui.SmallTextSF, GadgetOverlayGui.TextColour);
        stack.popPose();
    }

    private String formatTime24Hour(int hours, int minutes) {
        return String.format("%02d:%02d", hours, minutes);
    }

    private String formatTime12Hour(int hours, int minutes) {
        String period = "AM";
        if (hours >= 12)
            period = "PM";
        if (hours > 12)
            hours -= 12;

        return String.format("%d:%02d %s", hours, minutes, period);
    }
}
