package com.gmail.pthomas1997.loginMessagePlugin;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
/**
 * LoginMessagePlus
 * @author Parker
 * @version 3.0
 * Contact pthomas1997@gmail.com with questions, bugs, or comments
 * www.planetminecraft.com/member/castlecorp/
 */
public final class LoginMessagePlugin extends JavaPlugin implements Listener {

	// Declares instance...Don't touch.
	public static LoginMessagePlugin instance;

	// Gets instance
	public static LoginMessagePlugin getInstance(){
		return instance;
	}

	/**
	 * onEnable()
	 * What will happen when the plugin is enabled.
	 * @Override
	 */
	@Override
	public void onEnable() {
		instance = this;

		loadConfiguration();

		// Commands
		getCommand("welcome").setExecutor(new WelcomeCommmand());
		getCommand("ignite").setExecutor(new IgniteCommand());
		getCommand("cookie").setExecutor(new CookieCommand());
		getCommand("rq").setExecutor(new QuitCommand());
		getCommand("warn").setExecutor(new WarnCommand(this));
		getCommand("dump").setExecutor(new DumpCommand(this));
		getCommand("staffchat").setExecutor(new StaffChatCommand());

		// Events
		Bukkit.getServer().getPluginManager().registerEvents(new OnPlayerLoginEvent(), this);
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getPluginManager().registerEvents(new FirstJoinEvent(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new OnPlayerChatEvent(), this);

		/**
		 * getVersion() check
		 * This is really something that is very harmless. It was a special instance in which this needed to be included.
		 * I DO NOT suggest touching it...
		 */
		if(getVersion().equals("Public"))
		{
			/**
			 * About the whole Airplane thing...It's just a joke. I thought it was funny, and I was bored. 
			 * I like to hide stuff like this.
			 */
			getLogger().info("Flight attendants prepare for crosscheck.");
			getLogger().info("Crosscheck complete. Prepare for takeoff!");
		}
		else
		{

			if (getLicense().equals("1"))
			{
				Bukkit.getServer().getLogger().info("Crosscheck complete. Prepare for takeoff!");
			}
			else
			{
				//This is why you should not touch it...
				Bukkit.getServer().getLogger().info("Please Change The License Number in The Config File.");
				Bukkit.getServer().broadcastMessage("[LoginMessage] The License Number is Incorrect in The Config File. LoginMessage is being disabled. (Bad landing)");	
				//See that nice little "disablePlugin(this)" line? Yeah, that's why you don't touch this section
				Bukkit.getPluginManager().disablePlugin(this);
				return;
			}
		}
	}

	/**
	 * onDisable()
	 * What will happen when the plugin is disabled.
	 * It just logs those messages. Again, I was bored.
	 */
	@Override
	public void onDisable() {
		Bukkit.getServer().getLogger().info("Prepare for landing.");
		Bukkit.getServer().getLogger().info("We have landed. You are now free to move about the cabin!");
	}

// FirstJoinEvent was here. Now moved to it's own class: FirstJoinEvent

/**
 * logToFile(String message)
 * Logs the string "warning" from /warn command to the file /Plugins/LoginMessage/warningData.txt
 * FileWriter
 * @param message
 */
	public void logToFile(String message)

	{

		try
		{
			File LoginMessage = getDataFolder();
			if(!LoginMessage.exists())
			{
				LoginMessage.mkdir();
				getLogger().info("Making Folder LoginMessage");
			}

			File saveTo = new File(getDataFolder().getPath() + "/warningData.txt");
			if (!saveTo.exists())
			{
				saveTo.createNewFile();
				getLogger().info("Making File warningData.txt");
			}


			FileWriter fw = new FileWriter(saveTo, true);

			PrintWriter pw = new PrintWriter(fw);

			pw.println(message);

			pw.flush();

			pw.close();

		} catch (IOException e)
		{

			e.printStackTrace();

		}

	}

	/**
	 * loadConfiguration()
	 * The settings/paths and value pairs for the config.yml file.
	 * Probably shouldn't mess with this unless you know what you are doing...
	 */
	public void loadConfiguration() {

		FileConfiguration config = getConfig();
		//Paths and Value pairs
		config.addDefault("LoginMessage", "Version 3.0"); 
		config.addDefault("Join Message", "%name% has joined the game.");
		config.addDefault("#2", "Lower food and health levels on /plugins. true or false.");
		config.addDefault("Lower Health", false);
		config.addDefault("Lower Food", false);
		config.addDefault("Kick Player", false);
		config.addDefault("Kill Player", false);
		config.addDefault("Kick Player", false);
		config.addDefault("#3", "#Change the /welcome message below.");
		config.addDefault("Welcome Message","Change this message in the config file.");
		config.addDefault("#4", "Use group chat colors?");
		config.addDefault("Group Colors", true);
		config.addDefault("First Join Message", "Welcome, %name%, to the server!");
		config.addDefault("First Join Broadcast", "%name% has joined the server for the first time!");
		config.addDefault("Welcome Back Message", "Welcome back to the server, %name%");
		config.addDefault("#5","#Do Not Change Anything Below This Line! (Don't do it!)");
		config.addDefault("License","0");
		config.addDefault("Version","Public");

		config.options().copyDefaults(true);

		saveConfig();
	}

//Pssst...Read the /**  */ below!
	/**
	 * This works for all of the following "String get...()"s!
	 * getGroupColors()
	 * Check the return value of "Group Colors" in config.yml
	 * Corresponds directly to the path name in config.yml and returns that path's value
	 * @return
	 */
	public static String getGroupColors()
	{
		return getInstance().getConfig().getString("Group Colors");
	}

	public static String getKillPlayer()
	{
		return getInstance().getConfig().getString("Kill Player");
	}

	public static String getKickPlayer()
	{
		return getInstance().getConfig().getString("Kick Player");
	}

	public static String getLowerFood()
	{
		return getInstance().getConfig().getString("Lower Food");
	}

	public static String getLowerHealth()
	{
		return getInstance().getConfig().getString("Lower Health");
	}
	
	public static String getWelcomeBackMessage()
	{
		return getInstance().getConfig().getString("Welcome Back Message");			
	}

	public static String getFirstJoinBroadcast()
	{
		return getInstance().getConfig().getString("First Join Broadcast");
	}

	public static String getJoinMessage()
	{
		return getInstance().getConfig().getString("Join Message");
	}

	public static String getFirstJoinMessage()
	{
		return getInstance().getConfig().getString("First Join Message");
	}

	// Don't you dare change this... (It has to do with the version piece on top in onEnable(). Yeah, that one we discussed not touching...)
	public static String getVersion()
	{
		return getInstance().getConfig().getString("Version");
	}

	// Ok, back to stuff you can change! (W00T)
	public static String getWelcomeMessage()
	{
		return getInstance().getConfig().getString("Welcome Message");
	} 

	public static String getLicense()
	{
		return getInstance().getConfig().getString("License");
	}

	
/**
 * onPluginCommand(PlayerCommandPreprocessEvent e)
 * PluginBlocker implementation.
 * This checks if the player has permission to use /plugins when it is run.
 * @param e
 */
	@EventHandler
	public void onPluginCommand(PlayerCommandPreprocessEvent e){

		if(e.getMessage().equalsIgnoreCase("/plugins") || e.getMessage().equalsIgnoreCase("/pl")){

			if(e.getPlayer() instanceof Player){

				Player p = (Player)e.getPlayer();

				if(!p.hasPermission("loginmessage.plugins")){
					e.setCancelled(true);
					p.sendMessage(ChatColor.RED+"This command is blocked!");
					getLogger().info(p.getDisplayName()+" tried to use the /plugins command.");

					/**
					 * Below are a whole bunch of if, else statements that check if config values are true.
					 * They are punishments for /plugins
					 * So if "Lower Food" is true in the config, it will take off 9 food points when a player does /plugins
					 * And so on. You get the point.
					 */
					if(getLowerFood().equalsIgnoreCase("true")){
						p.setFoodLevel(p.getFoodLevel()-9);
					}
					else{
						return;
					}

					if(getLowerHealth().equalsIgnoreCase("true")){
						p.setHealth(p.getHealth()-6);
					}
					else{
						return;
					}

					if(getKickPlayer().equalsIgnoreCase("true")){
						p.kickPlayer(ChatColor.RED+"You have been kicked for trying to access the plugins list.");
					}

					if(getKillPlayer().equalsIgnoreCase("true")){
						getServer().dispatchCommand(getServer().getConsoleSender(), "kill "+p.getName());
					}

					for(Player pl: Bukkit.getOnlinePlayers()){
						if(pl.isOp() || pl.hasPermission("loginmessage.plugins.message"));
						pl.sendMessage(ChatColor.RED+p.getDisplayName()+" has attempted to execute the /plugins command!");
					}

				}
			}
		}
	}
}

