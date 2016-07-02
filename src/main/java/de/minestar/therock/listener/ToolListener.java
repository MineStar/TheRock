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

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.minestar.therock.tools.Tool;

public class ToolListener implements Listener {

    private HashMap<Material, Tool> toolList = new HashMap<Material, Tool>();

    public void addTool(Tool tool) {
        this.toolList.put(tool.getToolType(), tool);
    }

    public boolean isTool(Material tYPE) {
        return (this.toolList.get(tYPE) != null);
    }

    public Tool getTool(Material material) {
        return this.toolList.get(material);
    }

    private boolean onBlockInteract(final Player player, final Block block, final BlockFace blockFace, final boolean isLeftClick) {
        Tool tool = this.getTool(player.getItemInHand().getType());
        if (tool != null) {
            tool.onBlockInteract(player, block, blockFace, isLeftClick);
            return true;
        }
        return false;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent event) {

        // check the inventory type
        InventoryType type = event.getInventory().getType();
        if (type != InventoryType.CHEST && type != InventoryType.DROPPER && type != InventoryType.DISPENSER && type != InventoryType.FURNACE && type != InventoryType.BREWING) {
            return;
        }

        // get the current item
        ItemStack inCursor = event.getCursor();

        // fix slot < 0
        if (event.getSlot() < 0) {
            return;
        }

        // get the clicked item
        ItemStack inSlot = event.getView().getItem(event.getRawSlot());

        // get the player
        Player player = (Player) event.getWhoClicked();

        boolean cursorNull = (inCursor == null || inCursor.getType() == Material.AIR);
        boolean slotNull = (inSlot == null || inSlot.getType() == Material.AIR);

        // Cursor = null && Slot == null => nothing happens
        if (cursorNull && slotNull) {
            return;
        }

        if (!slotNull) {
            // do we have a tool?
            Material TYPE = inSlot.getType();
            if (this.isTool(TYPE)) {
                Tool tool = this.getTool(TYPE);
                if (tool.hasPermission(player)) {
                    inSlot.setAmount(0);
                    event.setCancelled(true);
                    return;
                }
            }
        }

        if (!cursorNull) {
            // do we have a tool?
            Material TYPE = inCursor.getType();
            if (this.isTool(TYPE)) {
                Tool tool = this.getTool(TYPE);
                if (tool.hasPermission(player)) {
                    inSlot.setAmount(0);
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
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
        Material TYPE = event.getItemDrop().getItemStack().getType();
        if (this.isTool(TYPE)) {
            Tool tool = this.getTool(TYPE);
            if (tool.hasPermission(event.getPlayer())) {
                //event.getItemDrop().setItemStack(new ItemStack(Material.SAND, 1));
                event.getItemDrop().remove();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        // prevent dropping of the lookup-tool
        for (Tool tool : this.toolList.values()) {
            if (tool.hasPermission(event.getEntity())) {
                for (int i = event.getDrops().size() - 1; i >= 0; i--) {
                    if (event.getDrops().get(i).getType() == tool.getToolType()) {
                        event.getDrops().remove(i);
                    }
                }
            }
        }
    }
}
