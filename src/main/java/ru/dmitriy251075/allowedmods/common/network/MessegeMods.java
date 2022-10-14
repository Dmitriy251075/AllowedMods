package ru.dmitriy251075.allowedmods.common.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import ru.dmitriy251075.allowedmods.AllowedMods;
import ru.dmitriy251075.allowedmods.common.config.Config;

import java.util.function.Supplier;

public class MessegeMods {

    private String mods = "";

    public MessegeMods() {}

    public MessegeMods(String mods) {
        this.mods = mods;
    }

    public static void encode(MessegeMods packet, PacketBuffer buf) {
        buf.writeString(packet.mods, 65536);
    }

    public static MessegeMods decode(PacketBuffer buf) {
        return new MessegeMods(buf.readString(65536));
    }

    public static void handle(MessegeMods message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player != null)
            {
                //AllowedMods.LOGGER.info("Server mods " + Config.Mods.get());
                AllowedMods.LOGGER.info("Player " + player.getName().getString() + " mods " + message.mods);
                boolean IsChecked = false;
                for (String mod : Config.AllowMods.get().split(":::"))
                {
                    if (message.mods.equals(mod))
                    {
                        IsChecked = true;
                        break;
                    }
                }
                if (!IsChecked)
                {
                    player.connection.disconnect(new StringTextComponent("Mods is not in whitelist."));
                }
                else
                {
                    for (int i = 0; i < AllowedMods.ListPlayers.size(); i++)
                    {
                        if (AllowedMods.ListPlayers.get(i).player == player)
                        {
                            AllowedMods.ListPlayers.get(i).postCheck();
                            AllowedMods.LOGGER.info("Player " + player.getName().getString() + " has successfully checked mods");
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
