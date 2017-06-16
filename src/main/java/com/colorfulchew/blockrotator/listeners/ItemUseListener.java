/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorfulchew.blockrotator.listeners;

import com.colorfulchew.blockrotator.BlockRotator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 *
 * @author Aaron
 */
public class ItemUseListener implements Listener {
    
    private boolean ignoreNext = false;
    public BlockRotator plugin;
    
    public ItemUseListener(BlockRotator plugin) {
        this.plugin = plugin;
    }
    
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=false)
    public void ItemInteract(PlayerInteractEvent e) {
        if(ignoreNext) return;
        // Checks player permissions
        if(e.getPlayer().hasPermission("blockrotator.rotate")) {
            if(e.getMaterial() == Material.BONE) {
                if(!canBuild(e)) return;
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
    
    private boolean canBuild(PlayerInteractEvent e) {
        ignoreNext = true;
        
        BlockPlaceEvent event = new BlockPlaceEvent(e.getClickedBlock(), null, null, null, e.getPlayer(), true, EquipmentSlot.OFF_HAND);
        try {
            Bukkit.getPluginManager().callEvent(event);
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
