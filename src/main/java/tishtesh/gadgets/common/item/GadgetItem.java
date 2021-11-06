package tishtesh.gadgets.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.*;

public class GadgetItem extends Item implements ICurioItem {

    public GadgetItem(String name) {
        super(new Item.Properties().tab(ItemGroup.TAB_TOOLS).stacksTo(1));
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

    public ArrayList<String> getGuiItems(ItemStack itemStack) {
        ArrayList<String> guiItems = new ArrayList<>();

        if (itemStack.getItem().getRegistryName().getPath() == "combinedgadget") {
            // get items from nbt
            if (itemStack.hasTag()) {
                guiItems.addAll(Arrays.asList(getNbtKeysInOrder(itemStack)));
            }
            // if no tag - return emtpy list
        }
        else {
            guiItems.add(itemStack.getItem().getRegistryName().getPath());
        }

        return guiItems;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(itemStack, world, tooltip, flag);

        if (itemStack.getItem().getRegistryName().getPath() == "combinedgadget") {
            // get items from nbt
            if (itemStack.hasTag()) {
                for (String item : getNbtKeysInOrder(itemStack)) {
                    tooltip.add(new StringTextComponent(item.substring(0, 1).toUpperCase() + item.substring(1)));
                }
            }
            else {
                tooltip.add(new StringTextComponent("Empty"));
            }
        }
    }

    private String[] getNbtKeysInOrder(ItemStack itemStack) {
        CompoundNBT nbt = itemStack.getTagElement("gadgets");

        Map<Integer, String> keyValues = new TreeMap<>();

        for (String key : nbt.getAllKeys()) {
            keyValues.put(nbt.getInt(key), key);
        }

        return keyValues.values().toArray(new String[0]);
    }
}
