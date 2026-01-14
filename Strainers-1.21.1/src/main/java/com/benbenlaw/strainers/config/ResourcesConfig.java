package com.benbenlaw.strainers.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class ResourcesConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Boolean> TIN;


    static {
        BUILDER.push("Strainers Config File");

        TIN = BUILDER.comment("Should Tin Ore Pieces/ Meshes/ recipe be loaded, default = false")
                .define("Tin", false);

        BUILDER.pop();
        SPEC = BUILDER.build();

    }
}