package com.wiryaimd.opverify;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class OpVerify extends JavaPlugin implements Listener, CommandExecutor {

    private ArrayList<String> opplayer;
    private String verifymsg = ChatColor.YELLOW + "[OP VERIFY] " + ChatColor.GREEN + " /verify (pin) ";
    private String verifymsg2 = ChatColor.YELLOW + "[OP VERIFY] " + ChatColor.LIGHT_PURPLE + "DM yang megang console kalo belum punya pin";
    private String verifymsg3 = ChatColor.YELLOW + "[OP VERIFY] " + ChatColor.LIGHT_PURPLE + "Yang OP wajib punya pin untuk keamanan";

    private FileConfiguration config;

    @Override
    public void onEnable() {
        config = getConfig();
        config.addDefault("Wiryaimd", "8304");
        config.options().copyDefaults(true);
        saveConfig();
        opplayer = new ArrayList<String>();

        getServer().getPluginManager().registerEvents(this, this);
        getCommand("opverify");
        getCommand("verify").setExecutor(this  );
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            if (opplayer.contains(((Player) sender).getPlayer().getName())){
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase(config.getString(((Player) sender).getPlayer().getName()))) {
                        for (int i = 0; i < opplayer.size(); i++) {
                            if (opplayer.get(i).equalsIgnoreCase(((Player) sender).getPlayer().getName())) {
                                opplayer.remove(i);
                                break;
                            }
                        }
                        ((Player) sender).getPlayer().sendMessage(ChatColor.YELLOW + "[OP VERIFY] " + ChatColor.GREEN + "Verifikasi berhasil");
                    } else {
                        ((Player) sender).getPlayer().sendMessage(ChatColor.YELLOW + "[OP VERIFY] " + ChatColor.RED + "Pin salah");

                    }
                }else{
                    ((Player) sender).getPlayer().sendMessage(ChatColor.YELLOW + "[OP VERIFY] " + ChatColor.RED + "/verify <pin>");
                }
                return true;
            }else {
                return false;
            }
        }else{
            return false;
        }
    }

    @EventHandler
    public void playerMove(PlayerMoveEvent event){
        if (opplayer.contains(event.getPlayer().getName())){
            event.setTo(event.getFrom());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if (event.getPlayer().isOp() || event.getPlayer().hasPermission("*")){
            opplayer.add(event.getPlayer().getName());
            event.getPlayer().sendMessage(verifymsg);
            event.getPlayer().sendMessage(verifymsg3);
            event.getPlayer().sendMessage(verifymsg2);
        }
    }

    @EventHandler
    public void playerChat(AsyncPlayerChatEvent event){
        if (opplayer.contains(event.getPlayer().getName())){
            event.setCancelled(true);
            event.getPlayer().sendMessage(verifymsg);
            event.getPlayer().sendMessage(verifymsg2);
        }
    }

    @EventHandler
    public void playerCmd(PlayerCommandPreprocessEvent event){
        if (opplayer.contains(event.getPlayer().getName())){
            System.out.println("[" + event.getPlayer().getName() + "] " + event.getMessage());
            String[] cmdargs = event.getMessage().split("\\s+");
            if (!cmdargs[0].equalsIgnoreCase("/verify")){
                event.setCancelled(true);
                event.getPlayer().sendMessage(verifymsg);
                event.getPlayer().sendMessage(verifymsg2);
            }
        }
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event){
        if (opplayer.contains(event.getPlayer().getName())){
            for (int i = 0; i < opplayer.size(); i++){
                if (opplayer.get(i).equalsIgnoreCase(event.getPlayer().getName())){
                    opplayer.remove(i);
                    break;
                }
            }
        }
    }

}
