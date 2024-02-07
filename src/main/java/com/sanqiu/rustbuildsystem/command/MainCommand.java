package com.sanqiu.rustbuildsystem.command;

import com.sanqiu.rustbuildsystem.model.BuildGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<>();
        list.add("give");
        return list;
    }
    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args)
    {
        if (command.getName().equalsIgnoreCase("rustbuild"))
        {
            if (!(sender instanceof Player ))
            {
                sender.sendMessage("你必须是一名玩家!");
                return true;
            }
            Player player = (Player) sender;
            if(!player.hasPermission("rustbuild.use"))
            {
                return true;
            }
            if(args.length == 1 &&  args[0]!=null && args[0].equals("give"))
            {
                player.sendMessage("构建图纸获取成功");
                ItemStack item = BuildGUI.createBuildingMap();
                player.getInventory().addItem(item);
            }

            return true; // 返回true防止返回指令的usage信息
        }
        return false;
    }

}
