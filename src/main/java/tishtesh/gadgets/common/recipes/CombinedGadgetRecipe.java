package tishtesh.gadgets.common.recipes;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.AirItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import tishtesh.gadgets.common.item.GadgetItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// From https://github.com/Deadbeetle/literature-mod/blob/master/src/main/java/com/magnus/literature/recipes/CoveredBookRecipe.java
public class CombinedGadgetRecipe implements ICraftingRecipe {

    public static final Serializer SERIALIZER = new Serializer();

    private final ResourceLocation id;

    public CombinedGadgetRecipe(ResourceLocation idIn) {
        id = idIn;
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        int gadgets = 0;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;
            Item item = stack.getItem();
            if (item instanceof AirItem) continue;
            if (item instanceof GadgetItem)
                gadgets ++;
        }
        return gadgets == 2;
    }

    @Override
    public ItemStack assemble(CraftingInventory inv) {
        ItemStack gadget1 = ItemStack.EMPTY;
        ItemStack gadget2 = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            Item item = stack.getItem();
            if (item instanceof GadgetItem) {
                if (gadget1.isEmpty()) {
                    gadget1 = stack;
                }
                else if (gadget2.isEmpty()) {
                    gadget2 = stack;
                }
            }
        }

        if (gadget1.isEmpty() || gadget2.isEmpty()) // Should only happen if the result from matches() gets ignored
            return ItemStack.EMPTY;

        // Create a list of unique gui items from both gadgets
        Set<String> guiItemSet = new LinkedHashSet<>(((GadgetItem) gadget1.getItem()).getGuiItems(gadget1));
        guiItemSet.addAll(((GadgetItem) gadget2.getItem()).getGuiItems(gadget2));
        List<String> guiItems = new ArrayList<>(guiItemSet);

        ItemStack newCombinedGadget = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gadgets:combinedgadget")));

        CompoundNBT gadgetsNBT = new CompoundNBT();
        for (int i = 0; i < guiItems.size(); i++) {
            gadgetsNBT.putInt(guiItems.get(i), i);
        }
        newCombinedGadget.addTagElement("gadgets", gadgetsNBT);

        return newCombinedGadget;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 4;
    }

    @Override
    public ItemStack getResultItem() {
        return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("gadgets:combinedgadget")));
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CombinedGadgetRecipe> {

        Serializer() {
            this.setRegistryName(new ResourceLocation("gadgets", "combinedgadget"));
        }

        @Override
        public CombinedGadgetRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            return new CombinedGadgetRecipe(recipeId);
        }

        @Nullable
        @Override
        public CombinedGadgetRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            return new CombinedGadgetRecipe(recipeId);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, CombinedGadgetRecipe recipe) {

        }
    }
}
