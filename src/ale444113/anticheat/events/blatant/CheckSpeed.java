package ale444113.anticheat.events.blatant;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import ale444113.anticheat.AntiCheat;
import net.md_5.bungee.api.ChatColor;

public class CheckSpeed {

    public static HashMap<String, Integer> playerSpeedWarnings = new HashMap<>();
    public static HashMap<Player, Boolean> playerContactSlime = new HashMap<>();


    public static boolean check(PlayerMoveEvent e, FileConfiguration config, AntiCheat plugin) {
            Player player = e.getPlayer();
            double distance = e.getFrom().distance(e.getTo());
            //Load data from config

            int Warnings = config.getInt("Config.speed.warnings");

            String pathSpeedVelocity = "Config.speed.velocity";
            double maxvelocity = config.getDouble(pathSpeedVelocity);

            Material materialdown = player.getLocation().clone().subtract(0, 1, 0).getBlock().getType();

            if(materialdown.equals(Material.SLIME_BLOCK)){playerContactSlime.put(player,true);}
            if (materialdown.equals(Material.AIR)) {
                if (distance > maxvelocity && !player.getAllowFlight() && !player.isInsideVehicle() && e.getFrom().getY() <= e.getTo().getY() && !playerContactSlime.get(player) && !player.isDead()) {
                    playerSpeedWarnings.put(player.getName(), playerSpeedWarnings.get(player.getName()) + 1);

                    if(playerSpeedWarnings.get(player.getName()) != Warnings) {
                        String warningsStringSpeed = Integer.toString(playerSpeedWarnings.get(player.getName())) + "/" + Integer.toString(Warnings);
                        String messageWarningStaff = ChatColor.translateAlternateColorCodes('&',config.getString("Config.speed.staff-message").replace("{warnings}", warningsStringSpeed).replace("{user}", player.getName()));

                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (p.hasPermission("anticheat.warnings")) {
                                p.sendMessage(plugin.nombre + messageWarningStaff);
                            }
                        }
                    }

                    if (playerSpeedWarnings.get(player.getName()) >= Warnings) {
                        String pathSpeedCommand = "Config.speed.reason";
                        String punishment = config.getString("Config.speed.punishment");

                        String toExecuteCommand = config.getString("Config.speed.punishment").replace("{user}",player.getName()).replace("{reason}",plugin.nombre + ChatColor.RED +config.getString(pathSpeedCommand)) + " " + ChatColor.YELLOW;
                        if(!config.getString("Config.speed.punishment-time").equals("0")){toExecuteCommand = toExecuteCommand.replace("{time}", config.getString("Config.speed.punishment-time"));}

                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        Bukkit.dispatchCommand(console, toExecuteCommand);
                        playerSpeedWarnings.remove(player.getName());
                        String toSendMessage = ChatColor.translateAlternateColorCodes('&',config.getString("Config.speed.staff-message-punishment").replace("{user}", player.getName()).replace("{punishment}",punishment.split(" ")[0]));
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (p.hasPermission("anticheat.warnings")) {
                                p.sendMessage(plugin.nombre  + toSendMessage);
                            }
                        }
                        return true;
                    }
                }
            }
            return false;
    }
}