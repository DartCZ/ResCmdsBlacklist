package cz.dartcz.rescmdsblacklist;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import cz.dartcz.rescmdsblacklist.listeners.PlayerListener;


public class ResCmdsBlacklist extends JavaPlugin {
	
	private static Plugin _plugin;
	private static JavaPlugin _javaPlugin;
	
	@Override
	public void onEnable() {
		_plugin = this;
		_javaPlugin = this;
		
		PluginManager pm = getServer().getPluginManager();
		if(pm.getPlugin("Residence") != null) {
			pm.registerEvents(new PlayerListener(this), this);
		} else {
			consoleMessage(ChatColor.RED + "Plugin Residence not found. Disabling.");
			pm.disablePlugin(_plugin);
			return;
		}
		
	    saveDefaultConfig();
	} 
	
	@Override
	public void onDisable() {
		_plugin = null;
		_javaPlugin = null;
		unReg(new PlayerListener(this));
		unRegPlugin(this);
	}
	
	public static void consoleMessage(String message){
	    ConsoleCommandSender console = Bukkit.getConsoleSender();
	    console.sendMessage(ChatColor.GREEN + "[" + _plugin.getName() + "] " + message);
	}
	
	public static void unReg(Listener... listeners) {
		for (Listener l : listeners) {
			HandlerList.unregisterAll(l);
		}
	}

	public static void unRegPlugin(Plugin p) {
		HandlerList.unregisterAll(p);
	}
	
	public static FileConfiguration config() {
		return _plugin.getConfig();
	}
	
	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if(sender.hasPermission("rescmdsblacklist.admin")) {
			if(args[0].equals("reload")) {
				this.reloadConfig();
				sender.sendMessage(ChatColor.GREEN + "[" + _plugin.getName() + "]" + " Reloaded config.");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Na tento prikaz nemas opravneni.");
		}
		return false;
	}
}
