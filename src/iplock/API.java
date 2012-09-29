package iplock;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class API {
	public ArrayList<String> loggedInList() {
		return iplock.isloggedin;
	}
    public boolean isLoggedIn(Player player) {
    	return iplock.isloggedin.contains(player.getName().toLowerCase());
    }
    public boolean hasPassword(Player player) {
    	return iplock.isloggedin.contains(player.getName().toLowerCase());
    }
    public void logIn(Player player) {
    	iplock.isloggedin.add(player.getName().toLowerCase());
	}
	public void addSafeIp(String ip, String name) {
		ip = ip.replaceAll("[/]","");
		ip = ip.replaceAll("[.]", "-");
		if(!iplock.plugin.config.contains(name + "." + ip)){
			iplock.plugin.config.set(name + "." + ip, "safe ip");
			iplock.plugin.saveYaml();
		}
	}
	
}
