package cz.dartcz.rescmdsblacklist.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.event.ResidenceChangedEvent;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;


import cz.dartcz.rescmdsblacklist.ResCmdsBlacklist;

public class PlayerListener implements Listener {

	private ResCmdsBlacklist plugin;
	
	public PlayerListener(ResCmdsBlacklist plugin) {
        this.plugin = plugin;
    }
	
	@EventHandler
	public void ResidenceChangedEvent(ResidenceChangedEvent event) {
		ClaimedResidence res = event.getTo();
		
		if(res != null){
			List<String> executeCommands = ResCmdsBlacklist.config().getStringList(res.getName() + ".execute-commands");
			for (int i = 0; i < executeCommands.size(); i++) {
				String executeCommand = executeCommands.get(i);
				if (executeCommands.isEmpty()) continue;
		            Bukkit.dispatchCommand(event.getPlayer(), executeCommand);
			} 
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void OnPlayerCommand(PlayerCommandPreprocessEvent event){
		Location loc = event.getPlayer().getLocation();
		ClaimedResidence res = Residence.getResidenceManager().getByLoc(loc);
		
		if(res != null){
			String message = event.getMessage();
			List<String> disabledCommands = ResCmdsBlacklist.config().getStringList(res.getName() + ".blacklist-commands");
			for (int i = 0; i < disabledCommands.size(); i++) {
				String disabledCommand = disabledCommands.get(i);
				if (disabledCommand.isEmpty()) continue;
				if (message.toLowerCase().startsWith(disabledCommand.toLowerCase())) {
		            event.setCancelled(true);
		            event.getPlayer().sendMessage(ChatColor.RED + "Tento prikaz je v teto oblasti zakazan!");
				}
			} 
		}
	}
	
}
