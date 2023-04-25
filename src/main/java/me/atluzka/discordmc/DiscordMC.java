package me.atluzka.discordmc;


import net.md_5.bungee.api.chat.TranslatableComponent;
import okhttp3.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.json.*;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.IOException;

public final class DiscordMC extends JavaPlugin implements Listener {

    public void sendWebhook(String text, int color) {
        try {
            // Replace WEBHOOK_URL with the actual webhook URL
            String webhookUrl = "WEBHOOK_URL";

            OkHttpClient httpClient = new OkHttpClient();

            JSONObject embed = new JSONObject();
            embed.put("title", text);
            embed.put("color", color);

            JSONObject jsonPayload = new JSONObject();
            jsonPayload.put("embeds", new JSONArray().put(embed));

            RequestBody body = RequestBody.create(jsonPayload.toString(), MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(webhookUrl)
                    .post(body)
                    .build();

            Response response = httpClient.newCall(request).execute();
            // Close the response body
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        // System.out.println("My first plugin has started");
        getServer().getPluginManager().registerEvents(this,this);

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        String player = event.getPlayer().getName();

        if (!event.getPlayer().hasPlayedBefore()) {
            sendWebhook(player + " joined the server for the first time", 16756290);
            return;
        }
        sendWebhook(player + " joined the server", 65280);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        String player = event.getPlayer().getName();
        sendWebhook(player + " left the server", 16711680);
    }

    @EventHandler
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        Advancement advancement = event.getAdvancement();
        if(!event.getAdvancement().getKey().getKey().contains("recipes")) {
            String player = event.getPlayer().getName();
            sendWebhook(player + " has made the advancement " + advancement.getDisplay().getTitle(), 16756290);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String deathmsg = event.getDeathMessage();
        sendWebhook(deathmsg, 0);
    }
}
