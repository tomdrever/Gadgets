package tishtesh.gadgets.common.recipes;

import com.google.gson.JsonObject;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import tishtesh.gadgets.common.item.GadgetItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CombinedGadgetRecipe extends ShapelessRecipe {

    public static final Serializer SERIALIZER = new Serializer();

    public CombinedGadgetRecipe(ResourceLocation id, String group, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(id, group, result, ingredients);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
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
    public ItemStack assemble(CraftingContainer inv) {
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

        CompoundTag gadgetsNBT = new CompoundTag();
        for (int i = 0; i < guiItems.size(); i++) {
            gadgetsNBT.putInt(guiItems.get(i), i);
        }
        newCombinedGadget.addTagElement("gadgets", gadgetsNBT);

        return newCombinedGadget;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    private static class Serializer extends ShapelessRecipe.Serializer{

        Serializer() {
            this.setRegistryName(new ResourceLocation("gadgets", "combinedgadget"));
        }

        @Override
        public CombinedGadgetRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ShapelessRecipe r = super.fromJson(recipeId, json);
            return new CombinedGadgetRecipe(r.getId(), r.getGroup(), r.getResultItem(), r.getIngredients());
        }

        @Nullable
        @Override
        public CombinedGadgetRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            ShapelessRecipe r = super.fromNetwork(recipeId, buffer);
            return new CombinedGadgetRecipe(r.getId(), r.getGroup(), r.getResultItem(), r.getIngredients());
        }
    }
}
