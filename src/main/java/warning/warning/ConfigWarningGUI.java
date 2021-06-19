package warning.warning;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
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
        int[] slot = { 0,1,2,3,4,5,6,7,8,18,19,20,21,22,23,24,25,26 };
        int[] slots = { 9,10,11,15,16,17 };
        settingItem(Material.STAINED_GLASS_PANE,1,15," ",null,slot);
        settingItem(Material.IRON_FENCE,1,0," ",null,slots);
        settingItem(Material.ENDER_PORTAL_FRAME,1,0,ChatColor.WHITE+"§l사용법",
                Arrays.asList(ChatColor.YELLOW+"§l좌클릭시 1초증가",ChatColor.YELLOW+"§l쉬프트 좌클릭시 10초증가",
                        ChatColor.YELLOW+"§l우클릭시 1초감소",ChatColor.YELLOW+"§l쉬프트 우클릭시 10초감소"),13);
        setWarningMuteItem();
        setWarningBanItem();
        setMuteTime();
        setBanTime();

        return inv;
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
        meta.setLore(Collections.singletonList(ChatColor.YELLOW+"§l현재 처벌 시간 : "+ ChatColor.WHITE + ChatColor.BOLD +serverWarningConfig.getMuteTime() + "§l초"));
        muteTime.setItemMeta(meta);
        inv.setItem(12,muteTime);
    }

    public void setBanTime() {
        ItemMeta meta = banTime.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE+"§l경고 "+serverWarningConfig.getBanWarnings()+"회 밴 처벌 설정");
        meta.setLore(Collections.singletonList(ChatColor.YELLOW+"§l현재 처벌 시간 : "+ ChatColor.WHITE + ChatColor.BOLD +serverWarningConfig.getBanTime() + "§l초"));
        banTime.setItemMeta(meta);
        inv.setItem(14,banTime);
    }

}
