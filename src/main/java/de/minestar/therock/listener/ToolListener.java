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
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.minestar.therock.Core;
import de.minestar.therock.manager.MainManager;

public class ToolListener implements Listener {

    private MainManager mainManager;

    public ToolListener(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        // event cancelled => return
        if (event.isCancelled())
            return;

        // do we have the Lookup-Tool?
        if (event.getPlayer().getItemInHand().getTypeId() == this.mainManager.getToolLookupID()) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getPlayer().isOp()) {
                    event.setCancelled(true);
                    Core.getInstance().getDatabaseHandler().getBlockChanges(event.getPlayer(), event.getClickedBlock().getRelative(event.getBlockFace()));
                }
            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (event.getPlayer().isOp()) {
                    event.setCancelled(true);
                    Core.getInstance().getDatabaseHandler().getBlockChanges(event.getPlayer(), event.getClickedBlock());
                }
            }
        }
    }
}
