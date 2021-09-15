package tishtesh.gadgets.core;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tishtesh.gadgets.client.gui.GuiRenderHandler;
import tishtesh.gadgets.core.registry.GadgetItemRegistry;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Gadgets.MOD_ID)
public class Gadgets
{
    public static final String MOD_ID = "gadgets";

    private static GuiRenderHandler guiRenderHandler;

    public Gadgets() {
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register gadget items
        GadgetItemRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register client and common configs
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        guiRenderHandler = new GuiRenderHandler();
        MinecraftForge.EVENT_BUS.register(guiRenderHandler);

        // Use reduced debug info by default (can still be changed in settings)
        Minecraft.getInstance().options.reducedDebugInfo = true;
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Register new Gadget curio type
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage.Builder("gadget")
                        .size(Config.CLIENT.gadgetSlots.get())
                        .icon(new ResourceLocation(Gadgets.MOD_ID, "item/empty_gadget_slot"))
                        .build());
    }

    // Add sprite for empty curio slot on pre texturestitchevent
    @Mod.EventBusSubscriber(modid = Gadgets.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class ClientProxy {
        @SubscribeEvent
        public static void onPreTexturesStitched(TextureStitchEvent.Pre event) {
            // Add the texture for the empty gadget slot
            event.addSprite(new ResourceLocation(Gadgets.MOD_ID, "item/empty_gadget_slot"));
        }

        @SubscribeEvent
        public static void onPostTexturesStitched(TextureStitchEvent.Post event) {
            // Load textures into the biome resource map
            guiRenderHandler.gui.generateBiomeResourceMap();
        }
    }
}
