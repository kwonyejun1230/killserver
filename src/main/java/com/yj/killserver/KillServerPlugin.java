package com.yj.killserver;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class KillServerPlugin extends JavaPlugin {

    private BukkitRunnable shutdownTask;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("kill")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("server")) {
                    if (!sender.hasPermission("killserver.kill")) {
                        sender.sendMessage("§c권한이 없습니다.");
                        return true;
                    }

                    if (shutdownTask != null) {
                        sender.sendMessage("§e이미 종료 카운트다운이 진행 중입니다.");
                        return true;
                    }

                    sender.sendMessage("§c서버 종료까지 30초...");
                    shutdownTask = new BukkitRunnable() {
                        int countdown = 30;

                        @Override
                        public void run() {
                            if (countdown == 0) {
                                Bukkit.broadcastMessage("§c서버를 종료합니다.");
                                Bukkit.getServer().shutdown();
                                shutdownTask = null;
                                cancel();
                                return;
                            }

                            if (countdown <= 5 || countdown % 5 == 0) {
                                Bukkit.broadcastMessage("§e서버 종료까지 " + countdown + "초...");
                            }

                            countdown--;
                        }
                    };
                    shutdownTask.runTaskTimer(this, 0L, 20L); // 20 ticks = 1 second
                    return true;

                } else if (args[0].equalsIgnoreCase("cancel")) {
                    if (!sender.hasPermission("killserver.kill")) {
                        sender.sendMessage("§c권한이 없습니다.");
                        return true;
                    }

                    if (shutdownTask != null) {
                        shutdownTask.cancel();
                        shutdownTask = null;
                        Bukkit.broadcastMessage("§a서버 종료가 취소되었습니다.");
                    } else {
                        sender.sendMessage("§e진행 중인 종료 작업이 없습니다.");
                    }
                    return true;
                }
            }
        }

        return false;
    }
}
