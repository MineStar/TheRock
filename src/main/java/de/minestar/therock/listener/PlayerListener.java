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

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

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
        if (event.isCancelled() || !this.worldManager.getWorld(event.getPlayer()).logPlayerChat())
            return;

        // create data
        this.queueBuilder.append("(");
        this.queueBuilder.append("'" + event.getPlayer().getName() + "'");
        this.queueBuilder.append(", ");
        this.queueBuilder.append("'" + event.getMessage() + "'");
        this.queueBuilder.append(")");

        // add to queue
        this.queueManager.appendChatEvent(this.queueBuilder);

        // reset data
        this.queueBuilder.setLength(0);
    }
}
