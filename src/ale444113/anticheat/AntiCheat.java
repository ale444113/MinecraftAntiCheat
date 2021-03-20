package ale444113.anticheat;

import java.io.File;

import ale444113.anticheat.commands.AntiCheatCommand;
import ale444113.anticheat.events.*;
import ale444113.anticheat.events.playerEvents.PlayerJoin;
import ale444113.anticheat.events.playerEvents.PlayerLeave;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiCheat extends JavaPlugin{
    public String rutaConfig;
    PluginDescriptionFile pdffile = getDescription();
    public String version = pdffile.getVersion();
    public String nombre = ChatColor.GREEN+"["+pdffile.getName()+"]";


    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"---------------------------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage(nombre+ChatColor.BLUE+"Tu gran "+ChatColor.RED+"AntiCheat"+ChatColor.BLUE+" ha sido activado correctamente (Version: "+version+"), cheaters temed...");
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+"Gracias por usarlo n.n ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"---------------------------------------------------------------------");
        registrarComandos();
        registrarEvent();
        registerConfig();

        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerJoin.createPlayerWarnings(p);
        }

    }
    public void onDisable(){
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"---------------------------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage(nombre+ChatColor.DARK_BLUE+"El plugin "+ChatColor.RED+"AntiCheat"+ChatColor.DARK_BLUE+" a sido desactivado correctamente");
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+"Te esperamos de nuevo pronto ^^");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"---------------------------------------------------------------------");
    }

    public void registrarComandos() {
        this.getCommand("anticheat").setExecutor(new AntiCheatCommand(this));
    }
    public void registrarEvent() {
        PluginManager pm = 	getServer().getPluginManager();

        pm.registerEvents(new PlayerJoin(this), this);
        pm.registerEvents(new PlayerLeave(this), this);

        pm.registerEvents(new moveEvent(this), this);
    }
    public void registerConfig() {
        File config = new File(this.getDataFolder(),"config.yml");
        rutaConfig = config.getPath();
        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            saveDefaultConfig();

        }
    }

}