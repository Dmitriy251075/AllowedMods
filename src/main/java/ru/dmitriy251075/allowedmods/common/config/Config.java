package ru.dmitriy251075.allowedmods.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class Config {

    public static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec spec;

    public static final ForgeConfigSpec.ConfigValue<String> AllowMods;

    public static final ForgeConfigSpec.ConfigValue<Integer> Timeout;

    static
    {
        builder.push("Config AllowMods");

        AllowMods = builder.define("Mods", "");
        Timeout = builder.define("Timeout", 600);

        builder.pop();
        spec = builder.build();
    }

}
