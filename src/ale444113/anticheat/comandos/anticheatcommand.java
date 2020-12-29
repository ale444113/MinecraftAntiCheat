package ale444113.anticheat.comandos;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ale444113.anticheat.AntiCheat;

public class anticheatcommand implements CommandExecutor{

    private AntiCheat plugin;
    public anticheatcommand(AntiCheat plugin) {
        this.plugin = plugin;
    }
    public boolean onCommand(CommandSender sender, Command comando, String label, String[] args) {
        if(args.length < 1) {
            sender.sendMessage(plugin.nombre+ChatColor.translateAlternateColorCodes('&', "&1 Creo que quiere decir /antichear reload ...."));
        }else if(args[1] == "reload") {
            if(sender.hasPermission("anticheat.reload")){
                plugin.reloadConfig();
                sender.sendMessage(plugin.nombre+ChatColor.translateAlternateColorCodes('&',"&a Plugin recargado con exito"));
            }else {
                sender.sendMessage(plugin.nombre+ChatColor.translateAlternateColorCodes('&',"&4 No tienes permisos para recargar este plugin"));
            }
        }else {
            sender.sendMessage(plugin.nombre+ChatColor.translateAlternateColorCodes('&', "&1 No entiendo que significa ")+ChatColor.RED+args[0]+ChatColor.translateAlternateColorCodes('&', " ..."));
        }
        return false;
    }

}
