package com.isnakebuzz.meetup.EventsManager.Events;

import com.isnakebuzz.meetup.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class EventLogin implements Listener {

    private Main plugin;

    public EventLogin(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void LoginEvent(PlayerLoginEvent e){
        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Settings");
        if (Main.getStates.state == Main.getStates.LOADING){
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cLoading Game");
        } else if (Main.getStates.state != Main.getStates.LOBBY){
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cIn Game");
        }

        if (Bukkit.getOnlinePlayers().size() >= config.getInt("GameOptions.MaxPlayers")){
            e.disallow(PlayerLoginEvent.Result.KICK_FULL, "§cFull");
        }
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}