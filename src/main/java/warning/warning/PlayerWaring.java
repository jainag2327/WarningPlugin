package warning.warning;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class PlayerWaring {

    public transient static final HashMap<UUID,PlayerWaring> warMap = new HashMap<>();

    public static PlayerWaring getPlayerWarning(UUID uuid) { return warMap.containsKey(uuid) ? warMap.get(uuid) : new PlayerWaring(uuid); }

    public static PlayerWaring getPlayerWarning(String name) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);    //테스트 필요 오프라인플레이어 다른쓰레드 사용 안함
        UUID uuid = offlinePlayer.getUniqueId();

        return warMap.containsKey(uuid) ? warMap.get(uuid) : new PlayerWaring(uuid);
    }

    public PlayerWaring(UUID uuid) {
        mute = false;
        muteTime = 0;
        BanTime = 0;
        this.uuid = uuid;
        warnings = 0;
        warMap.put(uuid,this);
    }

    private int warnings;
    @SuppressWarnings("FieldMayBeFinal")
    private UUID uuid;
    @SuppressWarnings("FieldMayBeFinal")
    private HashMap<Integer,String> causeMap = new HashMap<>();     // 경고 횟수 . 이유
    @SuppressWarnings("FieldMayBeFinal")
    private HashMap<Integer, Date> DayMap = new HashMap<>();       // 경고 획수 . 날짜

    private int muteTime;

    private int BanTime;

    private boolean mute;

    private boolean ban;


    public boolean isBan() { return ban; }

    public void changeBan() { ban = !ban; }

    public boolean isMute() { return mute; }

    public void changeMute() { mute = !mute; }

    public void unMute() { mute = false; }


    public int getMuteTime() { return muteTime; }

    public int getBanTime() { return BanTime; }

    public void setMuteTime(int muteTime) { this.muteTime = muteTime; }

    public void setBanTime(int banTime) { this.BanTime = banTime; }

    public void reduceMuteTime() { muteTime--; }

    public void reduceBanTime() { BanTime--; }




    public void addCause(int warnings, String cause) { causeMap.put(warnings,cause); }

    public void replaceCause(int warnings, String cause) { causeMap.replace(warnings,cause); }

    public String getCause(int warnings) { return causeMap.get(warnings); }


    public void addDay(int warnings, Date day) { DayMap.put(warnings,day); }

    public void replace(int warnings,Date day) { DayMap.replace(warnings,day); }

    public Date getDay(int warnings) { return DayMap.get(warnings); }

    public UUID getUUID() { return uuid; }



    public int getWarnings() { return warnings; }

    public void setWarning(int warnings) {
        if(this.warnings < warnings) {
            for(int i = this.warnings + 1; i <= warnings; i++) {
                causeMap.put(i,"null");
                DayMap.put(i,null);
            }
        } else {
            for(int i = this.warnings; i > warnings; i--) {
                causeMap.remove(i);
                DayMap.remove(i);
            }
        }
        this.warnings = warnings;
    }

    public void addWarning() { warnings++; }

    public void reduceWarning() {
        causeMap.remove(warnings);
        DayMap.remove(warnings);
        warnings--;
    }

    public void removeWarning(int warnings) { causeMap.remove(warnings); }

    public void resetWarning() {
        warnings = 0;
        causeMap.clear();
        DayMap.clear();
    }

    public static List<UUID> getWarningUUID() {
        List<UUID> list = new ArrayList<>();
        for(UUID uuid : warMap.keySet()) {
            PlayerWaring playerWaring = PlayerWaring.getPlayerWarning(uuid);
            if(playerWaring.getWarnings() > 0) {
                list.add(uuid);
            }
        }
        return list;
    }

    public static void loadData(FileConfiguration config) {
        config.getKeys(false).forEach(key -> {
            UUID uuid = UUID.fromString(key);
            warMap.put(uuid,new Gson().fromJson(config.getString(key),PlayerWaring.class));
        });
    }
}
