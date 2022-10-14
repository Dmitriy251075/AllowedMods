package ru.dmitriy251075.allowedmods;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.dmitriy251075.allowedmods.common.config.Config;
import ru.dmitriy251075.allowedmods.common.network.NetworkLoader;
import ru.dmitriy251075.allowedmods.server.PlayerMods;

import java.util.ArrayList;
import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AllowedMods.MODID)
public class AllowedMods {

    public static final String MODID = "allowedmods";

    public static final Logger LOGGER = LogManager.getLogger();

    public static List<PlayerMods> ListPlayers = new ArrayList<>();

    public static String Mods;

    public AllowedMods() {
        MinecraftForge.EVENT_BUS.register(this);
        NetworkLoader.registerPackets();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.spec, "allowedmods.toml");
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        String StrMods = "";

        List<ModInfo> list = FMLLoader.getLoadingModList().getMods();

        for (ModInfo modInfo : list)
        {
            StrMods = StrMods + Hashmod(modInfo).hashCode();
        }

        AllowedMods.Mods = StrMods;

        Config.AllowMods.set(StrMods);
    }

    private String Hashmod(ModInfo modInfo) {
        String StrMod = "";
        StrMod = StrMod + " " + modInfo.getModId()
                + modInfo.getModId().hashCode();
        StrMod = StrMod + " " + modInfo.getDisplayName()
                + modInfo.getDisplayName().hashCode();
        StrMod = StrMod + " " + modInfo.getNamespace()
                + modInfo.getNamespace().hashCode();
        StrMod = StrMod + " " + modInfo.getDescription()
                + modInfo.getDescription().hashCode();
        if (modInfo.getOwningFile() != null)
        {
            StrMod = StrMod + " " + modInfo.getOwningFile().getModLoader()
                    + modInfo.getOwningFile().getModLoader().hashCode()
                    + modInfo.getOwningFile().getLicense()
                    + modInfo.getOwningFile().getLicense().hashCode()
                    + modInfo.getOwningFile().getFile().getFileName()
                    + modInfo.getOwningFile().getFile().getFileName().hashCode()
                    + modInfo.getOwningFile().getModLoaderVersion().toString()
                    + modInfo.getOwningFile().getModLoaderVersion().toString().hashCode();
            if (modInfo.getOwningFile().getTrustData().isPresent())
                StrMod = StrMod + modInfo.getOwningFile().getTrustData().get()
                        + modInfo.getOwningFile().getTrustData().get().hashCode()
                        + modInfo.getOwningFile().getTrustData().isPresent();
            if (modInfo.getOwningFile().getManifest().isPresent())
                StrMod = StrMod + modInfo.getOwningFile().getManifest().get().hashCode()
                        + modInfo.getOwningFile().getManifest().isPresent();
            if (modInfo.getOwningFile().getCodeSigningFingerprint().isPresent())
                StrMod = StrMod + modInfo.getOwningFile().getCodeSigningFingerprint().get()
                        + modInfo.getOwningFile().getCodeSigningFingerprint().get().hashCode()
                        + modInfo.getOwningFile().getCodeSigningFingerprint().isPresent();
            if (modInfo.getOwningFile().getCodeSigners().isPresent())
                StrMod = StrMod + modInfo.getOwningFile().getCodeSigners().get().length
                        + modInfo.getOwningFile().getCodeSigners().isPresent();
        }
        if (modInfo.getUpdateURL() != null)
            StrMod = StrMod + " " + modInfo.getUpdateURL().toString()
                    + modInfo.getUpdateURL().toString().hashCode();
        if (modInfo.getVersion() != null)
            StrMod = StrMod + " " + modInfo.getVersion().getQualifier()
                    + modInfo.getVersion().getIncrementalVersion()
                    + modInfo.getVersion().getMajorVersion()
                    + modInfo.getVersion().getMinorVersion()
                    + modInfo.getVersion().getBuildNumber();
        if (modInfo.getDependencies() != null)
            StrMod = StrMod + " " + modInfo.getDependencies().size();

        return StrMod;
    }
}
