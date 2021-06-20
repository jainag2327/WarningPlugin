package warning.warning;

import com.google.gson.Gson;
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
    public FileConfiguration config2 = YamlConfiguration.loadConfiguration(configFile);

    public File ChatFile = new File(getDataFolder(),"ChatData.yml");

    @Override
    public void onEnable() {
        getLogger().info("제작 : SoonSaL_");
        getLogger().info("2차 배포 / 수정 금지");
        getLogger().info("플러그인이 활성화 되었습니다.");
        if(!baseFolder.exists()) {
            if(baseFolder.mkdir()) {
                getLogger().info("Config 폴더 생성 완료.");
            }
        }
        makeFile(ChatFile);
        instance = this;
        ServerWarningConfig.loadData(config2);
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
        SaveTheFile();
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

    public void SaveTheFile() {
        ServerWarningConfig serverWarningConfig = ServerWarningConfig.getInstance();
        config2.set("server",new Gson().toJson(serverWarningConfig));
        try{
            config2.save(configFile);
        } catch (IOException e) {
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
            String fileLine;
            while ((fileLine = reader.readLine()) != null) {
                WarningCommand.isMute.put(fileLine.split(",")[0],Integer.parseInt(fileLine.split(",")[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
