package warning.warning;

public class ServerWarningConfig {
    private static ServerWarningConfig serverWarningConfig = null;

    public static ServerWarningConfig getInstance() {
        if(serverWarningConfig == null) { serverWarningConfig = new ServerWarningConfig(); }
        return serverWarningConfig;
    }



    private int warningMuteCount = 1;

    private int warningBanCount = 1;

    private int muteTime = 1;

    private int banTime = 1;



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
