package iplock;

import java.net.InetSocketAddress;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

public class Joinlistener implements Listener {
	public static iplock plugin;
	public final Logger logger = Logger.getLogger("Minecraft");

	public Joinlistener(iplock instance) {
	plugin = instance;
	}

	
	
	
	@EventHandler
	public void OnPLayerQuit(PlayerQuitEvent event){
		if(iplock.isloggedin.contains(event.getPlayer().getName().toLowerCase().toLowerCase())){
			iplock.isloggedin.remove(event.getPlayer().getName().toLowerCase().toLowerCase());
		}
	}
	@EventHandler
	public void OnPLayerKick(PlayerKickEvent event){
		if(iplock.isloggedin.contains(event.getPlayer().getName().toLowerCase().toLowerCase())){
			iplock.isloggedin.remove(event.getPlayer().getName().toLowerCase().toLowerCase());
		}
	}
	@EventHandler
	public void OnPluginDisable(PluginDisableEvent event){
		Bukkit.broadcastMessage(ChatColor.RED+ plugin.translate.getString("reloadmessage"));
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.DARK_RED + plugin.translate.getString("reloadmessage2"));
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){		
		if(!plugin.config.contains(event.getPlayer().getName().toLowerCase().toLowerCase() + ".pass")){
			event.getPlayer().sendMessage(ChatColor.AQUA + plugin.translate.getString("not-yet-registered"));
			return;
		}
		
			InetSocketAddress ips = event.getPlayer().getAddress();
			String ip = ips.toString().split(":")[0];
			ip = ip.replaceAll("[/]","");
			ip = ip.replaceAll("[.]", "-");
			if(plugin.config.contains(event.getPlayer().getName().toLowerCase().toLowerCase() + "." + ip)){
				event.getPlayer().sendMessage(ChatColor.GREEN + plugin.translate.getString("successfull-login"));
				iplock.isloggedin.add(event.getPlayer().getName().toLowerCase());
				return;
			}
			if(plugin.config.contains(event.getPlayer().getName().toLowerCase()+".locked")){
				if(plugin.config.getString(event.getPlayer().getName().toLowerCase() + ".locked").equals(ip)){
					iplock.isloggedin.add(event.getPlayer().getName().toLowerCase());
					event.getPlayer().sendMessage(ChatColor.GREEN + plugin.translate.getString("successfull-login"));
					return;
				}
				else{
					event.getPlayer().kickPlayer(plugin.translate.getString("already-locked"));
				}
			}
			else{
				event.getPlayer().sendMessage(ChatColor.RED + plugin.translate.getString("ask-for-login"));
			}
	}
}
