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
                        BlockData blockData = block.getBlockData();
                        if(blockData instanceof Directional) {
                        	Directional directional = (Directional) blockData;
                        	if  (directional.getFacing().equals(BlockFace.NORTH)){
                        		directional.setFacing(BlockFace.WEST);
                        	}else if  (directional.getFacing().equals(BlockFace.WEST)){
                        		directional.setFacing(BlockFace.EAST);
                        	}else if  (directional.getFacing().equals(BlockFace.EAST)){
                        		directional.setFacing(BlockFace.SOUTH);
                        	}else if  (directional.getFacing().equals(BlockFace.SOUTH)){
                        		directional.setFacing(BlockFace.NORTH);
                        	}
                        	
                           
                          
                            e.getClickedBlock().setBlockData(directional);
                        }else {
                            
                        }
                    }
                    // Check terracotta
                    if(block.getType().toString().contains("STAIRS")) {
                       BlockData blockData = block.getBlockData();
                        if(blockData instanceof Directional) {
                        	Directional directional = (Directional) blockData;
                        	if (directional.getFacing().equals(BlockFace.DOWN)){
                        		directional.setFacing(BlockFace.UP);
                        		
                        	}else if  (directional.getFacing().equals(BlockFace.UP)){
                        		directional.setFacing(BlockFace.NORTH);
                        	}else if  (directional.getFacing().equals(BlockFace.NORTH)){
                        		directional.setFacing(BlockFace.WEST);
                        	}else if  (directional.getFacing().equals(BlockFace.WEST)){
                        		directional.setFacing(BlockFace.EAST);
                        	}else if  (directional.getFacing().equals(BlockFace.EAST)){
                        		directional.setFacing(BlockFace.SOUTH);
                        	}else if  (directional.getFacing().equals(BlockFace.SOUTH)){
                        		directional.setFacing(BlockFace.DOWN);
                        	}
                        	
                           
                          
                            e.getClickedBlock().setBlockData(directional);
                        }else {
                            
                        }
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
