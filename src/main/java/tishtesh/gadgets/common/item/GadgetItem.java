package tishtesh.gadgets.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class GadgetItem extends Item implements ICurioItem {

    public GadgetItem(String name) {
        super(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).stacksTo(1));
        this.setRegistryName(name);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_GOLD, 1.0f, 1.0f);
    }

    public ArrayList<String> getGuiItems(ItemStack stack) {
        ArrayList<String> guiItems = new ArrayList<>();

        if (stack.getItem().getRegistryName().getPath() == "combinedgadget") {
            // get items from nbt
            if (stack.hasTag()) {
                guiItems.addAll(Arrays.asList(getNbtKeysInOrder(stack)));
            }
            // if no tag - return emtpy list
        }
        else {
            guiItems.add(stack.getItem().getRegistryName().getPath());
        }

        return guiItems;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        if (stack.getItem().getRegistryName().getPath() == "combinedgadget") {
            // get items from nbt
            if (stack.hasTag()) {
                for (String item : getNbtKeysInOrder(stack)) {
                    tooltip.add(new TextComponent(item.substring(0, 1).toUpperCase() + item.substring(1)));
                }
            }
            else {
                tooltip.add(new TextComponent("Empty"));
            }
        }
    }

    private String[] getNbtKeysInOrder(ItemStack stack) {
        CompoundTag nbt = stack.getTagElement("gadgets");

        Map<Integer, String> keyValues = new TreeMap<>();

        for (String key : nbt.getAllKeys()) {
            keyValues.put(nbt.getInt(key), key);
        }

        return keyValues.values().toArray(new String[0]);
    }
}
