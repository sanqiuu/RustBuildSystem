package com.sanqiu.rustbuildsystem.model;

import com.sanqiu.rustbuildsystem.RustBuildSystem;
import com.sanqiu.rustbuildsystem.data.*;
import com.sanqiu.rustbuildsystem.util.ItemUtil;
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

import java.util.ArrayList;
import java.util.List;

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
    private static ItemStack  setItem(Material material,String name){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        List<String> list = new ArrayList<>();
        list.add("木头X5");
        itemMeta.setLore(list);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static void OpenBuildInventory(Player player)
    {
        Inventory inventory = Bukkit.createInventory(new BuildGUIHolder(),45,"建造图纸");
        //       4
        //  11        15
        //  20        24
        //  29        33
        //    39 40 41
        ItemStack itemStack1 = setItem(Material.OAK_STAIRS,"地基楼梯");
        ItemStack itemStack2 = setItem(Material.OAK_PLANKS,"墙");
        ItemStack itemStack3 = setItem(Material.SCAFFOLDING,"地基");
        ItemStack itemStack4 = setItem(Material.OAK_PRESSURE_PLATE,"地板");
        ItemStack itemStack5 = setItem(Material.OAK_STAIRS,"旋转楼梯");
        ItemStack itemStack6 = setItem(Material.GLASS_PANE,"窗户");
        ItemStack itemStack7= setItem(Material.OAK_DOOR,"门");
        ItemStack itemStack8 = setItem(Material.OAK_SLAB,"半墙");
        ItemStack itemStack9 = setItem(Material.OAK_STAIRS,"斜坡");
        ItemStack itemStack10 = setItem(Material.OAK_FENCE,"围栏");
        inventory.setItem(4,itemStack1);
        inventory.setItem(11,itemStack2);
        inventory.setItem(15,itemStack3);
        inventory.setItem(20,itemStack4);
        inventory.setItem(24,itemStack5);
        inventory.setItem(29,itemStack6);
        inventory.setItem(33,itemStack7);
        inventory.setItem(40,itemStack8);
        inventory.setItem(39,itemStack9);
        inventory.setItem(41,itemStack10);
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
        Inventory inventory = event.getInventory();
        if(index>=inventory.getSize()) return;
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
            case 39:
                //斜坡

                SafeStartDraw(player,new Slope());
                break;
            case 41:
                //围栏
                SafeStartDraw(player,new Fence());
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
