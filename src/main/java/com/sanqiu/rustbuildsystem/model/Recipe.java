package com.sanqiu.rustbuildsystem.model;

import com.sanqiu.rustbuildsystem.RustBuildSystem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    public static  void RegisterRustRecipeBook(Player player){
        for(NamespacedKey key : list){
            player.discoverRecipe(key);
        }
        player.discoverRecipe(Material.LECTERN.getKey());
    }
    private static final List<NamespacedKey> list = new ArrayList<>();
    public  static  void RegisterRustRecipe(){
        Plugin plugin = RustBuildSystem.getPlugin();
        ItemStack item = BuildGUI.createBuildingMap();
        NamespacedKey key = new NamespacedKey(plugin, "BuildingMap");

        list.add(key);
        ShapedRecipe recipe = new ShapedRecipe(key,item);
        recipe.shape("AAA", "AAA", "AAA");
        recipe.setIngredient('A', Material.STICK);
        plugin.getServer().addRecipe(recipe);
    }
}
