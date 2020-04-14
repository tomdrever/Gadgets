package tishtesh.gadgets.core.registry;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tishtesh.gadgets.common.item.GadgetItem;
import tishtesh.gadgets.core.Gadgets;

@Mod.EventBusSubscriber(modid = Gadgets.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GadgetItemRegistry {
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Gadgets.MOD_ID);

    public static RegistryObject<Item> COMPASS = ITEMS.register("compass", GadgetItem::new);
    public static RegistryObject<Item> DEPTHMETER = ITEMS.register("depthmeter", GadgetItem::new);
    public static RegistryObject<Item> BIOMETER = ITEMS.register("biometer", GadgetItem::new);
    public static RegistryObject<Item> CLOCK = ITEMS.register("clock", GadgetItem::new);
}
