package ru.dmitriy251075.allowedmods.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import ru.dmitriy251075.allowedmods.AllowedMods;

import java.util.function.Supplier;

public class MessegeRequestMods {
    public static void encode(MessegeRequestMods packet, PacketBuffer buf) {}

    public static MessegeRequestMods decode(PacketBuffer buffer) {
        return new MessegeRequestMods();
    }

    public static void handle(MessegeRequestMods message, Supplier<NetworkEvent.Context> ctx) {
        AllowedMods.LOGGER.info("Sending modslist packet to the server...");
        NetworkLoader.INSTANCE.sendToServer(new MessegeMods(AllowedMods.Mods));
        ctx.get().setPacketHandled(true);
    }
}
