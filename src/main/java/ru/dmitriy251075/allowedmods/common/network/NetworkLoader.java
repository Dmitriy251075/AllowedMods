package ru.dmitriy251075.allowedmods.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import ru.dmitriy251075.allowedmods.AllowedMods;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NetworkLoader {

    private static final String PROTOCOL_VERSION = "1.0";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(AllowedMods.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        registerPacket(MessegeMods.class, MessegeMods::encode, MessegeMods::decode, MessegeMods::handle, NetworkDirection.PLAY_TO_SERVER);
        registerPacket(MessegeRequestMods.class, MessegeRequestMods::encode, MessegeRequestMods::decode, MessegeRequestMods::handle, NetworkDirection.PLAY_TO_CLIENT);
    }

    private static int id = 0;

    private static <MSG> void registerPacket(Class<MSG> msg, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> handler, NetworkDirection direction) {
        INSTANCE.registerMessage(id++,msg,encoder,decoder,handler, Optional.ofNullable(direction));
    }
}
