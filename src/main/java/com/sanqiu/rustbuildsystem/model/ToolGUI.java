package com.sanqiu.rustbuildsystem.model;

import com.sanqiu.rustbuildsystem.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

class ToolGUIHolder implements InventoryHolder {
    public Block block;
    public ToolGUIHolder(Block block){
        this.block = block;
    }
    @Override
    public Inventory getInventory() {
        return null;
    }
}
public class ToolGUI {
    private static String Title = "建造变更";
    private  static int info_slot = 3;
    private  static int upgrade_slot = 4;
    private  static int break_slot = 5;
    private static boolean isMaterialEnough(Player player){
        boolean result = true;
        ItemStack woods = new ItemStack(Material.COBBLESTONE);
        if(ItemUtil.isPlayerItemEnough(player,woods,5)){
            ItemUtil.subPlayerItemAmount(player,woods,5);

        }else {
            player.sendMessage("材料不足");
            result =false;
        }
        return result;
    }
    public static void OperateToolInventory(InventoryClickEvent event)
    {
        event.setCancelled(true);
        int index = event.getRawSlot();
        if(index == upgrade_slot || index == break_slot){
            Player player = (Player) event.getWhoClicked();
            RustPlayer rustPlayer = new RustPlayer(player);
            ToolGUIHolder holder = (ToolGUIHolder)event.getInventory().getHolder();
            assert holder != null;
            Block block = holder.block;
            if(index == upgrade_slot){
                if(isMaterialEnough(player)){
                    rustPlayer.upgradeBuilding(block);
                    event.getWhoClicked().closeInventory();
                }
            }else {
                if(FactionBlockManager.INSTANCE.hasPermission(player,block.getLocation())){
                    rustPlayer.breakBuilding(block);
                    event.getWhoClicked().closeInventory();
                }else {
                    player.sendMessage("无权限");
                }
            }

        }
    }
    public static boolean isToolInventory(Inventory inventory ){
        return  inventory.getHolder() instanceof ToolGUIHolder;
    }
    public static void OpenToolInventory(Player player,Block block){
        Inventory inventory = Bukkit.createInventory(new ToolGUIHolder(block),9,Title);
        for(int i =0;i< inventory.getSize();i++){
            ItemStack itemStack;
            ItemMeta itemMeta;
            if( i ==upgrade_slot ){
                itemStack = new ItemStack(Material.COBBLESTONE);
                itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("升级");
                List<String> list = new ArrayList<>();
                list.add("石头X5");
                itemMeta.setLore(list);
                itemStack.setItemMeta(itemMeta);
                itemStack.setItemMeta(itemMeta);
            }else if(i==break_slot){
                itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("拆毁");
                itemStack.setItemMeta(itemMeta);
            }else if(i==info_slot){
                itemStack = new ItemStack(Material.PAPER);
                itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("信息");
                List<String> list = new ArrayList<>();
                if(FactionBlockManager.INSTANCE.isProtect(block.getLocation())){
                    list.add(ChatColor.GREEN+"建筑处于保护中");
                }else {
                    list.add(ChatColor.RED+"建筑未被保护");
                }
                itemMeta.setLore(list);
                itemStack.setItemMeta(itemMeta);
            }else {
                itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            }
            inventory.setItem(i,itemStack);
        }

        player.openInventory(inventory);
    }
}
