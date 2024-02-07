package com.sanqiu.rustbuildsystem.model;

import com.sanqiu.rustbuildsystem.RustBuildSystem;
import com.sanqiu.rustbuildsystem.data.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

class BuildGUIHolder implements InventoryHolder{

    @Override
    public Inventory getInventory() {
        return null;
    }
}
public class BuildGUI {
    private static String MapName = "建筑构建图纸";
    public static ItemStack createBuildingMap(){
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(MapName);
        item.setItemMeta(itemMeta);
        return item;
    }
    public static boolean isBuildingMap(ItemStack item){
        boolean result = false;
        if(item == null) return result;
        if(item.getType() == Material.PAPER){
            String name = item.getItemMeta().getDisplayName();
            if(name!=null && name.equals(MapName)){
                result = true;
            }
        }
        return result;
    }
    public static boolean isBuildingMap(Inventory inventory){
       return  inventory.getHolder() instanceof BuildGUIHolder;
    }

    public static void OpenBuildInventory(Player player)
    {
        Inventory inventory = Bukkit.createInventory(new BuildGUIHolder(),45,"建造图纸");
        //       4
        //  11        15
        //  20        24
        //  29        33
        //       40
        ItemStack itemStack1 = new ItemStack(Material.OAK_STAIRS);
        ItemMeta itemMeta1 = itemStack1.getItemMeta();
        itemMeta1.setDisplayName("地基楼梯");
        itemStack1.setItemMeta(itemMeta1);
        ItemStack itemStack2 = new ItemStack(Material.BRICK_WALL);
        ItemMeta itemMeta2 = itemStack2.getItemMeta();
        itemMeta2.setDisplayName("墙");
        itemStack2.setItemMeta(itemMeta2);
        ItemStack itemStack3 = new ItemStack(Material.OAK_PLANKS);
        ItemMeta itemMeta3 = itemStack3.getItemMeta();
        itemMeta3.setDisplayName("地基");
        itemStack3.setItemMeta(itemMeta3);
        ItemStack itemStack4 = new ItemStack(Material.OAK_PRESSURE_PLATE);
        ItemMeta itemMeta4 = itemStack4.getItemMeta();
        itemMeta4.setDisplayName("地板");
        itemStack4.setItemMeta(itemMeta4);
        ItemStack itemStack5 = new ItemStack(Material.OAK_FENCE);
        ItemMeta itemMeta5 = itemStack5.getItemMeta();
        itemMeta5.setDisplayName("旋转楼梯");
        itemStack5.setItemMeta(itemMeta5);
        ItemStack itemStack6 = new ItemStack(Material.GLASS_PANE);
        ItemMeta itemMeta6 = itemStack6.getItemMeta();
        itemMeta6.setDisplayName("窗户");
        itemStack6.setItemMeta(itemMeta6);
        ItemStack itemStack7 = new ItemStack(Material.OAK_DOOR);
        ItemMeta itemMeta7 = itemStack7.getItemMeta();
        itemMeta7.setDisplayName("门");
        itemStack7.setItemMeta(itemMeta7);
        ItemStack itemStack8 = new ItemStack(Material.OAK_SLAB);
        ItemMeta itemMeta8 = itemStack7.getItemMeta();
        itemMeta8.setDisplayName("半墙");
        itemStack8.setItemMeta(itemMeta8);
        inventory.setItem(4,itemStack1);
        inventory.setItem(11,itemStack2);
        inventory.setItem(15,itemStack3);
        inventory.setItem(20,itemStack4);
        inventory.setItem(24,itemStack5);
        inventory.setItem(29,itemStack6);
        inventory.setItem(33,itemStack7);
        inventory.setItem(40,itemStack8);
        player.openInventory(inventory);
    }
    private static void SafeStartDraw(Player player,RustBlock rustBlock){
        new RustPlayer(player).drawClear();
        //地基楼梯
        new BukkitRunnable(){
            @Override
            public void run() {
                RustPlayer rustPlayer = new RustPlayer(player);
                if (!rustPlayer.isDrawing()){
                    rustPlayer.StartDraw(rustBlock);
                }
            }
        }.runTaskLaterAsynchronously(RustBuildSystem.getPlugin(), 5);
    }

    public static void OperateBuildInventory(InventoryClickEvent event)
    {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        int index = event.getRawSlot();
        player.sendMessage("点击了 index:"+index);

        boolean needClose = true;
        switch (index)
        {
            case 4:
                SafeStartDraw(player,new BaseStair());
                break;
            case 11:
                //墙
                SafeStartDraw(player,new Wall());
                break;
            case 15:

                SafeStartDraw(player,new Base());
                //地基
                break;
            case 20:
                //地板
                SafeStartDraw(player,new Floor());
                break;
            case 24:
                //楼梯

                SafeStartDraw(player,new Stair());
                break;

            case 29:
                //窗户

                SafeStartDraw(player,new Window());
                break;
            case 33:
                //门

                SafeStartDraw(player,new Door());
                break;
            case 40:
                //半墙

                SafeStartDraw(player,new HalfWall());
                break;

            default:
                needClose = false;
                break;
        }
        if(needClose){
            player.closeInventory();
        }
    }
}
