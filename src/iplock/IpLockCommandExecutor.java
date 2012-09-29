package iplock;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IpLockCommandExecutor implements CommandExecutor {
	public static iplock plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	
	public IpLockCommandExecutor(iplock instance) {
	plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg){
		ArrayList<String> isloggedin = iplock.isloggedin;
		if(cmd.getName().equalsIgnoreCase("reset")){// /reset player/password (player)
			if(arg.length > 0){
				if(arg.length == 1){
					if(sender.hasPermission("iplock.reset.password")){
							String player = arg[0].toLowerCase();
							if(plugin.config.contains(player + ".pass")){
								plugin.config.set(player, null);
								plugin.saveYaml();
								if(playerIsOnline(player)){
									Player p = getOnlinePlayer(player);
									p.kickPlayer(ChatColor.RED + plugin.translate.getString("account-reset-message"));
								}
								sender.sendMessage(ChatColor.GREEN + plugin.translate.getString("successfull"));
								return true;
							}
							else{
								sender.sendMessage(ChatColor.GREEN + plugin.translate.getString("fail"));
							}
						}
				}
			}
			return false;
		}
		if(cmd.getName().equalsIgnoreCase("safeip")){
			if(sender instanceof Player){
				if(arg.length == 1){
					String password = arg[0].toString();
					String passencrypted = md5(password);//encrypt password;
				
					String pass = getPass(sender.getName().toLowerCase());
					
					if(passencrypted.equals(pass)){
						saveIp(((Player) sender).getAddress().toString().split(":")[0],sender.getName().toLowerCase());
						isloggedin.add(sender.getName().toLowerCase());
						sender.sendMessage(ChatColor.GREEN + plugin.translate.getString("safeip-message"));
						return true;
					}else{
					sender.sendMessage(ChatColor.RED + plugin.translate.getString("incorrect-password"));
					
					return true;
					}
				}
			}
			else{
				sender.sendMessage("This command can only be run as a player");
				return true;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("login")){
			if(sender instanceof Player){
				if(arg.length == 1){
					String password = arg[0].toString();
					String passencrypted = md5(password);//encrypt password;
				
					String pass = getPass(sender.getName().toLowerCase());
					if(pass == null){
						sender.sendMessage(ChatColor.RED + plugin.translate.getString("not-yet-registered"));
						return true;
					}
					if(pass.equalsIgnoreCase("lockedip")){
						isloggedin.add(sender.getName().toLowerCase());
						sender.sendMessage(ChatColor.GREEN + plugin.translate.getString("successfull-login"));
						return true;
					}
					if(passencrypted.equals(pass)){
						isloggedin.add(sender.getName().toLowerCase());
						sender.sendMessage(ChatColor.GREEN + plugin.translate.getString("successfull-login"));
						sender.sendMessage(ChatColor.GREEN + plugin.translate.getString("/login-message"));
						return true;
					
					}else{
					((Player) sender).kickPlayer(plugin.translate.getString("kickmessage"));
					return true;
					}
				}
				if(arg.length == 0){
					String pass = getPass(sender.getName().toLowerCase());
					if(pass.equals("lockedip")){
						isloggedin.add(sender.getName().toLowerCase());
						sender.sendMessage(ChatColor.GREEN + plugin.translate.getString("successfull-login"));
						return true;
					}
				}
				
			}
			else{
				sender.sendMessage("This command can only be run as a player");
				return true;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("setpassword")){
			if(sender instanceof Player){
				if(arg.length == 1){
					if(isloggedin.contains(sender.getName().toLowerCase())||!(plugin.config.contains(sender.getName().toLowerCase() + ".pass"))){
						plugin.config.set(sender.getName().toLowerCase(), "");
						plugin.config.set(sender.getName().toLowerCase() + ".pass",md5(arg[0]));
						plugin.saveYaml();
						sender.sendMessage(ChatColor.GREEN + plugin.translate.getString("password-set"));
						return true;
					}
					else if(!isloggedin.contains(sender.getName().toLowerCase())){
						sender.sendMessage(ChatColor.RED + plugin.translate.getString("block-if-not-logged-in"));
						return true;
					}
				}
			}
			else{
				sender.sendMessage("This command can only be run as a player");
			}
		}
		if(cmd.getName().equalsIgnoreCase("register")){//same as setpassword but logs you in
			if(sender instanceof Player){
				if(arg.length == 1){
					if(isloggedin.contains(sender.getName().toLowerCase())||!(plugin.config.contains(sender.getName().toLowerCase() + ".pass"))){
						plugin.config.set(sender.getName().toLowerCase(), "");
						plugin.config.set(sender.getName().toLowerCase() + ".pass",md5(arg[0]));
						plugin.saveYaml();
						sender.sendMessage(ChatColor.GREEN + plugin.translate.getString("registered") + arg[0]);
						isloggedin.add(sender.getName().toLowerCase());
						return true;
					}
					else if(!isloggedin.contains(sender.getName().toLowerCase())){
						sender.sendMessage(ChatColor.RED + plugin.translate.getString("block-if-not-logged-in"));
						return true;
					}
				}
			}
			else{
				sender.sendMessage("This command can only be run as a player");
			}
		}
		if(cmd.getName().equalsIgnoreCase("lockip")){
			if(sender instanceof Player){
				if(arg.length == 1){
					if(sender.hasPermission("iplock.lockown")){
						if(plugin.config.contains(sender.getName().toLowerCase() + ".locked")){
							sender.sendMessage(ChatColor.RED + plugin.translate.getString("already-locked"));
							return true;
						}
						String ip = ((Player) sender).getAddress().toString();
						ip = ip.replaceAll("[/]","");
						ip = ip.replaceAll("[.]", "-");
						ip = ip.split(":")[0];
							plugin.config.set(sender.getName().toLowerCase(), "");
							plugin.config.set(sender.getName().toLowerCase() + ".pass" , "lockedip");
							plugin.config.set(sender.getName().toLowerCase() + "." + "locked" , ip);
							
							
							plugin.saveYaml();
							sender.sendMessage(ChatColor.GREEN + "Ip successfully locked");
							return true;
					}
				}
			}
		}
		return false;
	}

	
	
	
	private Player getOnlinePlayer(String player) {
		Player[] online = Bukkit.getOnlinePlayers();
		for(int i=0; i < Bukkit.getOnlinePlayers().length; i++){
			if(online[i].getName().equalsIgnoreCase(player)){
				return online[i];
			}
		}
		return null;
		
	}

	public boolean playerIsOnline(String player){
		Player[] online = Bukkit.getOnlinePlayers();
		for(int i=0; i < Bukkit.getOnlinePlayers().length; i++){
			if(online[i].getName().equalsIgnoreCase(player)){
				return true;
			}
		}
		return false;
	}
	private void saveIp(String ip, String name) {
		ip = ip.replaceAll("[/]","");
		ip = ip.replaceAll("[.]", "-");
		if(!plugin.config.contains(name + "." + ip)){
			plugin.config.set(name + "." + ip, "safe ip");
			plugin.saveYaml();
		}
	}
	
	private String getPass(String name){
		if(!plugin.config.contains(name + ".pass")){
			return null;
		}
		return plugin.config.get(name + ".pass").toString();
	}

	private String md5(String password) {
		byte[] defaultBytes = password.getBytes();
		try{
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte messageDigest[] = algorithm.digest();
		            
			StringBuffer hexString = new StringBuffer();
			for (int i=0;i<messageDigest.length;i++) {
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}
			password=hexString.toString();
		}catch(NoSuchAlgorithmException nsae){
		            
		}
		return password;
	}
	
	

}