package warning.warning;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class WarningGUI {

    private Inventory inv;

    @SuppressWarnings("FieldMayBeFinal")
    private PlayerWaring playerWaring;

    public WarningGUI() {}

    public Inventory getInv() {
        inv = Bukkit.createInventory(null,54,"§l플레이어 경고 리스트");
        int[] slot = { 45,46,47,48,50,51,52,53 };

        settingItem(Material.STAINED_GLASS_PANE,1,15," ",null,slot);
        settingItem(Material.COMPASS,1,0,ChatColor.WHITE+"§l경고 관리", Collections.singletonList(ChatColor.YELLOW + "§l>> 클릭시 이동됩니다."),49);
        Warning.instance.getServer().getScheduler().runTask(Warning.instance, ()-> {
            for(UUID uuid : PlayerWaring.getWarningUUID()) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                if(offlinePlayer.isOnline()) {
                    inv.addItem(getPlayerHead(offlinePlayer.getPlayer()));
                } else {
                    inv.addItem(getPlayerHead(offlinePlayer.getName(),offlinePlayer.getUniqueId()));
                }
            }
        });

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

    public ItemStack getPlayerHead(Player player) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM,1,(short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setOwningPlayer(player);
        skull.setDisplayName(ChatColor.WHITE+""+ChatColor.BOLD+player.getName());
        playerWaring = PlayerWaring.getPlayerWarning(player.getUniqueId());
        skull.setLore(Collections.singletonList(ChatColor.YELLOW+"§l경고 : "+ playerWaring.getWarnings()));
        item.setItemMeta(skull);

        return item;
    }

    public ItemStack getPlayerHead(String player,UUID uuid) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM,1,(short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName(net.md_5.bungee.api.ChatColor.BOLD+player);

        playerWaring = PlayerWaring.getPlayerWarning(uuid);
        skull.setLore(Collections.singletonList(ChatColor.YELLOW+"§l경고 : "+ playerWaring.getWarnings()));
        item.setItemMeta(skull);

        return item;
    }


}
