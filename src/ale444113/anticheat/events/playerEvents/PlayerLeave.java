package ale444113.anticheat.events.playerEvents;

import ale444113.anticheat.AntiCheat;
import ale444113.anticheat.events.blatant.CheckFly;
import ale444113.anticheat.events.blatant.CheckSpeed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    private final AntiCheat plugin;
    public PlayerLeave(AntiCheat plugin) {
        this.plugin = plugin;
    }

    public static void deletePlayerWarnings(Player player){
        CheckSpeed.playerSpeedWarnings.remove(player.getName());
        CheckSpeed.playerContactSlime.remove(player);

        CheckFly.playerFlyWarnings.remove(player.getName());
        CheckFly.checkPlayerPositions.remove(player.getName());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        deletePlayerWarnings(player);
    }

}
