package iplock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

//Hello, I'm a comment :D
public class iplock extends JavaPlugin  {
	public static iplock plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	public final ArrayList<String> hasnotplayed = new ArrayList<String>();
	public final ArrayList<String> isloggedin = new ArrayList<String>();
	public IpLockCommandExecutor myExecutor;
	
	public File configFile;
	public File translateFile;
	public FileConfiguration config;
	public FileConfiguration translate;
	

	
	@Override
	public void onEnable() {
		handleFiles();
		
		myExecutor = new IpLockCommandExecutor(this);
		
		getCommand("login").setExecutor(myExecutor);
		getCommand("safeip").setExecutor(myExecutor);
		getCommand("setpassword").setExecutor(myExecutor);
		getCommand("lockip").setExecutor(myExecutor);
		getCommand("reset").setExecutor(myExecutor);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new Joinlistener(this), this);
		pm.registerEvents(new blockifnotloggedin(this), this);
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled.");

		
		
	}
	private void handleFiles() {
		config = new YamlConfiguration();
		configFile = new File(getDataFolder(), "/ips.yml");
		if(!configFile.exists()){                     
            configFile.getParentFile().mkdirs();
            try {
            	OutputStream out = new FileOutputStream(configFile);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		try {
		config.load(configFile);
		} catch (Exception e) {
            e.printStackTrace();
        }
		translate = new YamlConfiguration();
		translateFile = new File(getDataFolder(), "/translate.yml");
		if(!translateFile.exists()){
			translateFile.getParentFile().mkdirs();
				try {OutputStream out = new FileOutputStream(translateFile);
	            	out.close();
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			translate.set("kickmessage", "You were kicked from the server, reason: that password is invalid");
			translate.set("not-yet-registered", "You are not yet registered on this server. Please do so using /setpassword <password>");
			translate.set("ask-for-login", "Please log in using /login <password>");
			translate.set("reloadmessage", "The server has been reloaded, please log in again using /login <password>");
			translate.set("successfull-login", "You successfully logged in"); 
			translate.set("safeip-message", "You successfully logged in. If you log in from this location again no password will be required.");
			translate.set("/login-message","If you regard this network as safe, please use /safeip <password> to mark this location as safe.");
			translate.set("password-set", "Your password has been encrypted and saved! You can use /login <password> to log in");
			translate.set("block-if-not-logged-in", "You must be loggin in to do this. Use /login <password> to do so");
			translate.set("already-locked", "You account has already been locked");
			translate.set("reloadmessage2", "If your ip is locked do /login without a password");
			translate.set("successfull", "Successfull");
			translate.set("fail", "Error, please try again");
			translate.set("account-reset-message", "Your account has been reset, please register again");
			saveYaml();
			
		}
		try {
			translate.load(translateFile);
		} catch (Exception e) {
		}
		
	}
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		/**Bukkit.broadcastMessage(ChatColor.RED+ plugin.translate.getString("reloadmessage"));
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.DARK_RED + translate.getString("reloadmessage2"));**/
		this.logger.info(pdfFile.getName() + " is now disabled ");
	}
	public void saveYaml() {
		try {
			config.save(configFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		try {
			translate.save(translateFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	

}