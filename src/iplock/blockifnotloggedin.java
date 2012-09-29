package iplock;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
//@SuppressWarnings("deprecation")
//^^^ I updated to AsyncPlayerChatEvent
public class blockifnotloggedin implements Listener {
	public static iplock plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	

	public blockifnotloggedin(iplock instance) {
	plugin = instance;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		if(!iplock.isloggedin.contains(event.getPlayer().getName().toLowerCase())){
			event.getPlayer().teleport(event.getPlayer().getLocation());
			event.setCancelled(true);
		}		
	}
	
	@EventHandler 
	public void OnPlayerChat(AsyncPlayerChatEvent event){
		if(!iplock.isloggedin.contains(event.getPlayer().getName().toLowerCase())){
			event.getPlayer().sendMessage(ChatColor.RED + plugin.translate.getString("block-if-not-logged-in"));
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void OnBlockBreak(BlockBreakEvent event){
	
		if(!iplock.isloggedin.contains(event.getPlayer().getName().toLowerCase())){
			event.getPlayer().sendMessage(ChatColor.RED + plugin.translate.getString("block-if-not-logged-in"));
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void OnBlockPlace(BlockPlaceEvent event){
		if(!iplock.isloggedin.contains(event.getPlayer().getName().toLowerCase())){
			event.getPlayer().sendMessage(ChatColor.RED + plugin.translate.getString("block-if-not-logged-in"));
			event.setCancelled(true);			
		}
		
	}
	@EventHandler
	public void OnPlayerDropItem(PlayerDropItemEvent event){
		if(!iplock.isloggedin.contains(event.getPlayer().getName().toLowerCase())){
			event.getPlayer().sendMessage(ChatColor.RED + plugin.translate.getString("block-if-not-logged-in"));
			event.setCancelled(true);			
		}
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
			
		if(!iplock.isloggedin.contains(event.getPlayer().getName().toLowerCase())){
			event.getPlayer().sendMessage(ChatColor.RED + plugin.translate.getString("block-if-not-logged-in"));
			event.setCancelled(true);			
		}
		
		
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
		
		String cmd = event.getMessage().replaceAll("/", "");
		
		cmd = cmd.split(" ")[0];
		if(!cmd.equalsIgnoreCase("login")){
			if(cmd.equalsIgnoreCase("setpassword")){
				return;
			}
			if(cmd.equalsIgnoreCase("register")){
				return;
			}
			if(!iplock.isloggedin.contains(event.getPlayer().getName().toLowerCase())){
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + plugin.translate.getString("block-if-not-logged-in"));
			}
		}
	};
	//@EventHandler(priority = EventPriority.LOWEST) 
	//public void OnPlayerCommandPreprocessLow(PlayerCommandPreprocessEvent event){OnPlayerCommandPreprocess(event);}
}


