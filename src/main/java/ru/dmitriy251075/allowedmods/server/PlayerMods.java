package ru.dmitriy251075.allowedmods.server;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import ru.dmitriy251075.allowedmods.common.config.Config;
import ru.dmitriy251075.allowedmods.common.network.MessegeRequestMods;
import ru.dmitriy251075.allowedmods.common.network.NetworkLoader;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

public class PlayerMods {

    private static final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(2, new ThreadFactoryBuilder()
            .setNameFormat("AllowedMods-Worker-%d")
            .build());
    private static final Map<String, ScheduledFuture<?>> futures = new ConcurrentHashMap<>();

    public ServerPlayerEntity player = null;

    PlayerMods(ServerPlayerEntity player)
    {
        this.player = player;
    }

    public void preCheck()
    {
        Vector3d pos = player.getPositionVec();

        // Restrict movement
        ScheduledFuture<?> future = executor.scheduleWithFixedDelay(() -> {
            ServerLifecycleHooks.getCurrentServer().deferTask(() -> {
                player.setPosition(pos.x, pos.y, pos.z);
            });
        }, 0, 100, TimeUnit.MILLISECONDS);

        Optional.ofNullable(futures.put(player.getName().getString(), future))
                .ifPresent(f -> f.cancel(true));

        // Timeout
        ScheduledFuture<?> future2 = executor.schedule(() -> {
            player.connection.disconnect(new StringTextComponent("Timeout check mods"));
        }, Config.Timeout.get(), TimeUnit.SECONDS);

        Optional.ofNullable(futures.put(player.getName().getString(), future2))
                .ifPresent(f -> f.cancel(true));

        // Resend request
        ScheduledFuture<?> future3 = executor.scheduleWithFixedDelay(() -> {
            NetworkLoader.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new MessegeRequestMods());
        }, 0, 5, TimeUnit.SECONDS);

        Optional.ofNullable(futures.put(player.getName().getString(), future3))
                .ifPresent(f -> f.cancel(true));
    };

    public void postCheck()
    {
        Optional.ofNullable(futures.remove(player.getName().getString())).ifPresent(f -> f.cancel(true));
    };
}
