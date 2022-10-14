package ru.dmitriy251075.allowedmods.server;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import ru.dmitriy251075.allowedmods.AllowedMods;
import ru.dmitriy251075.allowedmods.common.network.MessegeRequestMods;
import ru.dmitriy251075.allowedmods.common.network.NetworkLoader;

@Mod.EventBusSubscriber(modid = AllowedMods.MODID, value = Dist.DEDICATED_SERVER)
public class ServerEvents {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();

        PlayerMods playerMods = new PlayerMods(player);
        AllowedMods.ListPlayers.add(playerMods);
        playerMods.preCheck();

        NetworkLoader.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)event.getPlayer()), new MessegeRequestMods());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void playerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();

        for (int i = 0; i < AllowedMods.ListPlayers.size(); i++)
        {
            if (AllowedMods.ListPlayers.get(i).player == player)
            {
                AllowedMods.ListPlayers.removeIf(l -> l.player.equals(player.getName().getString()));
            }
        }
    }
}
