package com.sanqiu.rustbuildsystem.listener;

import com.sanqiu.rustbuildsystem.model.BuildGUI;
import com.sanqiu.rustbuildsystem.model.RustPlayer;
import com.sanqiu.rustbuildsystem.model.ToolGUI;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BuildingMapListener  implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        if(BuildGUI.isBuildingMap(inv))
        {
            BuildGUI.OperateBuildInventory(event);
        }else if(ToolGUI.isToolInventory(inv))
        {
            ToolGUI.OperateToolInventory(event);
        }
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getHand() == EquipmentSlot.OFF_HAND) return;
        ItemStack item = event.getItem();
        if(item == null) return;
        Player player =  event.getPlayer();
        Action action = event.getAction();
        RustPlayer rustPlayer = new RustPlayer(player);
        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
        {
            Block block = event.getClickedBlock();
            boolean isMap = BuildGUI.isBuildingMap(item);
            if(block!= null && rustPlayer.checkBlockisBuilding(block)){
                if(!isMap) {
                    if(item.getType().toString().toLowerCase().contains("pickaxe")){
                        event.setCancelled(true);
                    }
                    return;
                }
                ToolGUI.OpenToolInventory(player,block);
            }else {
                if(!isMap) {
                    return;
                }
                BuildGUI.OpenBuildInventory(player);
            }

        }
        else if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)
        {

            if(rustPlayer.isDrawing()){
                rustPlayer.StartBuild();
            }
        }
    }
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack itemPrevious = player.getInventory().getItem(event.getPreviousSlot());
        if(itemPrevious!= null && BuildGUI.isBuildingMap(itemPrevious)){
            new RustPlayer(player).drawClear();
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        new RustPlayer(player).drawClear();
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        RustPlayer rustPlayer = new RustPlayer(event.getPlayer());
        if(rustPlayer.checkBlockisBuilding(event.getBlock())){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onTNTExplode(EntityExplodeEvent event) {
        RustPlayer rustPlayer = new RustPlayer(null);
        boolean hasBuilding = false;
        List<Block> list = event.blockList();
        for(Block block:list){
            if(rustPlayer.checkBlockisBuilding(block)){
                event.setCancelled(true);
                hasBuilding = true;
                break;
            }
        }
        if(hasBuilding){
            rustPlayer.destoryBuilding(list);
        }

    }
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        RustPlayer rustPlayer = new RustPlayer(null);
        boolean hasBuilding = false;
        List<Block> list = event.blockList();
        for(Block block:list){
            if(rustPlayer.checkBlockisBuilding(block)){
                event.setCancelled(true);
                hasBuilding = true;
                break;
            }
        }
        if(hasBuilding){
            rustPlayer.destoryBuilding(list);
        }

    }
}
