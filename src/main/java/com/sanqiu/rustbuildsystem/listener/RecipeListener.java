package com.sanqiu.rustbuildsystem.listener;

import com.sanqiu.rustbuildsystem.model.Recipe;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class RecipeListener  implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evt) {
        Player player=evt.getPlayer();
        Recipe.RegisterRustRecipeBook(player);
    }
}
