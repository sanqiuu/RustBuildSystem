package com.sanqiu.rustbuildsystem.model;
import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import com.sanqiu.rustbuildsystem.RustBuildSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class CodeDoorHolder implements InventoryHolder {
    Block block;
    public CodeDoorHolder(Block block){
        this.block  = block;
    }
    @Override
    public Inventory getInventory() {
        return null;
    }
}

public class CodeDoor {
    private static String TAG = "CodeDoor";
    private static String TAG_UUID = "CodeDoor_UUID";
    private static List<Integer> codeList ;
    private static List<Integer> bufferList ;
    private static List<UUID> uuidList ;
    private  Block block;
    private  Player player;
    private static ItemStack toItem(Material type,String name,int num)
    {
        ItemStack itemStack = new ItemStack(type,num);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public CodeDoor(Player player,Block block){
        this.player = player;
        this.block =block;
        if(block.getBlockData() instanceof Bisected ){
            Bisected bisected = (Bisected) block.getBlockData();
            if(bisected.getHalf() == Bisected.Half.TOP){
                this.block = block.getRelative(BlockFace.DOWN);
            }
        }
        RustBuildSystem plugin = RustBuildSystem.getPlugin();
        NamespacedKey key = new NamespacedKey(plugin,TAG);
        NamespacedKey key_uuid = new NamespacedKey(plugin,TAG_UUID);
        PersistentDataContainer data = new CustomBlockData(this.block,plugin);
        PersistentDataContainer player_data = player.getPersistentDataContainer();
        codeList = data.get(key,DataType.asList(DataType.INTEGER));
        uuidList = data.get(key_uuid,DataType.asList(DataType.UUID));
        bufferList = player_data.get(key,DataType.asList(DataType.INTEGER));

    }
    public CodeDoor(Player player,Inventory inventory){
        CodeDoorHolder holder = (CodeDoorHolder) inventory.getHolder();
        assert holder != null;
        Block block = holder.block;



        this.player = player;
        this.block = block;
        if(block.getBlockData() instanceof Bisected ){
            Bisected bisected = (Bisected) block.getBlockData();
            if(bisected.getHalf() == Bisected.Half.TOP){
                this.block = block.getRelative(BlockFace.DOWN);
            }
        }
        RustBuildSystem plugin = RustBuildSystem.getPlugin();
        NamespacedKey key = new NamespacedKey(plugin,TAG);
        NamespacedKey key_uuid = new NamespacedKey(plugin,TAG_UUID);
        PersistentDataContainer data = new CustomBlockData(this.block,plugin);
        PersistentDataContainer player_data = player.getPersistentDataContainer();
        codeList = data.get(key,DataType.asList(DataType.INTEGER));
        uuidList = data.get(key_uuid,DataType.asList(DataType.UUID));
        bufferList = player_data.get(key,DataType.asList(DataType.INTEGER));

    }

    public static boolean isCodeLock(Block block)
    {
        String string = block.getType().toString();
        return string.contains("DOOR") ||string.contains("FENCE_GATE");
    }
    public static boolean isCodeLock(Inventory inventory)
    {
        return  inventory.getHolder() instanceof CodeDoorHolder;
    }


    private   void OpenCodeDoorGUI()
    {
        Inventory inventory = Bukkit.createInventory(new CodeDoorHolder(block),27,"密码锁");
        for(int index=0 ; index<27 ; index++)
        {
            ItemStack item;
            switch (index)
            {
                case 3:
                    item = toItem(Material.GREEN_STAINED_GLASS_PANE,"1",1);
                    break;
                case 4:
                    item = toItem(Material.GREEN_STAINED_GLASS_PANE,"2",2);
                    break;
                case 5:
                    item = toItem(Material.GREEN_STAINED_GLASS_PANE,"3",3);
                    break;
                case 10:
                    item = toItem(Material.PAPER,"重置",1);
                    break;
                case 12:
                    item = toItem(Material.GREEN_STAINED_GLASS_PANE,"4",4);
                    break;
                case 13:
                    item = toItem(Material.GREEN_STAINED_GLASS_PANE,"5",5);
                    break;
                case 14:
                    item = toItem(Material.GREEN_STAINED_GLASS_PANE,"6",6);
                    break;
                case 21:
                    item = toItem(Material.GREEN_STAINED_GLASS_PANE,"7",7);
                    break;
                case 22:
                    item = toItem(Material.GREEN_STAINED_GLASS_PANE,"8",8);
                    break;
                case 23:
                    item = toItem(Material.GREEN_STAINED_GLASS_PANE,"9",9);
                    break;
                default:
                    item = toItem(Material.GRAY_STAINED_GLASS_PANE," ",1);
                    break;
            }
            inventory.setItem(index,item);
        }
        player.openInventory(inventory);
    }
    private     void openDoor(){
        BlockData doorData = block.getBlockData();
        if(!(doorData instanceof  Openable)) return;
        boolean open = ((Openable) doorData).isOpen();
        if(open){
            ((Openable) doorData).setOpen(false);
            block.setBlockData(doorData);
        }else {
            ((Openable) doorData).setOpen(true);
            block.setBlockData(doorData);
            new BukkitRunnable() {
                @Override
                public void run() {
                    BlockData doorData = block.getBlockData();
                    if(doorData instanceof  Openable){
                        ((Openable) doorData).setOpen(false);
                        block.setBlockData(doorData);
                    }
                }
            }.runTaskLater(RustBuildSystem.getPlugin(),5*20);
        }


    }
    public  void clickCodeDoor(){
        //uuidList !=null && uuidList.contains(player.getUniqueId())
        if(false){
            openDoor();
        }else {
            OpenCodeDoorGUI();
            RustBuildSystem plugin = RustBuildSystem.getPlugin();
            NamespacedKey key = new NamespacedKey(plugin,TAG);
            PersistentDataContainer data = player.getPersistentDataContainer();
            data.remove(key);
        }
    }
    public boolean hasPermissionToBreak(){
        return uuidList!=null && uuidList.get(0).equals(player.getUniqueId());
    }
    public  void operateCodeDoorInv(int slot){
        RustBuildSystem plugin = RustBuildSystem.getPlugin();
        NamespacedKey key = new NamespacedKey(plugin,TAG);
        NamespacedKey key_uuid = new NamespacedKey(plugin,TAG_UUID);
        PersistentDataContainer data = new CustomBlockData(block,plugin);
        PersistentDataContainer player_data = player.getPersistentDataContainer();
        if(slot == 10){
            //重置
            if(uuidList!=null){
                if(uuidList.get(0).equals(player.getUniqueId())){
                    player.sendMessage("密码已重置");
                    data.remove(key);
                    data.remove(key_uuid);
                    player_data.remove(key);
                    uuidList = null;
                    codeList = null;
                    bufferList = null;
                }else {
                    player.sendMessage("你不是密码锁的放置者");
                }
            }

        }else if(slot>=3 && slot<=5 || slot>=12 && slot<=14 ||slot>=21 && slot<=23){
            //输入密码
            int index =0;
            switch (slot) {
                case 3: index = 1;break;
                case 4: index = 2;break;
                case 5: index = 3;break;
                case 12: index = 4;break;
                case 13: index = 5;break;
                case 14: index = 6;break;
                case 21: index = 7;break;
                case 22: index = 8;break;
                case 23: index = 9;break;
            };
            if(uuidList == null){
                if(codeList == null){
                    codeList = new ArrayList<>();
                }
                codeList.add(index);
                data.set(key,DataType.asList(DataType.INTEGER),codeList);
                if(codeList.size()>=4){
                    uuidList = new ArrayList<>();
                    uuidList.add(player.getUniqueId());
                    data.set(key_uuid,DataType.asList(DataType.UUID),uuidList);
                    player.sendMessage("密码已设置"+codeList.toString());
                    player.closeInventory();
                }
            }else {
                if(bufferList == null){
                    bufferList = new ArrayList<>();
                }
                bufferList.add(index);
                player_data.set(key,DataType.asList(DataType.INTEGER),bufferList);
                if(bufferList.size()>=4){
                    //Bukkit.getLogger().info("b:"+bufferList.toString());
                    //Bukkit.getLogger().info("c"+codeList.toString());
                    if(bufferList.equals(codeList)){
                        player.sendMessage("密码正确");
                        openDoor();
                    }else {
                        player.sendMessage("密码错误");
                        player.damage(4);
                    }
                    player.closeInventory();
                }
            }

        }
    }


    public  void closeCodeDoorInv(){
        RustBuildSystem plugin = RustBuildSystem.getPlugin();
        NamespacedKey key = new NamespacedKey(plugin,TAG);
        PersistentDataContainer data = player.getPersistentDataContainer();
        data.remove(key);
    }

}
