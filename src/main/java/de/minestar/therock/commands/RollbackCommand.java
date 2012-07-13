package de.minestar.therock.commands;

import java.sql.ResultSet;
import java.util.HashSet;

import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;
import de.minestar.therock.Core;
import de.minestar.therock.data.BlockVector;
import de.minestar.therock.data.CacheElement;

public class RollbackCommand extends AbstractCommand {

    public RollbackCommand(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Rollback the selected action.";
    }

    public void execute(String[] args, Player player) {
        // Validate cache
        CacheElement cache = Core.getInstance().getCacheHolder().getCacheElement(player.getName());
        if (cache == null) {
            PlayerUtils.sendError(player, Core.NAME, "You must specify the rollback first.");
            PlayerUtils.sendInfo(player, "NOTE: Use /tr selection [Player] [time]");
            return;
        }

        try {
            ResultSet results = cache.getResults();
            HashSet<BlockVector> blockList = new HashSet<BlockVector>(512);
            BlockVector newVector;

            // create blocklist
            while (results.next()) {
                newVector = new BlockVector(cache.getWorld().getName(), results.getInt("blockX"), results.getInt("blockY"), results.getInt("blockZ"));
                newVector.setTypeID(results.getInt("fromID"));
                newVector.setSubData((byte) results.getInt("fromData"));
                blockList.add(newVector);
            }

            // rollback blocks
            for (BlockVector vector : blockList) {
                vector.getLocation().getBlock().setTypeIdAndData(vector.getTypeID(), vector.getSubData(), false);
            }

            PlayerUtils.sendSuccess(player, Core.NAME, "Rollback finished. ( " + blockList.size() + " Blocks)");
            blockList.clear();
            blockList = null;
            Core.getInstance().getCacheHolder().clearCacheElement(player.getName());
        } catch (Exception e) {
            PlayerUtils.sendError(player, Core.NAME, "Oooops.. something went wrong!");
            e.printStackTrace();
        }
    }
}