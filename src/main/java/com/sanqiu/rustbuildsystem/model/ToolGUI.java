package com.sanqiu.rustbuildsystem.model;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
    private  static int upgrade_slot = 3;
    private  static int break_slot = 5;
    public static void OperateToolInventory(InventoryClickEvent event)
    {
        event.setCancelled(true);
        int index = event.getRawSlot();
        if(index == upgrade_slot || index == break_slot){
            RustPlayer player = new RustPlayer((Player) event.getWhoClicked());
            ToolGUIHolder holder = (ToolGUIHolder)event.getInventory().getHolder();
            assert holder != null;
            Block block = holder.block;
            if(index == upgrade_slot){
                player.upgradeBuilding(block);
            }else {
                player.breakBuilding(block);
            }
            event.getWhoClicked().closeInventory();
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
                itemStack.setItemMeta(itemMeta);
            }else if(i==break_slot){
                itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("拆毁");
                itemStack.setItemMeta(itemMeta);
            }else {
                itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            }
            inventory.setItem(i,itemStack);
        }

        player.openInventory(inventory);
    }
}
