package warning.warning.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import warning.warning.Data.ServerWarningConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigWarningGUI {

    private static Inventory inv;
    private static final ItemStack warningMuteItem = new ItemStack(Material.PAINTING,1,(short) 0);
    private static final ItemStack warningBanItem = new ItemStack(Material.PAINTING,1,(short) 0);
    private static final ItemStack muteTime = new ItemStack(Material.BED,1,(short) 0);
    private static final ItemStack banTime = new ItemStack(Material.BED,1,(short) 15);
    @SuppressWarnings("FieldMayBeFinal")
    private ServerWarningConfig serverWarningConfig;

    public ConfigWarningGUI() { serverWarningConfig = ServerWarningConfig.getInstance();}

    public Inventory getInv() {
        inv = Bukkit.createInventory(null,27,"§l경고 관리");
        int[] slot = { 0,1,2,3,4,5,6,7,8,18,19,21,22,23,25,26 };
        int[] slots = { 9,10,15,16,17 };
        settingItem(Material.STAINED_GLASS_PANE,1,15," ",null,slot);
        settingItem(Material.IRON_FENCE,1,0," ",null,slots);
        settingItem(Material.STONE_BUTTON,1,0,ChatColor.WHITE+"§l뮤트 적용하기",null,21);
        settingItem(Material.STONE_BUTTON,1,0,ChatColor.WHITE+"§l밴 적용하기",null,23);
        settingItem(Material.ENDER_PORTAL_FRAME,1,0,ChatColor.WHITE+"§l적용된 뮤트경고 목록",null,11);
        settingItem(Material.ENDER_PORTAL_FRAME,1,0,ChatColor.WHITE+"§l적용된 밴 경고 목록",null,15);
        settingItem(Material.PAPER,1,0,ChatColor.WHITE+"§l뮤트 초기화"
                , Collections.singletonList(ChatColor.YELLOW + "§l뮤트에대한 경고조건이 초기화됩니다."),20);
        settingItem(Material.PAPER,1,0,ChatColor.WHITE+"§l밴 초기화"
                , Collections.singletonList(ChatColor.YELLOW + "§l밴에대한 경고조건이 초기화됩니다."),24);
        WarningShow();
        WarningBanShow();
        setWarningMuteItem();
        setWarningBanItem();
        setMuteTime();
        setBanTime();

        return inv;
    }

    public void WarningShow() {
        ItemStack item = inv.getContents()[11];
        ItemMeta meta = item.getItemMeta();
        List<String> list = new ArrayList<>();
        for(int i : serverWarningConfig.getWarningMute().keySet()){
            list.add(ChatColor.YELLOW+"§l경고 : "+i+"회 -->"+" 뮤트 처벌시간 : "+transSec(serverWarningConfig.getMute(i)));
        }
        meta.setLore(list);
        item.setItemMeta(meta);
        inv.setItem(11,item);
    }

    public void WarningBanShow() {
        ItemStack item = inv.getContents()[15];
        ItemMeta meta = item.getItemMeta();
        List<String> list = new ArrayList<>();
        for(int i : serverWarningConfig.getWarningBan().keySet()){
            list.add(ChatColor.YELLOW+"§l경고 : "+i+"회 -->"+" 밴 처벌시간 : "+transSec(serverWarningConfig.getBan(i)));
        }
        meta.setLore(list);
        item.setItemMeta(meta);
        inv.setItem(15,item);
    }

    public void settingItem(Material material, int amount, int data, String name, List<String> lore, int... slot) {
        ItemStack item = new ItemStack(material,amount,(short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if(lore != null) { meta.setLore(lore); }
        item.setItemMeta(meta);
        for(int i : slot) inv.setItem(i,item);
    }

    public void setWarningMuteItem() {
        ItemMeta meta = warningMuteItem.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE+"§l뮤트 경고조건 : "+serverWarningConfig.getMuteWarnings()+"§l회");
        warningMuteItem.setItemMeta(meta);
        inv.setItem(3,warningMuteItem);
    }

    public void setWarningBanItem() {
        ItemMeta meta = warningBanItem.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE+"§l밴 경고조건 : "+serverWarningConfig.getBanWarnings()+"§l회");
        warningBanItem.setItemMeta(meta);
        inv.setItem(5,warningBanItem);
    }

    public void setMuteTime() {
        ItemMeta meta = muteTime.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE+"§l경고 "+serverWarningConfig.getMuteWarnings()+"회 뮤트 처벌 설정");
        meta.setLore(Collections.singletonList(ChatColor.YELLOW+"§l현재 처벌 시간 : "
                + ChatColor.WHITE + ChatColor.BOLD +transSec(serverWarningConfig.getMuteTime())));
        muteTime.setItemMeta(meta);
        inv.setItem(12,muteTime);
    }

    public void setBanTime() {
        ItemMeta meta = banTime.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE+"§l경고 "+serverWarningConfig.getBanWarnings()+"회 밴 처벌 설정");
        meta.setLore(Collections.singletonList(ChatColor.YELLOW+"§l현재 처벌 시간 : "
                + ChatColor.WHITE + ChatColor.BOLD +transSec(serverWarningConfig.getBanTime())));
        banTime.setItemMeta(meta);
        inv.setItem(14,banTime);
    }

    public static String transSec(int time) {
        if(time == 0) { return "0초"; }
        int day = time / ( 60* 60* 24 );
        int hour = (time - day * 60 * 60 * 24) / ( 60*60 );
        int minute = (time - day * 60 * 60 * 24 - hour * 3600) / 60;
        int second = time % 60;

        return ChatColor.WHITE+""+ChatColor.BOLD+day+"일 "+hour+"시간 "+minute+"분 "+second+"초";
    }

}
