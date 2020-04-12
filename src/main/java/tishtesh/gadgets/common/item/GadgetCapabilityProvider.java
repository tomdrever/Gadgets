package tishtesh.gadgets.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GadgetCapabilityProvider implements ICapabilityProvider {

    final LazyOptional<ICurio> capability;

    GadgetCapabilityProvider() {
        this.capability = LazyOptional.of(() -> new ICurio() {

            @Override
            public void playEquipSound(LivingEntity livingEntity) {
                livingEntity.world
                        .playSound(null, livingEntity.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_GOLD,
                                SoundCategory.NEUTRAL, 1.0f, 1.0f);
            }

            @Nonnull
            @Override
            public ICurio.DropRule getDropRule(LivingEntity livingEntity) {
                return ICurio.DropRule.DEFAULT;
            }

            @Override
            public boolean canRightClickEquip() {
                return true;
            }
        });
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        return CuriosCapability.ITEM.orEmpty(cap, capability);
    }
}
