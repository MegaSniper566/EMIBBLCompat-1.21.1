package com.benbenlaw.strainers.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public final class StrainersConfigFile {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Boolean> showMeshesInWorld;
    public static final ModConfigSpec.ConfigValue<Boolean> showItemBeingStrainerInWorld;
    public static final ModConfigSpec.ConfigValue<Integer> strainerMaxProgress;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> blockDurations;

    public static final ModConfigSpec.ConfigValue<Integer> speedReduction1;
    public static final ModConfigSpec.ConfigValue<Integer> speedReduction2;
    public static final ModConfigSpec.ConfigValue<Integer> speedReduction3;
    public static final ModConfigSpec.ConfigValue<Double> chanceIncrease1;
    public static final ModConfigSpec.ConfigValue<Double> chanceIncrease2;
    public static final ModConfigSpec.ConfigValue<Double> chanceIncrease3;




    static {
        BUILDER.push("Strainers Config File");

        showMeshesInWorld = BUILDER.comment("Show the strainer meshes in the world, default = true")
                .define("Show Meshes", true);

        showItemBeingStrainerInWorld = BUILDER.comment("Show items in / on the strainer in the world, default = true")
                .define("Show Items ", true);

        strainerMaxProgress = BUILDER.comment("The maximum progress of the strainer, default = 220,")
                .defineInRange("Strainer Max Progress", 220, 1, Integer.MAX_VALUE);

        blockDurations = BUILDER.comment("The durations of the blocks in the world, default = [], example \"minecraft:cherry_leaves=1000\"")
                .defineList("Block Durations", List.of(), o -> o instanceof String);

        speedReduction1 = BUILDER.comment("The speed reduction of the tier 1 upgrade, default = 50")
                        .define("Speed Reduction 1", 50);

        speedReduction2 = BUILDER.comment("The speed reduction of the tier 2 upgrade, default = 100")
                        .define("Speed Reduction 2", 100);

        speedReduction3 = BUILDER.comment("The speed reduction of the tier 3 upgrade, default = 150")
                        .define("Speed Reduction 3", 150);

        chanceIncrease1 = BUILDER.comment("The chance increase of the tier 1 upgrade, default = 0.1")
                .define("Chance Increase 1",  0.1);

        chanceIncrease2 = BUILDER.comment("The chance increase of the tier 2 upgrade, default = 0.2")
                .define("Chance Increase 2",  0.2);

        chanceIncrease3 = BUILDER.comment("The chance increase of the tier 3 upgrade, default = 0.3")
                .define("Chance Increase 3",  0.3);


        BUILDER.pop();
        SPEC = BUILDER.build();

    }
}
