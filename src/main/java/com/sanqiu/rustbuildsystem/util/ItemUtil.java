package com.sanqiu.rustbuildsystem.util;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemUtil {
    public static boolean isLog(Material material){
        boolean result = false;
        switch (material){
            case OAK_LOG:
            case SPRUCE_LOG:
            case JUNGLE_LOG:
            case ACACIA_LOG:
            case DARK_OAK_LOG:
            case BIRCH_LOG:
                result = true;
                break;

        }
        return  result;
    }
    private  static  boolean isSimilar(ItemStack item, ItemStack compareItem){
        Material material = item.getType();
        Material cmp_materiao = compareItem.getType();
        if(isLog(material)){
            return isLog(cmp_materiao);
        }
        String display_name = item.getItemMeta().getDisplayName();
        if(!display_name.isEmpty()){
            String cmp_displayname = compareItem.getItemMeta().getDisplayName();
            if(!cmp_displayname.isEmpty()){
                return cmp_displayname.equals(display_name);
            }
            return false;

        }else{
            return item.isSimilar(compareItem);
        }
    }
    public static int getPlayerItemAmount(Player player , ItemStack targetItem)
    {
        int count = 0;
        PlayerInventory inv = player.getInventory();
        for (ItemStack item : inv.getContents()) {
            if (item != null && isSimilar(targetItem,item)) {
                count = count + item.getAmount();
            }
        }
        return count;

    }

    public static boolean isPlayerItemEnough(Player player , ItemStack targetItem,int num)
    {
        int count = 0;
        PlayerInventory inv = player.getInventory();
        for (ItemStack item : inv.getContents()) {
            if (item != null && isSimilar(targetItem,item)) {
                count = count + item.getAmount();
            }
        }
        return count>=num;

    }


    public static void subPlayerItemAmount(Player player , ItemStack targetItem,int num)
    {
        int itemsToRemove = num;
        for(ItemStack item : player.getInventory().getContents())
        {
            if(item != null && isSimilar(targetItem,item))
            {
                int preAmount = item.getAmount();
                int newAmount = Math.max(0, preAmount - itemsToRemove);
                itemsToRemove = Math.max(0, itemsToRemove - preAmount);
                item.setAmount(newAmount);
                if(itemsToRemove == 0)
                {
                    break;
                }
            }
        }

    }
    public static ItemStack stringToItem(String string){
        try {
            YamlConfiguration config = new YamlConfiguration();
            config.loadFromString(string);
            return config.getItemStack("Item");
        }
        catch(InvalidConfigurationException e){
            throw new IllegalArgumentException("String is not an item: " + string);
        }
    }
    public static String itemToString(ItemStack item){
        YamlConfiguration config = new YamlConfiguration();
        config.set("Item", item);
        return config.saveToString();
    }

}