package ale444113.anticheat.eventos;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import ale444113.anticheat.AntiCheat;
import net.md_5.bungee.api.ChatColor;

public class ComprobarFly implements Listener {

    public static HashMap<String, Integer> playerFlyWarnings = new HashMap<String, Integer>();
    public static HashMap<String, Integer> checkPlayerPositions = new HashMap<String, Integer>();
    public static HashMap<String, Location> checkPlayerPositionsCoords = new HashMap<String, Location>();

    private AntiCheat plugin;

    public ComprobarFly(AntiCheat plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        FileConfiguration config = plugin.getConfig();
        String enable = config.getString("Config.fly.enable");
        if (enable.equals("true")) {
            Player player = e.getPlayer();
            //Load data from config
            String pathBanApeal = "Config.apeal";
            String pathBanCommand = "Config.fly.reason";
            String punishment = config.getString("Config.fly.punishment");
            int Warnings = config.getInt("Config.fly.warnings");

            String toExecuteCommand = punishment.replace("{user}",player.getName()).replace("{reason}",plugin.nombre + ChatColor.RED +config.getString(pathBanCommand));
            String command = toExecuteCommand + " " + ChatColor.YELLOW + config.getString(pathBanApeal);
            if(config.getString("Config.fly.punishment-time") != Integer.toString(0)){command = command.replace("{time}", config.getString("Config.fly.punishment-time"));}

            if (player.isOnGround() || player.getAllowFlight() || player.getLocation().clone().subtract(0, 0.1, 0).getBlock().getType() == Material.LADDER || e.getTo().getBlock().isLiquid()) {
                checkPlayerPositions.put(player.getName(), 0);
            } else {
                try {
                    checkPlayerPositions.put(player.getName(), checkPlayerPositions.get(player.getName()) + 1);
                } catch (Exception error) {
                    checkPlayerPositions.put(player.getName(), 1);
                }
            }

            if (checkPlayerPositions.get(player.getName()) == 20) {
                checkPlayerPositionsCoords.put(player.getName(), player.getLocation());
            } else if (checkPlayerPositions.get(player.getName()) == 40) {
                if (player.getLocation().getY() >= checkPlayerPositionsCoords.get(player.getName()).getY()) {
                    try {
                        playerFlyWarnings.put(player.getName(), playerFlyWarnings.get(player.getName()) + 1);
                    } catch (Exception error) {
                        playerFlyWarnings.put(player.getName(), 1);
                    }
                    String messageToSendWarning = config.getString("Config.fly.staff-message");
                    String warningsStringFly = Integer.toString(playerFlyWarnings.get(player.getName())) + "/" + Integer.toString(Warnings);
                    String messageToSendWarning2 = messageToSendWarning.replace("{warnings}", warningsStringFly);
                    String messageToSendWarning3 = messageToSendWarning2.replace("{user}", player.getName());
                    if(playerFlyWarnings.get(player.getName()) != Warnings) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (p.hasPermission("anticheat.warnings")) {
                                p.sendMessage(plugin.nombre + ChatColor.DARK_RED + messageToSendWarning3);
                            }
                        }
                    }
                    if (playerFlyWarnings.get(player.getName()) == Warnings) {
                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        Bukkit.dispatchCommand(console, command);
                        playerFlyWarnings.remove(player.getName());
                        String toSendMessage = config.getString("Config.fly.staff-message-punishment").replace("{user}", player.getName()).replace("{punishment}", punishment.replace("{user}","").replace("{reason}","").replace("{time}",""));
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (p.hasPermission("anticheat.warnings")) {
                                p.sendMessage(plugin.nombre + ChatColor.DARK_RED + toSendMessage);
                            }
                        }
                    }
                    checkPlayerPositions.remove(player.getName());
                    checkPlayerPositionsCoords.remove(player.getName());
                }
            }
        }
    }
}