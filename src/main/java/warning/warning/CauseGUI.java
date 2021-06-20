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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CauseGUI {
    @SuppressWarnings("FieldMayBeFinal")
    private PlayerWaring playerWaring;
    private Inventory inv;

    public CauseGUI(String name) {
        playerWaring = PlayerWaring.getPlayerWarning(name);
    }

    public Inventory getInv() {
        inv = Bukkit.createInventory(null,54,"§l경고 사유");

        int[] slot = { 45,46,47,48,50,51,52,53 };
        settingItem(Material.STAINED_GLASS_PANE,1,15," ",null,slot);
        Player player = Bukkit.getPlayer(playerWaring.getUUID());
        if(player == null) {
            Warning.instance.getServer().getScheduler().runTask(Warning.instance, ()-> {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerWaring.getUUID());
                inv.setItem(49,getPlayerHead(offlinePlayer.getName(),offlinePlayer.getUniqueId()));
            });
        } else {
            inv.setItem(49,getPlayerHead(player));
        }
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i = 1; i <= playerWaring.getWarnings(); i++ ) {
            String to = fm.format(playerWaring.getDay(i));
            if(playerWaring.getCause(i) == null || playerWaring.getCause(i).equals("null")) {
                settingItem(Material.NAME_TAG,1,0, ChatColor.RED+"§l경고 : "+ChatColor.BOLD+i,
                        Arrays.asList(ChatColor.YELLOW + "§l사유 : " + ChatColor.WHITE + "§l없음",
                                ChatColor.YELLOW+"§l처리 일시 : "+ChatColor.WHITE+""+ChatColor.BOLD+to));
                continue;
            }
            settingItem(Material.NAME_TAG,1,0, ChatColor.RED+"§l경고 : "+ChatColor.BOLD+i,
                    Arrays.asList(ChatColor.YELLOW + "§l사유 : " + ChatColor.WHITE+ ChatColor.BOLD + playerWaring.getCause(i)
                            ,ChatColor.YELLOW+"§l처리 일시 : "+ChatColor.WHITE+""+ChatColor.BOLD+to));
        }
        return inv;
    }

    public void settingItem(Material material, int amount, int data, String name, List<String> lore) {
        ItemStack item = new ItemStack(material,amount,(short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if(lore != null) { meta.setLore(lore); }
        item.setItemMeta(meta);
        inv.addItem(item);
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

    public ItemStack getPlayerHead(String player, UUID uuid) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM,1,(short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName(net.md_5.bungee.api.ChatColor.BOLD+player);

        playerWaring = PlayerWaring.getPlayerWarning(uuid);
        skull.setLore(Collections.singletonList(ChatColor.YELLOW+"§l경고 : "+ playerWaring.getWarnings()));
        item.setItemMeta(skull);

        return item;
    }
}
