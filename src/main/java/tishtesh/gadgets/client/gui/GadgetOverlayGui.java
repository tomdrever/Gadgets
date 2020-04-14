package tishtesh.gadgets.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import tishtesh.gadgets.core.Config;
import tishtesh.gadgets.core.Gadgets;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.inventory.CurioStackHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;

@OnlyIn(Dist.CLIENT)
public class GadgetOverlayGui extends AbstractGui {

    private final Minecraft mc;

    public static int BackgroundColour = 0x40000000;
    public final static int TextColour = 0xFFFFFFFF;

    public final static float SmallTextSF = 0.9f;

    private final HashMap<String, GadgetGuiItem> gadgetGuiItems;

    private final HashMap<ResourceLocation, ResourceLocation> biomeResourceMap;

    public GadgetOverlayGui(Minecraft mc) {
        this.mc = mc;

        biomeResourceMap = new HashMap<>();

        // Create a list of gadgets that have corresponding gui items
        gadgetGuiItems = new HashMap<>();
        gadgetGuiItems.put("compass", new CompassGuiItem());
        gadgetGuiItems.put("depthmeter", new DepthmeterGuiItem());
        gadgetGuiItems.put("biometer", new BiometerGuiItem());
        gadgetGuiItems.put("clock", new ClockGuiItem());
    }

    public void render() {
        Position position = getRenderStartPos();

        if (Config.CLIENT.needCurios.get()) {
            // Go through the gadgets the player has on and render the corresponding gui items.
            ArrayList<String> playerGadgets = getPlayerGadgets();

            // If displaying on the bottom of the screen, reverse the list so the order matches the order of gadgets
            // in the player's curio slots
            if (!displayItemsFromTop())
                Collections.reverse(playerGadgets);

            for (String gadget : playerGadgets) {
                renderGadgetGui(gadgetGuiItems.get(gadget), position);
            }
        }
        else {
            // Just go through all gadget gui items
            for (GadgetGuiItem gadgetGuiItem : gadgetGuiItems.values()) {
                renderGadgetGui(gadgetGuiItem, position);
            }
        }
    }

    /**
     * Should be called when resources are loaded
     * Creates a map of biomename to biomeicon, so the biometer gadget gui can quickly map the name it gets from the player
     * to an icon
     */
    public void generateBiomeResourceMap() {
        biomeResourceMap.clear();
        for (ResourceLocation biomeNameResourceLocation : ForgeRegistries.BIOMES.getKeys()) {
            ResourceLocation biomeIconResourceLocation = new ResourceLocation(Gadgets.MOD_ID,
                    String.format("textures/gui/biome_icons/%s/%s.png", biomeNameResourceLocation.toString().split(":")));

            if (mc.getResourceManager().hasResource(biomeIconResourceLocation))
                biomeResourceMap.put(biomeNameResourceLocation, biomeIconResourceLocation);
        }
    }

    public ResourceLocation getBiomeIconResourceLocation(Biome biome) {
        return biomeResourceMap.getOrDefault(ForgeRegistries.BIOMES.getKey(biome),
                new ResourceLocation(Gadgets.MOD_ID, "textures/gui/biome_icons/unknown.png"));
    }

    public void drawTexture(ResourceLocation texture, int x, int y, int width, int height) {
        mc.getTextureManager().bindTexture(texture);
        blit(x, y, 0, 0, 0, width, height, width, height);
    }

    private void renderGadgetGui(GadgetGuiItem gadgetGuiItem, Position position) {
        // If gadgetGuiPosition is along the bottom of the screen, need to accomodate for each gadgetgui's height
        // before it's rendered
        if (!displayItemsFromTop())
            position.y -= gadgetGuiItem.getHeight();

        RenderSystem.pushMatrix();

        // Shade gui item background for visibility (if enabled in config)
        if (Config.CLIENT.gadgetGuiBackground.get())
            RenderSystem.enableAlphaTest();
            fill(position.x,position.y + gadgetGuiItem.getHeight(),
                    position.x + gadgetGuiItem.getWidth(), position.y,
                    GadgetOverlayGui.BackgroundColour);
            RenderSystem.disableAlphaTest();

        gadgetGuiItem.render(mc, this, position.x, position.y);

        RenderSystem.popMatrix();

        // Render next item below if gadgetGuiPosition is along the top, render next item above if along the bottom
        if (displayItemsFromTop())
            position.y += gadgetGuiItem.getHeight() + 2;
        else
            position.y -= 2;
    }

    private boolean displayItemsFromTop() {
        return Config.CLIENT.gadgetGuiPosition.get() == Config.GadgetGuiPosition.TOP_LEFT ||
                Config.CLIENT.gadgetGuiPosition.get() == Config.GadgetGuiPosition.TOP_MIDDLE ||
                Config.CLIENT.gadgetGuiPosition.get() == Config.GadgetGuiPosition.TOP_RIGHT;
    }

    private ArrayList<String> getPlayerGadgets() {
        ClientPlayerEntity player = this.mc.player;

        return new ArrayList<> (CuriosAPI.getCuriosHandler(player).map(handler -> {
            LinkedHashSet<String> _gadgets = new LinkedHashSet<>();

            CurioStackHandler stackHandler = handler.getStackHandler("gadget");

            if (stackHandler != null) {
                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    ItemStack stack = stackHandler.getStackInSlot(i);

                    if (!stack.isEmpty()) {
                        _gadgets.add(stack.getItem().toString());
                    }
                }
            }

            return _gadgets;
        }).orElse(new LinkedHashSet<>()));
    }

    private Position getRenderStartPos() {
        Config.GadgetGuiPosition startPos = Config.CLIENT.gadgetGuiPosition.get();
        int guiWidth = Config.CLIENT.gadgetGuiWidth.get();

        int startX = (int) (startPos.getX() * mc.getMainWindow().getScaledWidth());
        int startY = (int) (startPos.getY() * mc.getMainWindow().getScaledHeight());

        switch (startPos) {

            case TOP_LEFT:
                startX += 1;
                startY += 1;
                break;
            case TOP_MIDDLE:
                startX -= guiWidth / 2;
                break;
            case TOP_RIGHT:
                startX -= guiWidth + 1;
                startY += 1;
                break;
            case BOTTOM_LEFT:
                startX += 1;
                startY -= 1;
                break;
            case BOTTOM_RIGHT:
                startX -= guiWidth + 1;
                startY -= 1;
                break;
        }

        return new Position(startX, startY);
    }
    
    private static class Position {
        public int x;
        public int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
