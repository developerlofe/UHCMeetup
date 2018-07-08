package com.isnakebuzz.meetup.h;

import com.isnakebuzz.meetup.a.Main;
import com.isnakebuzz.meetup.c.LobbyTask;
import com.isnakebuzz.meetup.d.GamePlayer;
import com.isnakebuzz.meetup.f.ScoreBoardAPI;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class EventJoinAndLeave implements Listener {

    private Main plugin;

    public EventJoinAndLeave(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) throws IOException {
        e.setJoinMessage(null);
        Player p = e.getPlayer();
        //Set Full
        p.setHealth(p.getMaxHealth());
        p.setFoodLevel(40);

        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Settings");
        if (config.getBoolean("Lobby.enabled")) {
            p.teleport(plugin.getLobbyManager().getLobby());
            p.setGameMode(GameMode.ADVENTURE);
        } else {
            p.setGameMode(GameMode.CREATIVE);
            p.teleport(plugin.getLobbyManager().getWorldLobby());
        }
        if (!config.getBoolean("MongoDB.enabled")) {
            GamePlayer gamePlayer = new GamePlayer(plugin, p, p.getUniqueId(), false, 0, 0, 0);
            plugin.getPlayerManager().getUuidGamePlayerMap().put(p.getUniqueId(), gamePlayer);
            plugin.getPlayerManager().spectator(gamePlayer, true);
        } else {
            new BukkitRunnable(){
                @Override
                public void run() {
                    try {
                        plugin.getPlayerDataManager().loadPlayer(p);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    GamePlayer gamePlayer = plugin.getPlayerManager().getUuidGamePlayerMap().get(p.getUniqueId());
                    plugin.getPlayerManager().spectator(gamePlayer, true);
                }
            }.runTaskAsynchronously(plugin);
        }
        if (plugin.getArenaManager().checkStart()) {
            new LobbyTask(plugin, config.getInt("GameOptions.VoteTime")).runTaskTimer(plugin, 0l, 20l);
        }
        Configuration lang = plugin.getConfigUtils().getConfig(plugin, "Lang");
        p.sendMessage(c(lang.getString("VoteMessage")));

        plugin.getScoreBoardAPI().setScoreBoard(p, ScoreBoardAPI.ScoreboardType.LOBBY, false, false);
    }

    @EventHandler
    public void PlayerLeftEvent(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        plugin.getScoreBoardAPI().removeScoreBoard(e.getPlayer());
        if (plugin.getPlayerManager().getPlayersAlive().contains(e.getPlayer())) {
            plugin.getPlayerManager().getPlayersAlive().remove(e.getPlayer());
        }
        plugin.getPlayerDataManager().savePlayer(e.getPlayer());
    }

    @EventHandler
    public void PlayerLeftEvent(PlayerKickEvent e) {
        plugin.getScoreBoardAPI().removeScoreBoard(e.getPlayer());
        if (plugin.getPlayerManager().getPlayersAlive().contains(e.getPlayer())) {
            plugin.getPlayerManager().getPlayersAlive().remove(e.getPlayer());
        }
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
