package tishtesh.gadgets.client.gui;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import tishtesh.gadgets.core.Gadgets;

public class Icons {
    public static final ResourceLocation CompassGuiIcon = new ResourceLocation(Gadgets.MOD_ID, "textures/gui/compass_gui_icon.png");
    public static final ResourceLocation DepthmeterGuiIcon = new ResourceLocation(Gadgets.MOD_ID, "textures/gui/depthmeter_gui_icon.png");

    public static final ResourceLocation OverworldClockGuiIcon = new ResourceLocation(Gadgets.MOD_ID, "textures/gui/clock_icons/overworld.png");
    public static final ResourceLocation NetherClockGuiIcon = new ResourceLocation(Gadgets.MOD_ID, "textures/gui/clock_icons/the_nether.png");
    public static final ResourceLocation EndClockGuiIcon = new ResourceLocation(Gadgets.MOD_ID, "textures/gui/clock_icons/the_end.png");

    private static final HashMap<ResourceLocation, ResourceLocation> biomeResourceMap = new HashMap<>();

    /**
     * Should be called when resources are loaded
     * Creates a map of biomename to biomeicon, so the biometer gadget gui can quickly map the name it gets from the player
     * to an icon
     */
    public static void generateBiomeResourceMap() {
        biomeResourceMap.clear();
        for (ResourceLocation biomeNameResourceLocation : ForgeRegistries.BIOMES.getKeys()) {
            ResourceLocation biomeIconResourceLocation = new ResourceLocation(Gadgets.MOD_ID,
                    String.format("textures/gui/biome_icons/%s/%s.png", biomeNameResourceLocation.toString().split(":")));

            if (Minecraft.getInstance().getResourceManager().hasResource(biomeIconResourceLocation))
                biomeResourceMap.put(biomeNameResourceLocation, biomeIconResourceLocation);
        }
    }

    public static ResourceLocation getBiomeIconResourceLocation(ResourceLocation biomeNameResourceLocation) {
        return biomeResourceMap.getOrDefault(biomeNameResourceLocation, new ResourceLocation(Gadgets.MOD_ID, "textures/gui/biome_icons/unknown.png"));
    }
}
