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

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.minestar.therock.Core;
import de.minestar.therock.manager.QueueManager;
import de.minestar.therock.manager.WorldManager;

public class PlayerListener implements Listener {

    private WorldManager worldManager;
    private QueueManager queueManager;
    private StringBuilder queueBuilder;

    public PlayerListener(QueueManager queueManager, WorldManager worldManager) {
        this.worldManager = worldManager;
        this.queueManager = queueManager;
        this.queueBuilder = new StringBuilder();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(PlayerChatEvent event) {
        // event cancelled => return
        if (event.isCancelled() || !this.worldManager.logChat())
            return;

        // create data
        this.queueBuilder.append("(");
        this.queueBuilder.append(System.currentTimeMillis());
        this.queueBuilder.append(", ");
        this.queueBuilder.append("'" + event.getPlayer().getName() + "'");
        this.queueBuilder.append(", ");
        String message = event.getMessage();
        message = message.replace("\\", "\\\\").replace("'", "\\'");
        this.queueBuilder.append("'" + message + "'");
        this.queueBuilder.append(")");

        // add to queue
        this.queueManager.appendChatEvent(this.queueBuilder);

        // reset data
        this.queueBuilder.setLength(0);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        // event cancelled => return
        if (event.isCancelled())
            return;

        // clicked on a block?
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getPlayer().isOp() && event.getPlayer().getItemInHand().getTypeId() == Material.WATCH.getId()) {
                event.setCancelled(true);
                Core.getInstance().getDatabaseHandler().getBlockChanges(event.getPlayer(), event.getClickedBlock().getRelative(event.getBlockFace()));
            }
        } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getPlayer().isOp() && event.getPlayer().getItemInHand().getTypeId() == Material.WATCH.getId()) {
                event.setCancelled(true);
                Core.getInstance().getDatabaseHandler().getBlockChanges(event.getPlayer(), event.getClickedBlock());
            }
        }
    }
}
