/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorfulchew.blockrotator;

import com.colorfulchew.blockrotator.listeners.ItemUseListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Aaron
 */
public class BlockRotator extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ItemUseListener(this), this);
    }
    
    @Override
    public void onDisable() {
        
    }
}
