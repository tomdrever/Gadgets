package tishtesh.gadgets.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import tishtesh.gadgets.client.gui.GadgetGuiItem;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class GadgetItem extends Item implements ICurioItem {

    private final GadgetGuiItem guiItem;

    public GadgetItem(String name, GadgetGuiItem guiItem) {
        super(new Item.Properties().tab(ItemGroup.TAB_TOOLS).stacksTo(1));
        this.guiItem = guiItem;
        this.setRegistryName(name);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack itemStack) {
        return true;
    }

    @Override
    public void onEquipFromUse(SlotContext slotContext, ItemStack itemStack) {
        slotContext.getWearer().level.playSound(null, slotContext.getWearer().blockPosition(), SoundEvents.ARMOR_EQUIP_GOLD, SoundCategory.NEUTRAL, 1.0f, 1.0f);
    }

    public GadgetGuiItem getGuiItem() {
        return this.guiItem;
    }
}
