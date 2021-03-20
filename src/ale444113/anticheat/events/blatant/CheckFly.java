package ale444113.anticheat.events.blatant;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import ale444113.anticheat.AntiCheat;
import net.md_5.bungee.api.ChatColor;

public class CheckFly implements Listener {

    public static HashMap<String, Integer> playerFlyWarnings = new HashMap<>();
    public static HashMap<String, Integer> checkPlayerPositions = new HashMap<>();
    public static HashMap<String, Location> checkPlayerPositionsCoords = new HashMap<>();

    public static boolean check(PlayerMoveEvent e, FileConfiguration config, AntiCheat plugin) {
        Player player = e.getPlayer();
        //Load data from config
        String pathBanCommand = "Config.fly.reason";
        String punishment = config.getString("Config.fly.punishment");
        int Warnings = config.getInt("Config.fly.warnings");

        String toExecuteCommand = punishment.replace("{user}",player.getName()).replace("{reason}",plugin.nombre + ChatColor.WHITE +config.getString(pathBanCommand)) + " " + ChatColor.YELLOW;
        if(!config.getString("Config.fly.punishment-time").equals("0")){toExecuteCommand = toExecuteCommand.replace("{time}", config.getString("Config.fly.punishment-time"));}
        if (player.isOnGround() || player.getAllowFlight() || player.getLocation().clone().subtract(0, 0.1, 0).getBlock().getType() == Material.LADDER || e.getTo().getBlock().isLiquid()) {
            checkPlayerPositions.put(player.getName(), 0);
            } else { checkPlayerPositions.put(player.getName(), checkPlayerPositions.get(player.getName()) + 1); }

            if (checkPlayerPositions.get(player.getName()) == 20) {
                checkPlayerPositionsCoords.put(player.getName(), player.getLocation());
            } else if (checkPlayerPositions.get(player.getName()) == 40) {
                if (player.getLocation().getY() >= checkPlayerPositionsCoords.get(player.getName()).getY()) {
                    playerFlyWarnings.put(player.getName(), playerFlyWarnings.get(player.getName()) + 1);


                    if(playerFlyWarnings.get(player.getName()) < Warnings) {
                        String warningsStringFly = Integer.toString(playerFlyWarnings.get(player.getName())) + "/" + Warnings;
                        String messageWarningStaff = config.getString("Config.fly.staff-message").replace("{warnings}", warningsStringFly).replace("{user}", player.getName());
                        System.out.println(messageWarningStaff);
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (p.hasPermission("anticheat.warnings")) {
                                p.sendMessage(plugin.nombre +  ChatColor.translateAlternateColorCodes('&', messageWarningStaff));
                            }
                        }
                    }
                    if (playerFlyWarnings.get(player.getName()) >= Warnings) {
                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        Bukkit.dispatchCommand(console, toExecuteCommand);
                        playerFlyWarnings.remove(player.getName());
                        String toSendMessage = config.getString("Config.fly.staff-message-punishment").replace("{user}", player.getName()).replace("{punishment}",config.getString("Config.speed.punishment").split(" ")[0]);
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (p.hasPermission("anticheat.warnings")) {
                                p.sendMessage(plugin.nombre +  ChatColor.translateAlternateColorCodes('&', toSendMessage));
                            }
                        }
                        return true;
                    }
                    checkPlayerPositions.remove(player.getName());
                    checkPlayerPositions.put(player.getName(),0);

                    checkPlayerPositionsCoords.remove(player.getName());
                }
            }
            return false;
    }
}