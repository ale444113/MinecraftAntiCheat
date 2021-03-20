package ale444113.anticheat.events.playerEvents;

import ale444113.anticheat.AntiCheat;
import ale444113.anticheat.events.blatant.CheckFly;
import ale444113.anticheat.events.blatant.CheckSpeed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    private final AntiCheat plugin;

    public PlayerJoin(AntiCheat plugin) {
        this.plugin = plugin;
    }

    public static void createPlayerWarnings(Player player) {
        CheckSpeed.playerSpeedWarnings.put(player.getName(), 0);
        CheckSpeed.playerContactSlime.put(player, false);

        CheckFly.playerFlyWarnings.put(player.getName(), 0);
        CheckFly.checkPlayerPositions.put(player.getName(), 0);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        createPlayerWarnings(player);
    }
}
