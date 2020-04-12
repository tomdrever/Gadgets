package tishtesh.gadgets.core;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tishtesh.gadgets.client.gui.GuiRenderHandler;
import tishtesh.gadgets.core.registry.GadgetItemRegistry;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Gadgets.MOD_ID)
public class Gadgets
{
    public static final String MOD_ID = "gadgets";

    private static GuiRenderHandler guiRenderHandler;

    public Gadgets() {
        guiRenderHandler = new GuiRenderHandler();

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);

        GadgetItemRegistry.ITEMS.register( FMLJavaModLoadingContext.get().getModEventBus());

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(guiRenderHandler);

        // Register client and common configs
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // Use reduced debug info by default (can still be changed in settings)
        Minecraft.getInstance().gameSettings.reducedDebugInfo = true;
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("gadget")
                .setSize(Config.CLIENT.gadgetSlots.get()));

        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_ICON, () -> new Tuple<>("gadget",
                new ResourceLocation(Gadgets.MOD_ID, "item/empty_gadget_slot")));
    }

    @Mod.EventBusSubscriber(modid = Gadgets.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class ClientProxy {
        @SubscribeEvent
        public static void onPreTexturesStitched(TextureStitchEvent.Pre event) {
            // Add the texture for the empty gadget slot
            if (event.getMap().getTextureLocation() == PlayerContainer.LOCATION_BLOCKS_TEXTURE)
                event.addSprite(new ResourceLocation(Gadgets.MOD_ID, "item/empty_gadget_slot"));
        }

        @SubscribeEvent
        public static void onPostTexturesStitched(TextureStitchEvent.Post event) {
            // Load textures into the biome resource map
            guiRenderHandler.gui.generateBiomeResourceMap();
        }
    }
}
