package warning.warning;

import com.google.gson.Gson;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class ServerWarningConfig {
    private transient static ServerWarningConfig serverWarningConfig = null;

    public static ServerWarningConfig getInstance() {
        if(serverWarningConfig == null) { serverWarningConfig = new ServerWarningConfig(); }
        return serverWarningConfig;
    }

    @SuppressWarnings("FieldMayBeFinal")
    private  HashMap<Integer,Integer> warningMute = new HashMap<>();

    @SuppressWarnings("FieldMayBeFinal")
    private  HashMap<Integer,Integer> warningBan = new HashMap<>();

    private int warningMuteCount = 1;

    private int warningBanCount = 1;

    private int muteTime = 1;

    private int banTime = 1;

    public static void loadData(FileConfiguration config) {
        serverWarningConfig = new Gson().fromJson(config.getString("server"),ServerWarningConfig.class);
    }



    public boolean isMuteWarning(int warning) { return warningMute.containsKey(warning); }

    public boolean isMuteTime(int time) { return warningMute.containsValue(time); }

    public boolean isBanWarning(int warning) { return warningBan.containsKey(warning); }

    public void addMuteWarning(int warning, int time) { warningMute.put(warning, time); }

    public void addBanWarning(int warning, int time) { warningBan.put(warning, time); }

    public HashMap<Integer,Integer> getWarningMute() { return warningMute; }

    public HashMap<Integer,Integer> getWarningBan() { return warningBan; }

    public Integer getMute(int warning) { return warningMute.get(warning); }

    public Integer getBan(int warning) { return warningBan.get(warning); }

    public void removeMuteTime() { warningMute.clear(); }

    public void removeBanTime() { warningBan.clear(); }



    public void setMuteWarnings(int warningMuteCount) { this.warningMuteCount = warningMuteCount; }

    public void setBanWarnings(int warningBanCount) { this.warningBanCount = warningBanCount; }

    public void setMuteTime(int muteTime) { this.muteTime = muteTime; }

    public void setBanTime(int banTime) { this.banTime = banTime; }



    public int getMuteWarnings() { return warningMuteCount; }

    public int getBanWarnings() { return warningBanCount; }

    public int getMuteTime() { return muteTime; }

    public int getBanTime() { return banTime; }


    public void addMuteWarningCount() { warningMuteCount++; }

    public void addBanWarningCount() { warningBanCount++; }

    public void addMuteTime() { muteTime++; }

    public void addBanTime() { banTime++; }


    public void reduceMuteWarningCount() { warningMuteCount--; }

    public void reduceBanWarningCount() { warningBanCount--; }

    public void reduceMuteTime() { muteTime--; }

    public void reduceBanTime() { banTime--; }


    public void addDoubleMuteTime() { muteTime += 10; }

    public void addDoubleBanTime() { banTime += 10; }


    public void reduceDoubleMuteTime() { muteTime -= 10; }

    public void reduceDoubleBanTime() { banTime -= 10; }



}
