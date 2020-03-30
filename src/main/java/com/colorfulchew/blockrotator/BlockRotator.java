/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorfulchew.blockrotator;

import com.colorfulchew.blockrotator.listeners.ItemUseListener;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Array;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 *
 * @author Aaron
 */
public class BlockRotator extends JavaPlugin {

    public static Logger log;

    public static NamespacedKey CONTROL_ITEM_KEY;

    public static ItemStack rotationWandItem;

    public ShapedRecipe rotationWand;

    @Override
    public void onEnable() {
        CONTROL_ITEM_KEY = new NamespacedKey(this,"rotator_wand");

        rotationWandItem = new ItemStack(Material.STICK);
        ItemMeta wandMeta = rotationWandItem.getItemMeta();
        wandMeta.setLore(Arrays.asList(new String[]{"Block Rotator"}));
        wandMeta.getPersistentDataContainer()
                .set(CONTROL_ITEM_KEY, PersistentDataType.BYTE, (byte)0x1);
        rotationWandItem.setItemMeta(wandMeta);

        RecipeChoice.MaterialChoice allGlazed = new RecipeChoice.MaterialChoice(Arrays.asList(new Material[] {
                Material.BLACK_GLAZED_TERRACOTTA,
                Material.GRAY_GLAZED_TERRACOTTA,
                Material.LIGHT_GRAY_GLAZED_TERRACOTTA,
                Material.BLUE_GLAZED_TERRACOTTA,
                Material.LIGHT_BLUE_GLAZED_TERRACOTTA,
                Material.CYAN_GLAZED_TERRACOTTA,
                Material.LIME_GLAZED_TERRACOTTA,
                Material.GREEN_GLAZED_TERRACOTTA,
                Material.BROWN_GLAZED_TERRACOTTA,
                Material.MAGENTA_GLAZED_TERRACOTTA,
                Material.ORANGE_GLAZED_TERRACOTTA,
                Material.PINK_GLAZED_TERRACOTTA,
                Material.PURPLE_GLAZED_TERRACOTTA,
                Material.RED_GLAZED_TERRACOTTA,
                Material.WHITE_GLAZED_TERRACOTTA,
                Material.YELLOW_GLAZED_TERRACOTTA}));

        rotationWand = new ShapedRecipe(CONTROL_ITEM_KEY, rotationWandItem);
        rotationWand.shape(" g","I ");
        
        rotationWand.setIngredient('I', rotationWandItem.getType());
        rotationWand.setIngredient('g', allGlazed);
        rotationWand.setIngredient(' ', Material.AIR);

        getServer().addRecipe(rotationWand);

        log = this.getLogger();
        getServer().getPluginManager().registerEvents(new ItemUseListener(this), this);

    }
    
    @Override
    public void onDisable() {}
}
