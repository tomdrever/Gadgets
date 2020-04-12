package tishtesh.gadgets.core;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {

    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;


    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static class Client {

        public final ForgeConfigSpec.EnumValue<GadgetGuiPosition> gadgetGuiPosition;
        public final ForgeConfigSpec.BooleanValue gadgetGuiBackground;
        public final ForgeConfigSpec.BooleanValue gadgetGuiIcons;
        public final ForgeConfigSpec.IntValue gadgetGuiWidth;
        public final ForgeConfigSpec.BooleanValue hideWhenDebugShown;

        public final ForgeConfigSpec.BooleanValue needCurios;
        public final ForgeConfigSpec.IntValue gadgetSlots;

        public Client(ForgeConfigSpec.Builder builder) {
            builder.comment("Gadget client settings").push("client");

            gadgetGuiPosition = builder
                    .comment("Where on the screen to display the gadget gui")
                    .translation(Gadgets.MOD_ID + ".config.gadgetGuiPosition")
                    .defineEnum("gadgetGuiPosition", GadgetGuiPosition.TOP_LEFT);
            gadgetGuiBackground = builder
                    .comment("Display a transparent black background for the gadget gui")
                    .translation(Gadgets.MOD_ID + ".config.gadgetGuiBackground")
                    .define("gadgetGuiBackground", true);
            gadgetGuiIcons = builder
                    .comment("Display icons for gadget gui items")
                    .translation(Gadgets.MOD_ID + ".config.gadgetGuiIcons")
                    .define("gadgetGuiIcons", true);
            gadgetGuiWidth = builder
                    .comment("The width of each gadget gui item")
                    .translation(Gadgets.MOD_ID + ".config.gadgetGuiWidth")
                    .defineInRange("gadgetGuiWidth", 100, 70, 300);
            hideWhenDebugShown = builder
                    .comment("Should the gadget gui be hidden when the debug info (F3) is shown")
                    .translation(Gadgets.MOD_ID + ".config.hideWhenDebugShown")
                    .define("hideWhenDebugShown", true);

            needCurios = builder
                    .comment("If enabled, a player needs each gadget equipped for its gadget gui to be rendered. If disabled, a player " +
                            "just gets all gui items by default")
                    .translation(Gadgets.MOD_ID + ".config.needCurios")
                    .define("needCurios", true);

            gadgetSlots = builder
                    .comment("The number of slots for gadgets the player has")
                    .translation(Gadgets.MOD_ID + ".config.gadgetSlots")
                    .worldRestart()
                    .defineInRange("gadgetSlots", 3, 0, 5);

            builder.pop();
        }

    }

    public enum GadgetGuiPosition {
        TOP_LEFT(0, 0),
        TOP_MIDDLE(0.5F, 0),
        TOP_RIGHT(1, 0),
        BOTTOM_LEFT(0, 1),
        BOTTOM_RIGHT(1, 1);

        final float x;
        final float y;

        GadgetGuiPosition(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }
}
