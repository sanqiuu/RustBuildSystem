package com.sanqiu.rustbuildsystem.listener;

import com.sanqiu.rustbuildsystem.model.BuildGUI;
import com.sanqiu.rustbuildsystem.model.RustPlayer;
import com.sanqiu.rustbuildsystem.model.ToolGUI;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
        if(!BuildGUI.isBuildingMap(item)) return;
        Player player =  event.getPlayer();
        Action action = event.getAction();
        RustPlayer rustPlayer = new RustPlayer(player);
        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
        {
            Block block = event.getClickedBlock();
            if(block!= null && rustPlayer.checkBlockisBuilding(block)){
                ToolGUI.OpenToolInventory(player,block);
            }else {
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

}
