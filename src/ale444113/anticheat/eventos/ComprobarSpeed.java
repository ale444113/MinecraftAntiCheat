package ale444113.anticheat.eventos;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import ale444113.anticheat.AntiCheat;
import net.md_5.bungee.api.ChatColor;

public class ComprobarSpeed implements Listener {
    private AntiCheat plugin;

    public ComprobarSpeed(AntiCheat plugin) {
        this.plugin = plugin;
    }

    public static HashMap<String, Integer> playerSpeedWarnings = new HashMap<String, Integer>();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        FileConfiguration config = plugin.getConfig();
        String enable = config.getString("Config.speed.enable");
        if (enable.equals("true")) {
            Player player = e.getPlayer();
            Double distance = e.getFrom().distance(e.getTo());
            //Load data from config
            String pathSpeedCommand = "Config.speed.reason";
            String pathBanApeal = "Config.apeal";
            String punishment = config.getString("Config.speed.punishment");;
            int Warnings = config.getInt("Config.speed.warnings");

            String toExecuteCommand = config.getString("Config.speed.punishment").replace("{user}",player.getName()).replace("{reason}",plugin.nombre + ChatColor.RED +config.getString(pathSpeedCommand));
            String command = toExecuteCommand + " " + ChatColor.YELLOW + config.getString(pathBanApeal);
            if(config.getString("Config.speed.punishment-time") != Integer.toString(0)){command = command.replace("{time}", config.getString("Config.speed.punishment-time"));}

            String pathSpeedVelocity = "Config.speed.velocity";
            Double maxvelocity = config.getDouble(pathSpeedVelocity);
            Boolean touchedSlime = false;
            Material materialdown = player.getLocation().clone().subtract(0, 1, 0).getBlock().getType();
            if (materialdown.equals(Material.SLIME_BLOCK)) {
                touchedSlime = true;
            } else if (!materialdown.equals(Material.AIR)) {
                if (distance > maxvelocity && !player.getAllowFlight() && !player.isInsideVehicle() && e.getFrom().getY() <= e.getTo().getY() && !player.isDead() && !touchedSlime) {
                    try {
                        playerSpeedWarnings.put(player.getName(), playerSpeedWarnings.get(player.getName()) + 1);
                    } catch (Exception error) {
                        playerSpeedWarnings.put(player.getName(), 1);
                    }
                    String messageToSendWarning = config.getString("Config.speed.staff-message");
                    String warningsStringSpeed = Integer.toString(playerSpeedWarnings.get(player.getName())) + "/" + Integer.toString(Warnings);
                    String messageToSendWarning2 = messageToSendWarning.replace("{warnings}", warningsStringSpeed);
                    String messageToSendWarning3 = messageToSendWarning2.replace("{user}", player.getName());
                    if(playerSpeedWarnings.get(player.getName()) != Warnings) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (p.hasPermission("anticheat.warnings")) {
                                p.sendMessage(plugin.nombre + ChatColor.DARK_RED + messageToSendWarning3);
                            }
                        }
                    }

                    if (playerSpeedWarnings.get(player.getName()) == Warnings) {
                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        Bukkit.dispatchCommand(console, command);
                        playerSpeedWarnings.remove(player.getName());
                        String toSendMessage = config.getString("Config.speed.staff-message-punishment").replace("{user}", player.getName()).replace("{punishment}", punishment.replace("{user}","").replace("{reason}","").replace("{time}",""));
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (p.hasPermission("anticheat.warnings")) {
                                p.sendMessage(plugin.nombre + ChatColor.DARK_RED + toSendMessage);
                            }
                        }
                    }
                }
            }
        }
    }
}