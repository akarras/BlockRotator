/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorfulchew.blockrotator.listeners;

import com.colorfulchew.blockrotator.BlockRotator;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Slab;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

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

    private boolean HoldingWand(PlayerInteractEvent event){
        if (!event.hasItem())
            return false;
        ItemStack item = event.getItem();
        if (item.getType() != Material.STICK)
            return false;
        if( item.getItemMeta() == null
                || item.getItemMeta().getPersistentDataContainer() == null)
            return false;

        byte isWand = item.getItemMeta().getPersistentDataContainer().getOrDefault(plugin.CONTROL_ITEM_KEY,
                PersistentDataType.BYTE,
                (byte)0b0);
        return isWand == 0x1;
    }

    private static boolean PassesBlacklist(Block block){
        if(block.getType() == Material.WALL_TORCH)
            return false;
        if(block.getBlockData() instanceof Door)
            return false;
        // All Clear
        return true;
    }

    private static void Rotate(Directional blockData) {
        switch (blockData.getFacing()) {
            case NORTH:
                blockData.setFacing(BlockFace.EAST);
                break;
            case EAST:
                blockData.setFacing(BlockFace.SOUTH);
                break;
            case SOUTH:
                blockData.setFacing(BlockFace.WEST);
                break;
            case WEST:
                blockData.setFacing(BlockFace.NORTH);
                break;
        }
    }

    private static void Rotate(Orientable blockData) {
        switch (blockData.getAxis()) {
            case X:
                blockData.setAxis(Axis.Z);
                break;
            case Z:
                blockData.setAxis(Axis.X);
                break;
        }
    }
    private static void Flip(Orientable blockData){
        switch (blockData.getAxis()){
            case X:
            case Z:
                blockData.setAxis(Axis.Y);
                break;
            case Y:
                blockData.setAxis(Axis.X);
                break;
        }
    }

    private static void Flip(Bisected blockData){
        switch (blockData.getHalf()){
            case TOP:
                blockData.setHalf(Bisected.Half.BOTTOM);
                break;
            case BOTTOM:
                blockData.setHalf(Bisected.Half.TOP);
                break;
        }
    }
    private static void Flip(Slab blockData){
        switch (blockData.getType()){
            case TOP:
                blockData.setType(Slab.Type.BOTTOM);
                break;
            case BOTTOM:
                blockData.setType(Slab.Type.TOP);
                break;
        }
    }
    
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=false)
    public void ItemInteract(PlayerInteractEvent e) {
        if (ignoreNext) return;
        // Checks player permissions
        if (e.getPlayer().hasPermission("blockrotator.rotate")) {
            if(HoldingWand(e)){
                if (!canBuild(e)) return;
                if (e.hasBlock()) {
                    Block block = e.getClickedBlock();
                    if (PassesBlacklist(block)) {
                        BlockState blockState = block.getState();
                        BlockData blockData = blockState.getBlockData();

                        if (e.getPlayer().isSneaking()) {
                            if ((blockData instanceof Slab)) {
                                Flip((Slab) blockData);
                            } else if (blockData instanceof Bisected) {
                                Flip((Bisected) blockData);
                            } else if (blockData instanceof Orientable){
                                Flip((Orientable) blockData);
                            }
                        } else {
                            if (blockData instanceof Directional) {
                                Rotate((Directional) blockData);
                            } else if (blockData instanceof Orientable){
                                Rotate((Orientable) blockData);
                            }
                        }
                        blockState.setBlockData(blockData);
                        blockState.update();
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
