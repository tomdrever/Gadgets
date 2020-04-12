package tishtesh.gadgets.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class GadgetItem extends Item{
    public GadgetItem() {
        super(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1));
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT unused) {
        return new GadgetCapabilityProvider();
    }
}
