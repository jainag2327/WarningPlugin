package warning.warning;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.UUID;

public final class Warning extends JavaPlugin {
    public static Warning instance;

    public File baseFolder = getDataFolder();

    public File file = new File(getDataFolder(),"PlayerData.yml");
    public FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    public File configFile = new File(getDataFolder(),"ConfigData.yml");

    public File ChatFile = new File(getDataFolder(),"ChatData.yml");

    @Override
    public void onEnable() {
        if(!baseFolder.exists()) {
            if(baseFolder.mkdir()) {
                getLogger().info("Config 폴더 생성 완료.");
            }
        }
        makeFile(configFile);
        makeFile(ChatFile);
        instance = this;
        fileToClass();
        PlayerWaring.loadData(config);
        FileToMap();
        getCommand("경고").setExecutor(new WarningCommand());
        getServer().getPluginManager().registerEvents(new WarningEvent(),this);
        for(UUID uuid : PlayerWaring.warMap.keySet()) {
            PlayerWaring playerWaring = PlayerWaring.getPlayerWarning(uuid);
            playerWaring.unMute();
            Player player = instance.getServer().getPlayer(uuid);
            if(player == null) { continue; }
            if(WarningCommand.isMute.containsKey(player.getName())) {
                player.chat(" ");
            }
        }
    }

    @Override
    public void onDisable() {
        SavePlayerData();
        SaveConfigData();
        MapToFile();
    }

    public void SavePlayerData() {
        for(UUID uuid : PlayerWaring.warMap.keySet()) {
            PlayerWaring playerWaring = PlayerWaring.getPlayerWarning(uuid);
            config.set(uuid.toString(),new Gson().toJson(playerWaring));
        }
        try {
            config.save(file);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SaveConfigData() {
        try {
            ServerWarningConfig serverWarningConfig = ServerWarningConfig.getInstance();
            FileWriter writer = new FileWriter(configFile, false);
            writer.write(serverWarningConfig.getBanWarnings()+",");
            writer.write(serverWarningConfig.getMuteWarnings()+",");
            writer.write(serverWarningConfig.getBanTime()+",");
            writer.write(serverWarningConfig.getMuteTime()+",");
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileToClass() {
        try {
            ServerWarningConfig serverWarningConfig = ServerWarningConfig.getInstance();
            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            String fileLine;
            while ((fileLine = reader.readLine()) != null) {
                serverWarningConfig.setBanWarnings(Integer.parseInt(fileLine.split(",")[0]));
                serverWarningConfig.setMuteWarnings(Integer.parseInt(fileLine.split(",")[1]));
                serverWarningConfig.setBanTime(Integer.parseInt(fileLine.split(",")[2]));
                serverWarningConfig.setMuteTime(Integer.parseInt(fileLine.split(",")[3]));
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeFile(File f) {
        if (!f.exists() || !f.isFile()) {
            try {
                if(f.createNewFile()) {
                    getLogger().info("config 파일 생성완료");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void MapToFile() {
        try {
            FileWriter writer = new FileWriter(ChatFile, false);
            for(String name : WarningCommand.isMute.keySet()) {
                writer.write(name+","+WarningCommand.isMute.get(name));
            }
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void FileToMap() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ChatFile));
            String fileLine = null;
            while ((fileLine = reader.readLine()) != null) {
                WarningCommand.isMute.put(fileLine.split(",")[0],Integer.parseInt(fileLine.split(",")[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
