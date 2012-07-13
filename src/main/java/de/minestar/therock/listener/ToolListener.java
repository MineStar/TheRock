/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of TheRock.
 * 
 * TheRock is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * TheRock is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with TheRock.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.therock.listener;

import java.util.HashMap;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.minestar.minestarlibrary.utils.PlayerUtils;
import de.minestar.therock.Core;
import de.minestar.therock.tools.Tool;

public class ToolListener implements Listener {

    private HashMap<Integer, Tool> toolList = new HashMap<Integer, Tool>();

    public void addTool(Tool tool) {
        this.toolList.put(tool.getToolID(), tool);
    }

    public boolean isTool(int ID) {
        return (this.toolList.get(ID) != null);
    }

    public Tool getTool(int ID) {
        return this.toolList.get(ID);
    }

    private boolean onBlockInteract(final Player player, final Block block, final BlockFace blockFace, final boolean isLeftClick) {
        Tool tool = this.toolList.get(player.getItemInHand().getTypeId());
        if (tool != null) {
            tool.onBlockInteract(player, block, blockFace, isLeftClick);
            return true;
        }
        return false;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        // event cancelled => return
        if (event.isCancelled())
            return;

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (this.onBlockInteract(event.getPlayer(), event.getClickedBlock(), event.getBlockFace(), true)) {
                event.setCancelled(true);
                return;
            }
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (this.onBlockInteract(event.getPlayer(), event.getClickedBlock(), event.getBlockFace(), false)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemDrop(PlayerDropItemEvent event) {
        // event cancelled => return
        if (event.isCancelled())
            return;

        // do we have a tool?
        int ID = event.getItemDrop().getItemStack().getTypeId();
        if (this.isTool(ID)) {
            Tool tool = this.getTool(ID);
            if (tool.hasPermission(event.getPlayer())) {
                PlayerUtils.sendError(event.getPlayer(), Core.NAME, "You cannot drop the " + tool.getToolName() + "-tool!");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        // prevent dropping of the lookup-tool
        for (Tool tool : this.toolList.values()) {
            if (tool.hasPermission(event.getEntity())) {
                for (int i = event.getDrops().size() - 1; i >= 0; i--) {
                    if (event.getDrops().get(i).getTypeId() == tool.getToolID()) {
                        event.getDrops().remove(i);
                    }
                }
            }
        }
    }
}
