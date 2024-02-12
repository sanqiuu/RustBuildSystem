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
import java.util.List;
import java.util.UUID;
class FactionBlockInvHolder implements InventoryHolder {
    public Location location;
    public FactionBlockInvHolder(Location location) {
        this.location = location;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
public class FactionBlock {
    private static  int size = 54;
    private static String TAG = "FactionBlock";
    private Location location;
    public FactionBlock(Location location){
        this.location = location;
    }
    public FactionBlock(Inventory inventory){
        FactionBlockInvHolder holder =  (FactionBlockInvHolder)inventory.getHolder();
        this.location = holder.location;
    }
    private  ItemStack showPlayerInfo(OfflinePlayer offlinePlayer){
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
        boolean hasPermission = hasPermission(offlinePlayer.getUniqueId());
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
    private   void changePermission(UUID uuid){
        Block block = location.getBlock();
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
    public   boolean hasPermission(UUID uuid){
        Block block = location.getBlock();
        RustBuildSystem plugin = RustBuildSystem.getPlugin();
        NamespacedKey key = new NamespacedKey(plugin,TAG);
        PersistentDataContainer data = new CustomBlockData(block,plugin);
        List<UUID> list = data.get(key, DataType.asList(DataType.UUID));
        //Bukkit.getLogger().info(String.valueOf(list!=null && list.contains(uuid)));
        //Bukkit.getLogger().info(uuid.toString());
        //Bukkit.getLogger().info(location.toString());
        return list!=null && list.contains(uuid);
    }

    public   void OperateFactionBlockGUI(InventoryClickEvent event)
    {
        Inventory inventory = event.getInventory();
        int slot = event.getRawSlot();
        if(slot<0||slot>=inventory.getSize()) return;

        ItemStack item = inventory.getItem(slot);
        if(item == null) return;
        UUID uuid = UUID.fromString(item.getItemMeta().getLore().get(0));
        changePermission(uuid);
        ItemStack new_item = showPlayerInfo(Bukkit.getOfflinePlayer(uuid));
        item.setItemMeta(new_item.getItemMeta());
    }
    public   void OpenFactionBlockGUI(Player player)
    {
        Block block = location.getBlock();
        Inventory inventory = Bukkit.createInventory(new FactionBlockInvHolder(block.getLocation()),size,"领地柜");
        OfflinePlayer[] offs = Bukkit.getOfflinePlayers();
        for(int index = 0;index<size;index++){
            if(index<offs.length){
                ItemStack itemStack = showPlayerInfo(offs[index]);
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
        return  inventory.getHolder() instanceof FactionBlockInvHolder;
    }


}
