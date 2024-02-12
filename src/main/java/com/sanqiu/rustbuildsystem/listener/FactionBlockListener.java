package com.sanqiu.rustbuildsystem.listener;

import com.sanqiu.rustbuildsystem.model.FactionBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;

public class FactionBlockListener implements Listener {
    @EventHandler
    public void onFactionBlockClick(PlayerInteractEvent event)
    {
        Block block =event.getClickedBlock();
        if(block== null) return;

        Player player = event.getPlayer();
        Action action = event.getAction();

        if(event.getHand()== EquipmentSlot.HAND)
        {
            if(action == Action.RIGHT_CLICK_BLOCK)
            {
                if(FactionBlock.isFactionBlock(block)){
                    event.setCancelled(true);
                    FactionBlock.OpenFactionBlockGUI(player,block);
                }

            }
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        if(FactionBlock.isFactionBlock(inv)){
            FactionBlock.OperateFactionBlockGUI(event);
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onFactionBlockPlace(BlockPlaceEvent event)
    {
        Block block = event.getBlock();
        if(FactionBlock.isFactionBlock(block)){

        }
    }
    @EventHandler
    public void onFactionBlockBreak(BlockBreakEvent event)
    {
        Block block = event.getBlock();
        if(FactionBlock.isFactionBlock(block)){

        }
    }
}
