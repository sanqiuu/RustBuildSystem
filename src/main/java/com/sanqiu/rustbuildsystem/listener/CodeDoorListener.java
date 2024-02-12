package com.sanqiu.rustbuildsystem.listener;

import com.sanqiu.rustbuildsystem.model.CodeDoor;
import com.sanqiu.rustbuildsystem.model.RustPlayer;
import org.bukkit.event.Listener;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;


public class CodeDoorListener implements Listener {
    @EventHandler
    public void onOpenDoor(PlayerInteractEvent event)
    {
        Block block =event.getClickedBlock();
        if(block== null) return;

        Player player = event.getPlayer();
        Action action = event.getAction();

        if(event.getHand()== EquipmentSlot.HAND)
        {
            if(action == Action.RIGHT_CLICK_BLOCK)
            {

                if(CodeDoor.isCodeLock(block)){
                    CodeDoor codeDoor = new CodeDoor(player,block);
                    codeDoor.clickCodeDoor();
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onCodeDoorSet(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        if(!CodeDoor.isCodeLock(inv)) return;
        Player player  = (Player)event.getWhoClicked();
        CodeDoor codeDoor = new CodeDoor(player,inv);
        codeDoor.operateCodeDoorInv(event.getRawSlot());
        event.setCancelled(true);
    }
    @EventHandler
    public void onCodeDoorClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        if(!CodeDoor.isCodeLock(inv)) return;
        Player player = (Player)event.getPlayer();
        CodeDoor codeDoor = new CodeDoor(player,inv);
        codeDoor.closeCodeDoorInv();
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if(!CodeDoor.isCodeLock(block)) return;
        CodeDoor codeDoor = new CodeDoor(player,block);
        if(!codeDoor.hasPermissionToBreak()){
            event.setCancelled(true);
            player.sendMessage("你不是密码锁的放置者");
        }

    }
}
