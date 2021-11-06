package tishtesh.gadgets.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import tishtesh.gadgets.common.item.GadgetItem;
import tishtesh.gadgets.core.Config;
import tishtesh.gadgets.core.Gadgets;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GadgetOverlayGui extends AbstractGui {
    public static int BackgroundColour = 0x40000000;
    public final static int TextColour = 0xFFFFFFFF;

    public final static float SmallTextSF = 0.9f;

    private final Minecraft minecraft;
    private final HashMap<ResourceLocation, ResourceLocation> biomeResourceMap;

    private final HashMap<String, GadgetGuiItem> gadgetGuiItems;

    public GadgetOverlayGui(Minecraft minecraft) {
        this.minecraft = minecraft;
        biomeResourceMap = new HashMap<>();

        gadgetGuiItems = new HashMap<>();
        gadgetGuiItems.put("clock", new ClockGuiItem());
        gadgetGuiItems.put("compass", new CompassGuiItem());
        gadgetGuiItems.put("depthmeter", new DepthmeterGuiItem());
        gadgetGuiItems.put("biometer", new BiometerGuiItem());
    }

    @OnlyIn(Dist.CLIENT)
    public void render(MatrixStack matrixStack) {
        Position position = getRenderStartPos();

        if (Config.CLIENT.needCurios.get()) {
            // Go through the gadgets the player has on and render the corresponding gui items.
            ArrayList<GadgetGuiItem> playerGadgets = getPlayerGadgets();

            // If displaying at the bottom of the screen, reverse the list so the order matches the order of gadgets
            // in the player's curio slots
            if (!displayItemsFromTop())
                Collections.reverse(playerGadgets);

            for (GadgetGuiItem gadgetGuiItem : playerGadgets) {
                renderGadgetGui(matrixStack, gadgetGuiItem, position);
            }
        }
        else {
            // Just go through all gadget gui items
            for (GadgetGuiItem gadgetGuiItem : gadgetGuiItems.values()) {
                renderGadgetGui(matrixStack, gadgetGuiItem, position);
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

            if (minecraft.getResourceManager().hasResource(biomeIconResourceLocation))
                biomeResourceMap.put(biomeNameResourceLocation, biomeIconResourceLocation);
        }
    }

    public ResourceLocation getBiomeIconResourceLocation(ResourceLocation biomeNameResourceLocation) {
        return biomeResourceMap.getOrDefault(biomeNameResourceLocation, new ResourceLocation(Gadgets.MOD_ID, "textures/gui/biome_icons/unknown.png"));
    }

    public void drawTexture(MatrixStack matrixStack, ResourceLocation texture, int x, int y, int width, int height) {
        minecraft.getTextureManager().bind(texture);
        blit(matrixStack, x, y, 0, 0, 0, width, height, width, height);
    }

    private void renderGadgetGui(MatrixStack matrixStack, GadgetGuiItem gadgetGuiItem, Position position) {
        // If gadgetGuiPosition is along the bottom of the screen, need to accomodate for each gadgetgui's height
        // before it's rendered
        if (!displayItemsFromTop())
            position.y -= gadgetGuiItem.getHeight();

        RenderSystem.pushMatrix();

        // Shade gui item background for visibility (if enabled in config)
        if (Config.CLIENT.gadgetGuiBackground.get()) {
            RenderSystem.enableAlphaTest();
            fill(matrixStack, position.x, position.y + gadgetGuiItem.getHeight(),
                    position.x + gadgetGuiItem.getWidth(), position.y,
                    GadgetOverlayGui.BackgroundColour);
            RenderSystem.disableAlphaTest();
        }

        gadgetGuiItem.render(matrixStack, minecraft, this, position.x, position.y);

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

    private ArrayList<GadgetGuiItem> getPlayerGadgets() {
        ArrayList<GadgetGuiItem> playerGadgets = new ArrayList<>();

        // Get gui items of all equipped gadgets
        CuriosApi.getCuriosHelper().getEquippedCurios(minecraft.player).ifPresent(itemHandler -> {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack stack = itemHandler.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getItem() instanceof GadgetItem) {
                    for (String gadgetItemName : ((GadgetItem) stack.getItem()).getGuiItems(stack)) {
                        playerGadgets.add(gadgetGuiItems.get(gadgetItemName));
                    }
                }
            }
        });

        return playerGadgets;
    }

    private Position getRenderStartPos() {
        Config.GadgetGuiPosition startPos = Config.CLIENT.gadgetGuiPosition.get();
        int guiWidth = Config.CLIENT.gadgetGuiWidth.get();

        int startX = (int) (startPos.getX() * minecraft.getWindow().getGuiScaledWidth());
        int startY = (int) (startPos.getY() * minecraft.getWindow().getGuiScaledHeight());

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
