package tishtesh.gadgets.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GadgetCapabilityProvider implements ICapabilityProvider {

    final LazyOptional<ICurio> capability;

    GadgetCapabilityProvider() {
        this.capability = LazyOptional.of(() -> new ICurio() {
            @Override
            public boolean canEquipFromUse(SlotContext slotContext) {
                return true;
            }

            @Override
            public void onEquipFromUse(SlotContext slotContext) {
                slotContext.getWearer().level.playSound(null, slotContext.getWearer().blockPosition(), SoundEvents.ARMOR_EQUIP_GOLD, SoundCategory.NEUTRAL, 1.0f, 1.0f);
            }

            @Nonnull
            @Override
            public ICurio.DropRule getDropRule(LivingEntity livingEntity) {
                return ICurio.DropRule.DEFAULT;
            }
        });
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CuriosCapability.ITEM.orEmpty(cap, capability);
    }
}
