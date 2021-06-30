package warning.warning.subWarning;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import warning.warning.Data.PlayerWaring;
import warning.warning.GUI.CauseGUI;
import warning.warning.GUI.ConfigWarningGUI;
import warning.warning.GUI.WarningGUI;
import warning.warning.Data.ServerWarningConfig;
import warning.warning.Warning;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class WarningCommand implements CommandExecutor {

    public static HashMap<String,Integer> isMute = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;
        String name;
        String cause;
        Date date = new Date();
        ServerWarningConfig serverWarningConfig = ServerWarningConfig.getInstance();

        if(args.length == 0) {
            Text(player);
            return true;
        }

        switch (args[0]) {
            case "관리":
                if(!player.isOp()) { return true; }
                WarningGUI warningGUI = new WarningGUI();
                player.openInventory(warningGUI.getInv());
                break;
            case "주기":
                if(!player.isOp() && !player.hasPermission("Warning.police")) { return true; }
                if(args.length == 1) {
                    player.sendMessage(ChatStyle("플레이어를 입력해주세요."));
                    return true;
                }
                name = args[1];
                Player p = Bukkit.getPlayer(name);
                StringBuilder builder = new StringBuilder();
                for(int i = 2; i < args.length; i++) {
                    builder.append(args[i]);
                    builder.append(" ");
                }
                cause = builder.toString();
                if(p != null) {
                    PlayerWaring playerWaring = PlayerWaring.getPlayerWarning(p.getUniqueId());
                    playerWaring.addWarning();
                    playerWaring.addDay(playerWaring.getWarnings(),date);
                    if(args.length == 2) {
                        playerWaring.addCause(playerWaring.getWarnings(),null);
                        Bukkit.broadcastMessage(ChatStyle(p.getName()+"님이 경고를 받았습니다."));
                        Bukkit.broadcastMessage(ChatStyle("현재 누적경고 : "+playerWaring.getWarnings()));

                        if(serverWarningConfig.isMuteWarning(playerWaring.getWarnings())) {
                            isMute.put(name,serverWarningConfig.getMute(playerWaring.getWarnings()));
                            playerWaring.startMute(p.getUniqueId());
                            p.sendMessage(ChatStyle("경고 "+playerWaring.getWarnings()+"회가 누적되어 당신은 지금부터 뮤트상태가 활성화됩니다."));
                            p.sendMessage(ChatStyle("남은 시간 : "
                                    + ConfigWarningGUI.transSec(serverWarningConfig.getMute(playerWaring.getWarnings()))));
                            player.sendMessage(ChatStyle(p.getName() + "님이 경고누적으로 뮤트상태가 시작됩니다!"));
                        }
                        if(serverWarningConfig.isBanWarning(playerWaring.getWarnings())) {
                            playerWaring.changeBan();
                            playerWaring.setBanTime(serverWarningConfig.getBan(playerWaring.getWarnings()));
                            Bukkit.getBanList(BanList.Type.NAME).addBan(name,"경고누적",
                                    new Date(System.currentTimeMillis()+serverWarningConfig.getBan(playerWaring.getWarnings())* 1000L),player.getName());
                            Player banPlayer = Bukkit.getPlayer(name);
                            player.sendMessage(ChatStyle(p.getName() + "님이 경고누적으로 뮤트상태가 시작됩니다!"));
                            if(banPlayer == null) return true;
                            banPlayer.kickPlayer("차단되었습니다. 사유 : 경고누적");
                        }
                        return true;
                    }
                    playerWaring.addCause(playerWaring.getWarnings(),cause);
                    Bukkit.broadcastMessage(ChatStyle(p.getName()+"님이 경고를 받았습니다."));
                    Bukkit.broadcastMessage(ChatStyle("사유 : "+cause));
                    Bukkit.broadcastMessage(ChatStyle("현재 누적경고 : "+playerWaring.getWarnings()));
                    if(serverWarningConfig.isMuteWarning(playerWaring.getWarnings())) {
                        isMute.put(name,serverWarningConfig.getMute(playerWaring.getWarnings()));
                        playerWaring.startMute(p.getUniqueId());
                        player.sendMessage(ChatStyle(p.getName() + "님이 경고누적으로 뮤트상태가 시작됩니다!"));
                        p.sendMessage(ChatStyle("경고 "+playerWaring.getWarnings()+"회가 누적되어 당신은 지금부터 뮤트상태가 활성화됩니다."));
                        p.sendMessage(ChatStyle("남은 시간 : "
                                +ConfigWarningGUI.transSec(serverWarningConfig.getMute(playerWaring.getWarnings()))));
                    }
                    if(serverWarningConfig.isBanWarning(playerWaring.getWarnings())) {
                        playerWaring.changeBan();
                        playerWaring.setBanTime(serverWarningConfig.getBan(playerWaring.getWarnings()));
                        Bukkit.getBanList(BanList.Type.NAME).addBan(name,"경고누적",
                                new Date(System.currentTimeMillis()+serverWarningConfig.getBan(playerWaring.getWarnings())* 1000L),player.getName());
                        Player banPlayer = Bukkit.getPlayer(name);
                        player.sendMessage(ChatStyle(p.getName() + "님이 경고누적으로 정지상태가 시작됩니다!"));
                        if(banPlayer == null) return true;
                        banPlayer.kickPlayer("차단되었습니다. 사유 : 경고누적");
                    }
                    return true;
                }
                Warning.instance.getServer().getScheduler().runTask(Warning.instance, ()-> {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if(!offlinePlayer.hasPlayedBefore()) {
                        player.sendMessage(ChatStyle("알 수 없는 닉네임 입니다."));
                        return;
                    }
                    UUID uuid = offlinePlayer.getUniqueId();
                    PlayerWaring playerWaring = PlayerWaring.getPlayerWarning(uuid);
                    playerWaring.addWarning();
                    playerWaring.addDay(playerWaring.getWarnings(), date);
                    if(args.length == 2) {
                        playerWaring.addCause(playerWaring.getWarnings(),null);
                        Bukkit.broadcastMessage(ChatStyle(offlinePlayer.getName()+"님이 경고를 받았습니다."));
                        Bukkit.broadcastMessage(ChatStyle("현재 누적경고 : "+playerWaring.getWarnings()));
                        if(serverWarningConfig.isMuteWarning(playerWaring.getWarnings())) {
                            isMute.put(name,serverWarningConfig.getMute(playerWaring.getWarnings()));
                            playerWaring.startMute(uuid);
                            player.sendMessage(ChatStyle(offlinePlayer.getName() + "님이 경고누적으로 뮤트상태가 시작됩니다!"));
                        }
                        if(serverWarningConfig.isBanWarning(playerWaring.getWarnings())) {
                            playerWaring.changeBan();
                            playerWaring.setBanTime(serverWarningConfig.getBan(playerWaring.getWarnings()));
                            Bukkit.getBanList(BanList.Type.NAME).addBan(name,"경고누적",
                                    new Date(System.currentTimeMillis()+serverWarningConfig.getBan(playerWaring.getWarnings())* 1000L),player.getName());
                            player.sendMessage(ChatStyle(offlinePlayer.getName() + "님이 경고누적으로 정지상태가 시작됩니다!"));
                        }
                        return;
                    }
                    playerWaring.addCause(playerWaring.getWarnings(),cause);
                    Bukkit.broadcastMessage(ChatStyle(offlinePlayer.getName()+"님이 경고를 받았습니다."));
                    Bukkit.broadcastMessage(ChatStyle("사유 : "+cause));
                    Bukkit.broadcastMessage(ChatStyle("현재 누적경고 : "+playerWaring.getWarnings()));
                    if(serverWarningConfig.isMuteWarning(playerWaring.getWarnings())) {
                        isMute.put(name,serverWarningConfig.getMute(playerWaring.getWarnings()));
                        playerWaring.startMute(uuid);
                        player.sendMessage(ChatStyle(offlinePlayer.getName() + "님이 경고누적으로 뮤트상태가 시작됩니다!"));
                    }
                    if(serverWarningConfig.isBanWarning(playerWaring.getWarnings())) {
                        playerWaring.changeBan();
                        playerWaring.setBanTime(serverWarningConfig.getBan(playerWaring.getWarnings()));
                        Bukkit.getBanList(BanList.Type.NAME).addBan(name,"경고누적",
                                new Date(System.currentTimeMillis()+serverWarningConfig.getBan(playerWaring.getWarnings())* 1000L),player.getName());
                        player.sendMessage(ChatStyle(offlinePlayer.getName() + "님이 경고누적으로 정지상태가 시작됩니다!"));
                    }
                });
                break;
            case "설정":
                if(!player.isOp()) { return true; }
                if(args.length == 1) {
                    player.sendMessage(ChatStyle("플레이어를 입력해주세요."));
                    return true;
                }
                if(args.length == 2) {
                    player.sendMessage(ChatStyle("횟수를 입력해주세요."));
                    return true;
                }
                name = args[1];
                p = Bukkit.getPlayer(name);
                if(p == null) {
                    Warning.instance.getServer().getScheduler().runTask(Warning.instance, () -> {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                        UUID uuid = offlinePlayer.getUniqueId();
                        PlayerWaring playerWaring = PlayerWaring.getPlayerWarning(uuid);
                        int size = Integer.parseInt(args[2]);
                        if (size == playerWaring.getWarnings()) {
                            player.sendMessage(ChatStyle("이미 플레이어의 경고가 " + size + "입니다."));
                            return;
                        }
                        playerWaring.setWarning(size);
                        player.sendMessage(ChatStyle(offlinePlayer.getName() + "님의 경고가 " + size + "(으)로 변경되었습니다."));
                        if (serverWarningConfig.isMuteWarning(playerWaring.getWarnings())) {
                            isMute.put(name, serverWarningConfig.getMute(playerWaring.getWarnings()));
                            playerWaring.startMute(uuid);
                            player.sendMessage(ChatStyle(offlinePlayer.getName() + "님이 경고누적으로 뮤트상태가 시작됩니다!"));
                        }
                        if (serverWarningConfig.isBanWarning(playerWaring.getWarnings())) {
                            playerWaring.changeBan();
                            playerWaring.setBanTime(serverWarningConfig.getBan(playerWaring.getWarnings()));
                            Bukkit.getBanList(BanList.Type.NAME).addBan(name, "경고누적",
                                    new Date(System.currentTimeMillis() + serverWarningConfig.getBan(playerWaring.getWarnings()) * 1000L), player.getName());
                            player.sendMessage(ChatStyle(offlinePlayer.getName() + "님이 경고누적으로 정지상태가 시작됩니다!"));
                        }
                    });
                } else {
                    UUID uuid = p.getUniqueId();
                    PlayerWaring playerWaring = PlayerWaring.getPlayerWarning(uuid);
                    int size = Integer.parseInt(args[2]);
                    if (size == playerWaring.getWarnings()) {
                        player.sendMessage(ChatStyle("이미 플레이어의 경고가 " + size + "입니다."));
                        return true;
                    }
                    playerWaring.setWarning(size);
                    player.sendMessage(ChatStyle(p.getName() + "님의 경고가 " + size + "(으)로 변경되었습니다."));
                    if (serverWarningConfig.isMuteWarning(playerWaring.getWarnings())) {
                        isMute.put(name, serverWarningConfig.getMute(playerWaring.getWarnings()));
                        playerWaring.startMute(uuid);
                        p.sendMessage(ChatStyle("경고 "+playerWaring.getWarnings()+"회가 누적되어 당신은 지금부터 뮤트상태가 활성화됩니다."));
                        p.sendMessage(ChatStyle("남은 시간 : "
                                + ConfigWarningGUI.transSec(serverWarningConfig.getMute(playerWaring.getWarnings()))));
                        player.sendMessage(ChatStyle(p.getName() + "님이 경고누적으로 뮤트상태가 시작됩니다!"));
                    }
                    if (serverWarningConfig.isBanWarning(playerWaring.getWarnings())) {
                        playerWaring.changeBan();
                        playerWaring.setBanTime(serverWarningConfig.getBan(playerWaring.getWarnings()));
                        Bukkit.getBanList(BanList.Type.NAME).addBan(name, "경고누적",
                                new Date(System.currentTimeMillis() + serverWarningConfig.getBan(playerWaring.getWarnings()) * 1000L), player.getName());
                        p.kickPlayer("차단되었습니다. 사유 : 경고누적");
                        player.sendMessage(ChatStyle(p.getName() + "님이 경고누적으로 정지상태가 시작됩니다!"));
                    }
                }
                break;
            case "보기":
                CauseGUI causeGUI = new CauseGUI(player.getName());
                player.openInventory(causeGUI.getInv());
                break;
            case "사유":
                if(!player.isOp()) { return true; }
                if(args.length == 1) {
                    player.sendMessage(ChatStyle("플레이어를 입력해주세요."));
                    return true;
                }
                name = args[1];
                Warning.instance.getServer().getScheduler().runTask(Warning.instance, () -> {
                   OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                   if(!offlinePlayer.hasPlayedBefore()) {
                       player.sendMessage(ChatStyle("해당 플레이어는 존재하지 않습니다."));
                       return;
                   }
                    CauseGUI causeGUI1 = new CauseGUI(name);
                    player.openInventory(causeGUI1.getInv());
                });
                break;
            case "뮤트해제":
                if(!player.isOp()) { return true; }
                if(args.length == 1) {
                    player.sendMessage(ChatStyle("플레이어를 입력해주세요."));
                    return true;
                }
                name = args[1];
                PlayerWaring playerWaring = PlayerWaring.getPlayerWarning(name);
                if(!playerWaring.isMute()) {
                    player.sendMessage(ChatStyle("해당 플레이어는 뮤트상태가 아닙니다."));
                    return true;
                }
                playerWaring.setMute(false);
                isMute.remove(name);
                Player mutePlayer = Bukkit.getPlayer(name);
                player.sendMessage(ChatStyle("뮤트가 강제로 해제되었습니다."));
                if(mutePlayer == null) { return true; }
                mutePlayer.sendMessage(ChatStyle("뮤트가 강제로 해제되었습니다."));
                break;
            case "밴해제":
                if(!player.isOp()) { return true; }
                if(args.length == 1) {
                    player.sendMessage(ChatStyle("플레이어를 입력해주세요."));
                    return true;
                }
                name = args[1];
                playerWaring = PlayerWaring.getPlayerWarning(name);
                if(!playerWaring.isBan()) {
                    player.sendMessage(ChatStyle("해당 플레이어는 밴 상태가 아닙니다."));
                    return true;
                }
                playerWaring.changeBan();
                Bukkit.dispatchCommand(player,"unban "+name);
                player.sendMessage(ChatStyle("밴이 강제로 해제되었습니다."));
                break;
            case "사유변경":
                if(!player.isOp()) { return true; }
                if(args.length < 4) {
                    player.sendMessage(ChatStyle("명령어를 제대로 기입해주세요."));
                    return true;
                }
                builder = new StringBuilder();
                for(int i = 3; i < args.length; i++) {
                    builder.append(args[i]);
                    builder.append(" ");
                }
                cause = builder.toString();
                name = args[1];
                int warnings = Integer.parseInt(args[2]);
                Player pl = Bukkit.getPlayer(name);
                if(pl == null) {
                    Warning.instance.getServer().getScheduler().runTask(Warning.instance, ()-> {
                       OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                       PlayerWaring playerWaring1 = PlayerWaring.getPlayerWarning(offlinePlayer.getUniqueId());
                        if(!playerWaring1.isCause(warnings)) {
                            player.sendMessage(ChatStyle("경고횟수가 존재하지 않습니다."));
                            return;
                        }
                        playerWaring1.replaceCause(warnings,cause);
                        player.sendMessage(ChatStyle("변경되었습니다."));
                    });
                } else {
                    playerWaring = PlayerWaring.getPlayerWarning(pl.getUniqueId());
                    if(!playerWaring.isCause(warnings)) {
                        player.sendMessage(ChatStyle("경고횟수가 존재하지 않습니다."));
                        return true;
                    }
                    playerWaring.replaceCause(warnings,cause);
                    player.sendMessage(ChatStyle("변경되었습니다."));
                }
                break;
            case "뮤트":
                if(args.length < 3) {
                    player.sendMessage(ChatStyle("명령어를 제대로 기입해주세요."));
                    return true;
                }
                Player byPlayer = Bukkit.getPlayerExact(args[1]);
                int time = Integer.parseInt(args[2]);
                if(byPlayer == null) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                    playerWaring = PlayerWaring.getPlayerWarning(offlinePlayer.getUniqueId());
                    if(!offlinePlayer.hasPlayedBefore()) {
                        player.sendMessage(ChatStyle("주의 :: 해당 플레이어는 서버를 한번도 접속하지 않았습니다."));
                    }
                    if(playerWaring.isMute()) {
                        player.sendMessage(ChatStyle("해당 플레이어는 이미 뮤트를 진행중입니다."));
                        return true;
                    }
                    playerWaring.startMute(offlinePlayer.getUniqueId());
                    isMute.put(args[1],time);
                    player.sendMessage(ChatStyle(args[1] + "님을 " + ConfigWarningGUI.transSec(time) + "동안 뮤트를 줬습니다."));

                    return true;
                }
                playerWaring = PlayerWaring.getPlayerWarning(byPlayer.getUniqueId());
                if(playerWaring.isMute()) {
                    player.sendMessage(ChatStyle("해당 플레이어는 이미 뮤트를 진행중입니다."));
                    return true;
                }
                playerWaring.startMute(byPlayer.getUniqueId());
                isMute.put(args[1],time);
                player.sendMessage(ChatStyle(args[1] + "님을 " + ConfigWarningGUI.transSec(time) + "동안 뮤트를 줬습니다."));
                break;
            case "밴":
                if(args.length < 3) {
                    player.sendMessage(ChatStyle("명령어를 제대로 기입해주세요."));
                    return true;
                }
                byPlayer = Bukkit.getPlayerExact(args[1]);
                time = Integer.parseInt(args[2]);
                playerWaring = PlayerWaring.getPlayerWarning(player.getUniqueId());
                if(byPlayer == null) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                    if(!offlinePlayer.hasPlayedBefore()) {
                        player.sendMessage(ChatStyle("주의 :: 해당 플레이어는 서버를 한번도 접속하지 않았습니다."));
                    }
                    playerWaring.changeBan();
                    Bukkit.getBanList(BanList.Type.NAME).addBan(args[1],"관리자에 의하여 정지를 당했습니다.", new Date(time * 1000L),player.getName());
                    player.sendMessage(ChatStyle(args[1] + "님을 " + ConfigWarningGUI.transSec(time) + "동안 밴을 줬습니다."));

                    return true;
                }
                playerWaring.changeBan();
                Bukkit.getBanList(BanList.Type.NAME).addBan(byPlayer.getName(),"관리자에 의하여 정지를 당했습니다.", new Date(time * 1000L),player.getName());
                player.sendMessage(ChatStyle(args[1] + "님을 " + ConfigWarningGUI.transSec(time) + "동안 밴을 줬습니다."));
                break;
            default:
                player.sendMessage(ChatStyle("알 수 없는 명령어"));
                break;
        }



        return true;
    }

    public static String ChatStyle(String msg) { return ChatColor.RED+"§l[ 경고 ] "+ChatColor.WHITE+ChatColor.BOLD+msg; }

    public void Text(Player player) {
        if(!player.isOp()) {
            player.sendMessage(ChatStyle("/경고 보기"));    //플레이어 사용가능
            return;
        }
        player.sendMessage(ChatStyle("/경고 주기 (플레이어) (사유)"));
        player.sendMessage(ChatStyle("/경고 설정 (플레이어) (횟수)"));
        player.sendMessage(ChatStyle("/경고 사유 (플레이어)"));
        player.sendMessage(ChatStyle("/경고 관리"));
        player.sendMessage(ChatStyle("/경고 사유변경 (플레이어) (바꿀경고횟수) (변경할 사유)"));
        player.sendMessage(ChatStyle("/경고 뮤트해제 (플레이어)"));
        player.sendMessage(ChatStyle("/경고 밴해제 (플레이어)"));
        player.sendMessage(ChatStyle("/경고 뮤트 (플레이어) (시간)"));
        player.sendMessage(ChatStyle("/경고 밴 (플레이어) (시간)"));
    }

}
