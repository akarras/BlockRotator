/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorfulchew.blockrotator.listeners;

import com.colorfulchew.blockrotator.BlockRotator;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author Aaron
 */
public class ItemUseListener implements Listener {
    
    private boolean ignoreNext = true;
    public BlockRotator plugin;
    
    public ItemUseListener(BlockRotator plugin) {
        this.plugin = plugin;
    }
    
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=false)
    public void ItemInteract(PlayerInteractEvent e) {
        if(ignoreNext) return;
        if(e.getPlayer().hasPermission("blockrotator.rotate")) {
            if(e.getMaterial() == Material.BONE) {
                if(e.hasBlock()) {
                    Block block = e.getClickedBlock();
                    // Check terracotta
                    if(block.getType().toString().contains("TERRACOTTA")) {
                        byte blockData = block.getData();
                        // Wrap block data back to 0
                        if(blockData == 3) {
                            blockData = 0;
                        } else {
                            blockData++;
                        }
                        block.setData(blockData);
                    }
                    // Check terracotta
                    if(block.getType().toString().contains("STAIRS")) {
                        byte blockData = block.getData();
                        // Check if the player is sneaking
                        if(e.getPlayer().isSneaking()) {
                            // Flip the stairs upside down
                            if(blockData > 3) {
                                blockData = (byte)(blockData - 4);
                            } else {
                                blockData = (byte)(blockData + 4);
                            }
                        } else {
                            // Rotate
                            if(blockData % 4 == 3) {
                                blockData = (byte)((int)(blockData / 4) * 4);
                            } else {
                                blockData++;
                            }
                        }
                        block.setData(blockData);
                    }
                }
            }   
        }
    }
    
    /**
     * Checks if the player is able to interact with that block
     * @param e Interact event for the block that the player is attempting to manipulate
     * @return true if the player can interact, false if the player cannot.
     */
    private boolean canInteract(PlayerInteractEvent e) {
        ignoreNext = true;
        ArrayList<Event> events = new ArrayList<>();
        PlayerInteractEvent event = new PlayerInteractEvent(e.getPlayer(), e.getAction(), e.getItem(), e.getClickedBlock(), e.getBlockFace());
        try {
            plugin.getServer().getPluginManager().callEvent(event);
        } catch (IllegalStateException ex) {
            ignoreNext = false;
            return false;
        }
        if(event.isCancelled()) {
            ignoreNext = false;
            return false;
        }
        ignoreNext = false;
        return true;
    }
}
