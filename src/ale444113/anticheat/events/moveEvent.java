package ale444113.anticheat.events;

import ale444113.anticheat.AntiCheat;
import ale444113.anticheat.events.blatant.CheckFly;
import ale444113.anticheat.events.blatant.CheckSpeed;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


public class moveEvent implements Listener {

    private final AntiCheat plugin;
    public moveEvent(AntiCheat plugin) {
        this.plugin = plugin;
    }

    private boolean isEnable(String name){
        FileConfiguration config = plugin.getConfig();
        String enable = config.getString(String.format("Config.%s.enable", name)).toLowerCase();
        return enable.equals("true");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        boolean hacks_found = false;
        FileConfiguration config = plugin.getConfig();
        if (isEnable("speed")){hacks_found = CheckSpeed.check(e,config,plugin);}
        if (isEnable("fly") && !hacks_found){hacks_found = CheckFly.check(e,config,plugin);}
        //todo Look a better way of doing this shit <3

    }
}
