package com.sanqiu.rustbuildsystem.model;

import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import com.sanqiu.rustbuildsystem.RustBuildSystem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

class FactionBlockHolder implements InventoryHolder {

    Block block;
    public FactionBlockHolder(Block block){
        this.block  = block;
    }
    @Override
    public Inventory getInventory() {
        return null;
    }
}

public class FactionBlock {
    private static  int size = 54;
    private static String TAG = "FactionBlock";
    private static ItemStack showPlayerInfo(OfflinePlayer offlinePlayer,Block block){
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(offlinePlayer.getName());
        List<String> lores = itemMeta.getLore();
        if(lores == null){
            lores = new ArrayList<>();
        }
        lores.add(offlinePlayer.getUniqueId().toString());
        if(offlinePlayer.isOnline()){
            lores.add(ChatColor.GREEN+"online");
        }else {
            lores.add(ChatColor.RED+"offline");
        }
        boolean hasPermission = hasPermission(block,offlinePlayer.getUniqueId());
        if(hasPermission){
            lores.add(ChatColor.GREEN+"有权限");
        }else {
            lores.add(ChatColor.RED+"无权限");
        }
        lores.add("点击移除建造权限");
        itemMeta.setLore(lores);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    private  static void changePermission(Block block,UUID uuid){
        RustBuildSystem plugin = RustBuildSystem.getPlugin();
        NamespacedKey key = new NamespacedKey(plugin,TAG);
        PersistentDataContainer data = new CustomBlockData(block,plugin);
        List<UUID> list = data.get(key, DataType.asList(DataType.UUID));
        if(list!=null){
            if(list.contains(uuid)){
                list.remove(uuid);
            }else {
                list.add(uuid);
            }
        }else {
            list = new ArrayList<>();
            list.add(uuid);
        }
        data.set(key,DataType.asList(DataType.UUID),list);
    }
    private  static boolean hasPermission(Block block,UUID uuid){
        RustBuildSystem plugin = RustBuildSystem.getPlugin();
        NamespacedKey key = new NamespacedKey(plugin,TAG);
        PersistentDataContainer data = new CustomBlockData(block,plugin);
        List<UUID> list = data.get(key, DataType.asList(DataType.UUID));
        return list!=null && list.contains(uuid);
    }

    public  static void OperateFactionBlockGUI(InventoryClickEvent event)
    {
        Inventory inventory = event.getInventory();
        int slot = event.getRawSlot();
        if(slot<=0||slot>=inventory.getSize()) return;
        ItemStack item = inventory.getItem(slot);
        if(item == null) return;
        UUID uuid = UUID.fromString(item.getItemMeta().getLore().get(0));
        FactionBlockHolder holder = (FactionBlockHolder)inventory.getHolder();
        changePermission(holder.block,uuid);
        ItemStack new_item = showPlayerInfo(Bukkit.getOfflinePlayer(uuid), holder.block);
        item.setItemMeta(new_item.getItemMeta());
    }
    public  static void OpenFactionBlockGUI(Player player,Block block)
    {
        Inventory inventory = Bukkit.createInventory(new FactionBlockHolder(block),size,"领地柜");
        OfflinePlayer[] offs = Bukkit.getOfflinePlayers();
        for(int index = 0;index<size;index++){
            if(index<offs.length){
                ItemStack itemStack = showPlayerInfo(offs[index],block);
                inventory.addItem(itemStack);
            }
        }
        player.openInventory(inventory);
    }
    public static boolean isFactionBlock(Block block)
    {
        return block.getType() == Material.BEACON;
    }
    public static boolean isFactionBlock(Inventory inventory)
    {
        return  inventory.getHolder() instanceof FactionBlockHolder;
    }
    public static void placeFactionBlock(Player player ,Block block){
        RustBuildSystem plugin = RustBuildSystem.getPlugin();
        NamespacedKey key = new NamespacedKey(plugin,TAG);
        PersistentDataContainer data = player.getPersistentDataContainer();
        List<Location> list = data.get(key, DataType.asList(DataType.LOCATION));
        if(list == null){
            list = new ArrayList<>();
        }
        list.add(block.getLocation());
        data.set(key,DataType.asList(DataType.LOCATION),list);

    }
    public static void breakFactionBlock(Block block){

    }
    public static boolean hasPermission(Player player ){
        UUID uuid = player.getUniqueId();
        return true;
    }
}
