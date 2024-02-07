package com.sanqiu.rustbuildsystem.model;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.sanqiu.rustbuildsystem.RustBuildSystem;
import com.sanqiu.rustbuildsystem.data.RustBlock;
import com.sanqiu.rustbuildsystem.data.RustBlockData;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import com.jeff_media.customblockdata.CustomBlockData;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RustPlayer {
    private static BlockData passBlockData = Material.GREEN_STAINED_GLASS.createBlockData();
    private static BlockData forbidBlockData = Material.RED_STAINED_GLASS.createBlockData();
    private static String TAG = "RustPlayer";
    Player player;
    public RustPlayer(Player player){
        this.player = player;
    }
    public boolean isDrawing(){
        return player.hasMetadata(TAG);
    }
    public void drawClear(){
        if(player.hasMetadata(TAG)){
            player.removeMetadata(TAG,RustBuildSystem.getPlugin());
        }
    }
    private void sendRustBlockChange(List<RustBlockData> BlockList,BlockData data){
        for (RustBlockData rustBlockData: BlockList){
            player.sendBlockChange(rustBlockData.location,data);
        }
    }
    private void sendRustBlockChange(List<RustBlockData> BlockList){
        for (RustBlockData rustBlockData: BlockList){
            Location location = rustBlockData.location;
            player.sendBlockChange(rustBlockData.location,location.getBlock().getBlockData());
        }
    }
    public  boolean checkBlockisBuilding(Block block){
        RustBuildSystem plugin = RustBuildSystem.getPlugin();
        NamespacedKey key = new NamespacedKey(plugin,TAG);
        PersistentDataContainer data = new CustomBlockData(block,plugin);
        return data.has(key,DataType.asList(DataType.LOCATION));
    }
    public  boolean checkBlockisBuilding(){
        Block block = getTargetBlock();
        return checkBlockisBuilding(block);
    }
    public void upgradeBuilding(Block block){
        RustBuildSystem plugin = RustBuildSystem.getPlugin();
        NamespacedKey key = new NamespacedKey(plugin,TAG);
        PersistentDataContainer data = new CustomBlockData(block,plugin);
        List<Location> list = data.get(key,DataType.asList(DataType.LOCATION));
        assert list != null;
        for(Location location: list){
            Block b = location.getBlock();
            Material material = b.getType();
            Material changeMaterial = changeBlockType(material,true);
            if(changeMaterial!=null && changeMaterial!=material){
                location.getBlock().setType(changeMaterial);
            }
        }
    }
    private Material changeBlockType(Material material,boolean isUpgrade){
        Material changedMaterial  = material;
        switch (material){
            case OAK_PLANKS:
                if(isUpgrade) changedMaterial = Material.CRACKED_STONE_BRICKS;
                else changedMaterial = Material.AIR;
                break;
            case CRACKED_STONE_BRICKS:
                if(isUpgrade) changedMaterial = Material.STONE_BRICKS;
                else changedMaterial = Material.OAK_PLANKS;
                break;
            case STONE_BRICKS:
                if(isUpgrade) changedMaterial = Material.CHISELED_STONE_BRICKS;
                else changedMaterial = Material.CRACKED_STONE_BRICKS;
                break;
            case CHISELED_STONE_BRICKS:
                if(!isUpgrade) changedMaterial = Material.STONE_BRICKS;
                break;
            default:
                changedMaterial = null;
                break;
        }
        return changedMaterial;
    }
    public void breakBuilding(Block block){
        RustBuildSystem plugin = RustBuildSystem.getPlugin();
        NamespacedKey key = new NamespacedKey(plugin,TAG);
        PersistentDataContainer data = new CustomBlockData(block,plugin);
        List<Location> list = data.get(key,DataType.asList(DataType.LOCATION));
        assert list != null;
        for(Location location: list){
            location.getBlock().setType(Material.AIR);
            data.remove(key);
        }
    }
    private void build(List<RustBlockData> BlockList){
        RustBuildSystem plugin = RustBuildSystem.getPlugin();
        NamespacedKey key = new NamespacedKey(plugin,TAG);
        List<Location> save_list = new ArrayList<>();

        for(RustBlockData rustBlockData :BlockList){
            save_list.add(rustBlockData.location);
        }
        for (RustBlockData rustBlockData: BlockList){
            Block block = rustBlockData.location.getBlock();
            PersistentDataContainer data = new CustomBlockData(block,plugin);
            if(!data.has(key,DataType.asList(DataType.LOCATION))){
                data.set(key, DataType.asList(DataType.LOCATION),save_list);
            }
            block.setType(rustBlockData.material);
        }
    }
    private Block getTargetBlock(){
        int distance;
        if(player.getGameMode() == GameMode.CREATIVE){
            distance = 5;
        }else {
            distance = 4;
        }
        List<Block>  targrt_list = player.getLastTwoTargetBlocks((Set<Material>)null,distance);
        Block target_block = null;
        for(Block block:targrt_list){
            if(block.getType().isSolid()) continue;
            target_block = block;
            break;
        }
        assert target_block != null;
        return target_block;
    }

    public   void StartDraw(RustBlock rustBlock){
        player.sendMessage("开始绘制");
        player.setMetadata(TAG,new FixedMetadataValue(RustBuildSystem.getPlugin(),0));
        int drawTicks = 5;
        new BukkitRunnable(){
            List<RustBlockData> previousBlocksList =  null;
            @Override
            public void run() {

                if(previousBlocksList != null) {
                    sendRustBlockChange(previousBlocksList);
                }

                Block target_block = getTargetBlock();
                previousBlocksList = rustBlock.build(target_block.getLocation(),player.getFacing());
                boolean canBuild = rustBlock.canBuild(target_block.getLocation(),player.getFacing());
                if(canBuild){
                    sendRustBlockChange(previousBlocksList,passBlockData);
                }else{
                    sendRustBlockChange(previousBlocksList,forbidBlockData);
                }

                boolean hasTAG = player.hasMetadata(TAG);
                if(hasTAG && player.getMetadata(TAG).get(0).asInt() == 1){
                    if(canBuild) {
                        player.sendMessage("停止绘制");
                        build(previousBlocksList);
                        drawClear();
                        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(RustBuildSystem.getPlugin(), () -> {
                            sendRustBlockChange(previousBlocksList);
                        }, 1);
                        cancel();
                    }else {
                        player.setMetadata(TAG,new FixedMetadataValue(RustBuildSystem.getPlugin(),0));
                    }
                }else if(!hasTAG){
                    player.sendMessage("停止绘制");
                    drawClear();
                    Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(RustBuildSystem.getPlugin(), () -> {
                        sendRustBlockChange(previousBlocksList);
                    }, 1);
                    cancel();
                }

            }
        }.runTaskTimer(RustBuildSystem.getPlugin(),0,drawTicks);

    }
    public void StartBuild(){
        player.sendMessage("开始构建");
        player.setMetadata(TAG,new FixedMetadataValue(RustBuildSystem.getPlugin(),1));
    }
}
