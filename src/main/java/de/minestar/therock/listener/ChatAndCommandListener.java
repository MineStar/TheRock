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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import de.minestar.therock.TheRockCore;
import de.minestar.therock.data.sqlElements.MessageElement;
import de.minestar.therock.manager.MainConsumer;
import de.minestar.therock.manager.MainManager;

public class ChatAndCommandListener implements Listener {

    private MainManager mainManager;
    private MainConsumer mainConsumer;
    private StringBuilder queueBuilder;

    public ChatAndCommandListener() {
        this.mainManager = TheRockCore.mainManager;
        this.mainConsumer = TheRockCore.mainConsumer;
        this.queueBuilder = new StringBuilder();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        // event cancelled => return
        if (event.isCancelled() || !this.mainManager.logChat())
            return;

        // add to queue
        this.mainConsumer.appendChatEvent(new MessageElement(event.getPlayer().getName(), event.getMessage()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        // event cancelled => return
        if (event.isCancelled() || !this.mainManager.logCommands())
            return;

        // add to queue
        this.mainConsumer.appendCommandEvent(new MessageElement(event.getPlayer().getName(), event.getMessage()));

        // reset data
        this.queueBuilder.setLength(0);
    }

}
