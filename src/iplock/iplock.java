package iplock;

import java.awt.List;
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

public class iplock extends JavaPlugin  {
	public static iplock plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	public final ArrayList<String> hasnotplayed = new ArrayList<String>();
	public final static ArrayList<String> isloggedin = new ArrayList<String>();
	public IpLockCommandExecutor myExecutor;
	
	public File bypassFile;
	public File configFile;
	public FileConfiguration bypass;
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
		getCommand("register").setExecutor(myExecutor);
		getCommand("lockip").setExecutor(myExecutor);
		getCommand("reset").setExecutor(myExecutor);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new Joinlistener(this), this);
		pm.registerEvents(new blockifnotloggedin(this), this);
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled.");

		
		
	}
	private void handleFiles() {
		bypass = new YamlConfiguration();
		bypassFile = new File(getDataFolder(), "/bypass.yml");
		if(!bypassFile.exists()){                     
            bypassFile.getParentFile().mkdirs();
            try {
            	OutputStream out = new FileOutputStream(bypassFile);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            List iplist = new List();
            iplist.add("127.0.0.1");
            bypass.set("bypasses", iplist);
        }
		/*
		try {
		config.load(bypassFile);
		} catch (Exception e) {
            e.printStackTrace();
        }
        */
		
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
			}
		addYamlElement(translate,"kickmessage", "You were kicked from the server because you entered the wrong password!");
		addYamlElement(translate,"not-yet-registered", "You are not yet registered on this server. Please do so using /setpassword <password>");
		addYamlElement(translate,"ask-for-login", "Please log in using /login <password>");
		addYamlElement(translate,"reloadmessage", "The server has been reloaded, please log in again using /login <password>");
		addYamlElement(translate,"successfull-login", "You successfully logged in"); 
		addYamlElement(translate,"safeip-message", "You successfully logged in. If you log in from this location again no password will be required.");
		addYamlElement(translate,"/login-message","If you regard this network as safe, please use /safeip <password> to mark this location as safe.");
		addYamlElement(translate,"password-set", "Your password has been encrypted and saved! You can use /login <password> to log in");
		addYamlElement(translate,"block-if-not-logged-in", "You must be loggin in to do this. Use /login <password> to do so");
		addYamlElement(translate,"already-locked", "You account has already been locked");
		addYamlElement(translate,"reloadmessage2", "If your ip is locked do /login without a password");
		addYamlElement(translate,"successfull", "Successfull");
		addYamlElement(translate,"fail", "Error, please try again");
		addYamlElement(translate,"account-reset-message", "Your account has been reset, please register again");
		addYamlElement(translate,"registered", "Your password was set to: ");//register message
		addYamlElement(translate,"nopassyet", "You do not have a password yet! Please type /register <password>");//the player tried to login but doesn't have a password
		
		saveYaml();
		try {
			translate.load(translateFile);
		} catch (Exception e) {
		}
		
	}
	public void addYamlElement(FileConfiguration YAMLConfig,String name,Object value) { //sets an Yaml element if it doesnt exist
		if(!YAMLConfig.isSet(name)){
			YAMLConfig.set(name, value);
		}
	}
	public void removeYamlElement(FileConfiguration YAMLConfig,String name) {
		if(!YAMLConfig.isSet(name)){
			YAMLConfig.set(name, null);
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