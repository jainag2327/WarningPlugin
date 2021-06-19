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

public class WarningPlayerGUI {
    private Inventory inv;
    private ItemStack item;

    public WarningPlayerGUI(ItemStack item) {
        this.item = item;
    }

    public Inventory getInv() {
        inv = Bukkit.createInventory(null,9,"§l플레이어 경고 관리");
        String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        PlayerWaring playerWaring = PlayerWaring.getPlayerWarning(name);

        inv.setItem(0,item);
        settingItem(Material.STAINED_GLASS_PANE,1,15," ",null,1);
        settingItem(Material.MUSHROOM_SOUP,1,0, ChatColor.WHITE+"§l경고 주기", null,2);
        settingItem(Material.BOWL,1,0, ChatColor.WHITE+"§l경고 감소", null,3);
        settingItem(Material.ANVIL,1,0, ChatColor.WHITE+"§l사유 보기", null,6);
        settingItem(Material.BARRIER,1,0, ChatColor.RED+"§l경고 초기화",
                Collections.singletonList(ChatColor.DARK_RED + "§l주의 :: 이 플레이어에 대한모든 경고가 초기화됩니다."),8);
        settingItem(Material.MINECART,1,0,ChatColor.RED+"§l뮤트", Arrays.asList(ChatColor.WHITE+"§l현재 플레이어 뮤트 상태 : "+playerWaring.isMute()),4);
        settingItem(Material.HOPPER_MINECART,1,0,ChatColor.RED+"§l밴", Arrays.asList(ChatColor.WHITE+"§l현재 플레이어 밴 상태 : "+playerWaring.isBan()),5);

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
}
