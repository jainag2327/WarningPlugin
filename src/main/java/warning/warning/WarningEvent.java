package warning.warning;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;

public class WarningEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if(e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) { return; }

        WarningGUI warningGUI = new WarningGUI();
        if(e.getInventory().getTitle().equals("§l플레이어 경고 리스트")) {
            e.setCancelled(true);
            if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
                WarningPlayerGUI warningPlayerGUI = new WarningPlayerGUI(e.getCurrentItem());
                player.openInventory(warningPlayerGUI.getInv());
            }
            if(e.getCurrentItem().getType().equals(Material.COMPASS)) {
                ConfigWarningGUI configWarningGUI = new ConfigWarningGUI();
                player.openInventory(configWarningGUI.getInv());
            }
        }
        if(e.getInventory().getTitle().equals("§l플레이어 경고 관리")) {
            e.setCancelled(true);
            String name = ChatColor.stripColor(e.getInventory().getContents()[0].getItemMeta().getDisplayName());

            PlayerWaring playerWaring = PlayerWaring.getPlayerWarning(name);

            ServerWarningConfig serverWarningConfig = ServerWarningConfig.getInstance();

            if(e.getCurrentItem().getType().equals(Material.MUSHROOM_SOUP)) {
                playerWaring.addWarning();
                if(serverWarningConfig.isMuteWarning(playerWaring.getWarnings())) {
                    WarningCommand.isMute.put(name,serverWarningConfig.getMute(playerWaring.getWarnings()));
                    Player mutePlayer = Bukkit.getPlayer(name);
                    if(mutePlayer == null) { return; }
                    mutePlayer.sendMessage(WarningCommand.ChatStyle("경고 "+playerWaring.getWarnings()+"회가 누적되어 당신은 지금부터 뮤트상태가 활성화됩니다."));
                    mutePlayer.sendMessage(WarningCommand.ChatStyle("남은 시간 : "+serverWarningConfig.getMute(playerWaring.getWarnings()) +"초"));
                    mutePlayer.chat(" ");
                }
                if(serverWarningConfig.isBanWarning(playerWaring.getWarnings())) {
                    playerWaring.changeBan();
                    playerWaring.setBanTime(serverWarningConfig.getBan(playerWaring.getWarnings()));
                    Bukkit.getBanList(BanList.Type.NAME).addBan(name,"경고누적",
                            new Date(System.currentTimeMillis()+playerWaring.getBanTime()* 1000L),player.getName());
                    Player banPlayer = Bukkit.getPlayer(name);
                    if(banPlayer == null) { return; }
                    banPlayer.kickPlayer("차단되었습니다. 사유 : 경고누적");

                }
                playerWaring.addCause(playerWaring.getWarnings(),null);
                Date date = new Date();
                playerWaring.addDay(playerWaring.getWarnings(),date);
                player.sendMessage(WarningCommand.ChatStyle(name+"님의 경고가 증가했습니다."));
                player.openInventory(warningGUI.getInv());

            }
            if(e.getCurrentItem().getType().equals(Material.BOWL)) {
                playerWaring.reduceWarning();
                player.sendMessage(WarningCommand.ChatStyle(name+"님의 경고가 감소했습니다."));
                player.openInventory(warningGUI.getInv());
            }
            if(e.getCurrentItem().getType().equals(Material.ANVIL)) {
                CauseGUI causeGUI = new CauseGUI(name);
                player.openInventory(causeGUI.getInv());
            }
            if(e.getCurrentItem().getType().equals(Material.BARRIER)) {
                playerWaring.resetWarning();
                player.closeInventory();
                player.sendMessage(WarningCommand.ChatStyle(name+"님의 모든 경고가 초기화되었습니다."));
            }
        }
        if(e.getInventory().getTitle().equals("§l경고 관리")) {
            ServerWarningConfig serverWarningConfig = ServerWarningConfig.getInstance();
            ConfigWarningGUI configWarningGUI = new ConfigWarningGUI();
            e.setCancelled(true);
            if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.WHITE+"§l뮤트 경고조건 : "+serverWarningConfig.getMuteWarnings()+"§l회")) {
                if(e.getClick() == ClickType.LEFT) {
                    serverWarningConfig.addMuteWarningCount();
                    player.openInventory(configWarningGUI.getInv());
                } else if(e.getClick() == ClickType.RIGHT) {
                    if(serverWarningConfig.getMuteWarnings() == 0) { return; }
                    serverWarningConfig.reduceMuteWarningCount();
                    player.openInventory(configWarningGUI.getInv());
                }
            }
            if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.WHITE+"§l밴 경고조건 : "+serverWarningConfig.getBanWarnings()+"§l회")) {
                if(e.getClick() == ClickType.LEFT) {
                    serverWarningConfig.addBanWarningCount();
                    player.openInventory(configWarningGUI.getInv());
                }  else if(e.getClick() == ClickType.RIGHT) {
                    if(serverWarningConfig.getBanWarnings() == 0) { return; }
                    serverWarningConfig.reduceBanWarningCount();
                    player.openInventory(configWarningGUI.getInv());
                }
            }
            if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.WHITE+"§l경고 "+serverWarningConfig.getMuteWarnings()+"회 뮤트 처벌 설정")) {
                if(e.getClick() == ClickType.LEFT) {
                    serverWarningConfig.addMuteTime();
                    player.openInventory(configWarningGUI.getInv());
                } else if(e.getClick() == ClickType.SHIFT_LEFT) {
                    serverWarningConfig.addDoubleMuteTime();
                    player.openInventory(configWarningGUI.getInv());
                } else if(e.getClick() == ClickType.RIGHT) {
                    if(serverWarningConfig.getMuteTime() == 0) { return; }
                    serverWarningConfig.reduceMuteTime();
                    player.openInventory(configWarningGUI.getInv());
                } else if(e.getClick() == ClickType.SHIFT_RIGHT) {
                    if(serverWarningConfig.getMuteTime() - 10 < 0) { return; }
                    serverWarningConfig.reduceDoubleMuteTime();
                    player.openInventory(configWarningGUI.getInv());
                }
            }
            if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.WHITE+"§l경고 "+serverWarningConfig.getBanWarnings()+"회 밴 처벌 설정")) {
                if(e.getClick() == ClickType.LEFT) {
                    serverWarningConfig.addBanTime();
                    player.openInventory(configWarningGUI.getInv());
                } else if(e.getClick() == ClickType.SHIFT_LEFT) {
                    serverWarningConfig.addDoubleBanTime();
                    player.openInventory(configWarningGUI.getInv());
                } else if(e.getClick() == ClickType.RIGHT) {
                    if(serverWarningConfig.getBanTime() == 0) { return; }
                    serverWarningConfig.reduceBanTime();
                    player.openInventory(configWarningGUI.getInv());
                } else if(e.getClick() == ClickType.SHIFT_RIGHT) {
                    if(serverWarningConfig.getBanTime() - 10 < 0) { return; }
                    serverWarningConfig.reduceDoubleBanTime();
                    player.openInventory(configWarningGUI.getInv());
                }
            }
            if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.WHITE+"§l뮤트 적용하기")) {
                if(serverWarningConfig.isMuteWarning(serverWarningConfig.getMuteWarnings())
                        && serverWarningConfig.isMuteTime(serverWarningConfig.getMuteTime())) {
                    player.sendMessage(WarningCommand.ChatStyle("이미 적용되었습니다."));
                    return;
                }
                serverWarningConfig.addMuteWarning(serverWarningConfig.getMuteWarnings(),serverWarningConfig.getMuteTime());
                configWarningGUI.WarningShow();
                player.openInventory(configWarningGUI.getInv());
            }
            if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.WHITE+"§l밴 적용하기")) {
                if(serverWarningConfig.isMuteWarning(serverWarningConfig.getMuteWarnings())
                        && serverWarningConfig.isMuteTime(serverWarningConfig.getMuteTime())) {
                    player.sendMessage(WarningCommand.ChatStyle("이미 적용되었습니다."));
                    return;
                }
                serverWarningConfig.addBanWarning(serverWarningConfig.getBanWarnings(),serverWarningConfig.getBanTime());
                configWarningGUI.WarningBanShow();
                player.openInventory(configWarningGUI.getInv());
            }
            if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.WHITE+"§l뮤트 초기화")) {
                serverWarningConfig.removeMuteTime();
                player.openInventory(configWarningGUI.getInv());
            }
            if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.WHITE+"§l밴 초기화")) {
                serverWarningConfig.removeBanTime();
                player.openInventory(configWarningGUI.getInv());
            }

        }
        if(e.getInventory().getTitle().equals("§l경고 사유")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMute(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if(!WarningCommand.isMute.containsKey(player.getName())) { return; }

        PlayerWaring playerWaring = PlayerWaring.getPlayerWarning(player.getUniqueId());

        if(playerWaring.isMute()) {
            e.setCancelled(true);
            player.sendMessage(WarningCommand.ChatStyle("현재 뮤트상태입니다."));
            player.sendMessage(WarningCommand.ChatStyle("남은시간 : "+playerWaring.getMuteTime()+"초"));
            return;
        }

        e.setCancelled(true);
        playerWaring.changeMute();
        if(playerWaring.getMuteTime() == 0) {
            playerWaring.setMuteTime(WarningCommand.isMute.get(player.getName()));
        } else {
            playerWaring.setMuteTime(playerWaring.getMuteTime());
        }
        player.sendMessage(WarningCommand.ChatStyle("뮤트가 활성화 되었습니다."));

        new BukkitRunnable() {

            @Override
            public void run() {
                if(playerWaring.getMuteTime() == 0) {
                    Player mutePlayer = Bukkit.getPlayer(playerWaring.getUUID());
                    if(mutePlayer != null) {
                        mutePlayer.sendMessage(WarningCommand.ChatStyle("뮤트가 해제되었습니다."));
                    }
                    e.setCancelled(false);
                    WarningCommand.isMute.remove(player.getName());
                    playerWaring.changeMute();
                    cancel();
                    return;
                }
                playerWaring.reduceMuteTime();
            }
        }.runTaskTimer(Warning.instance, 0,20);
    }


}


