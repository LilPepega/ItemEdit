package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.Util;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class Rename extends SubCmd {

    public Rename(ItemEditCommand cmd) {
        super("rename", cmd, true, true);
    }

    @Override
    public void onCommand(CommandSender sender, String alias, String[] args) {
        Player p = (Player) sender;
        ItemStack item = this.getItemInHand(p);
        if (!Util.isAllowedRenameItem(sender, item.getType()))
            return;

        ItemMeta itemMeta = item.getItemMeta();
        if (args.length == 1) {
            itemMeta.setDisplayName(" ");
            item.setItemMeta(itemMeta);
            p.updateInventory();
            return;
        }

        if (args.length == 2 && args[1].equalsIgnoreCase("clear")) {
            itemMeta.setDisplayName(null);
            item.setItemMeta(itemMeta);
            p.updateInventory();
            return;
        }

        StringBuilder bname = new StringBuilder(args[1]);
        for (int i = 2; i < args.length; i++)
            bname.append(" ").append(args[i]);

        String name = Util.formatText(p, bname.toString(), getPermission());
        if (Util.hasBannedWords(p, name))
            return;

        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);
        p.updateInventory();
    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args) {
        if (!(sender instanceof Player))
            return Collections.emptyList();
        if (args.length != 2)
            return Collections.emptyList();
        ItemStack item = this.getItemInHand((Player) sender);
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName())
                return Util.complete(args[1], meta.getDisplayName().replace('§', '&'), "clear");
        }
        return Collections.emptyList();
    }
}
