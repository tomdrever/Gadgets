package tishtesh.gadgets.core;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tishtesh.gadgets.client.gui.*;
import tishtesh.gadgets.common.item.GadgetItem;
import tishtesh.gadgets.common.recipes.CombinedGadgetRecipe;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Gadgets.MOD_ID)
public class Gadgets
{
    public static final String MOD_ID = "gadgets";

    public Gadgets() {
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);

        // Register ourselves for server and other game events we are interested in
        //MinecraftForge.EVENT_BUS.register(this);

        // Register config
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Register new Gadget curio type
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("gadget")
                        .size(Config.CLIENT.gadgetSlots.get())
                        .icon(new ResourceLocation(Gadgets.MOD_ID, "item/empty_gadget_slot"))
                        .build());
    }

    // Add sprite for empty curio slot on pre texturestitchevent
    @Mod.EventBusSubscriber(modid = Gadgets.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientProxy {
        public static GuiRenderHandler guiRenderHandler = new GuiRenderHandler();

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            MinecraftForge.EVENT_BUS.register(guiRenderHandler);

            // Use reduced debug info by default (can still be changed in settings)
            Minecraft.getInstance().options.reducedDebugInfo = true;
        }

        @SubscribeEvent
        public static void onPreTexturesStitched(TextureStitchEvent.Pre event) {
            // Add the texture for the empty gadget slot
            event.addSprite(new ResourceLocation(Gadgets.MOD_ID, "item/empty_gadget_slot"));
        }

        @SubscribeEvent
        public static void onPostTexturesStitched(TextureStitchEvent.Post event) {
            // Load textures into the biome resource map
            Icons.generateBiomeResourceMap();
        }
    }

    @Mod.EventBusSubscriber(modid = Gadgets.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class GadgetsRegistry {
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(
                    new GadgetItem("biometer"),
                    new GadgetItem("compass"),
                    new GadgetItem("depthmeter"),
                    new GadgetItem("clock"),
                    new GadgetItem("combinedgadget"));
        }

        @SubscribeEvent
        public static void registerRecipeSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
            event.getRegistry().register(CombinedGadgetRecipe.SERIALIZER);
        }
    }
}
