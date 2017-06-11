/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.item;

import com.karuslabs.commons.item.meta.*;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


/**
 * Represents a builder for items with <code>ItemMeta</code>.
 * <p>
 * This class contains methods which copy this and construct a new <code>Builder</code> for a specific <code>ItemMeta</code> subclass.
 * Each of the aforementioned methods will first cast this <code>ItemMeta</code> to one of its subclasses. It is the callee's responsibility
 * to ensure that this <code>ItemMeta</code> may be casted to the specific subclass.
 */
public class ItemBuilder extends Builder<ItemBuilder, ItemMeta> {
    
    /**
     * Constructs a <code>ItemBuilder</code> with the specified material.
     * 
     * @param material the material of the item
     */
    public ItemBuilder(Material material) {
        super(material);
    }
    
    /**
     * Constructs a <code>ItemBuilder</code> with the specified <code>ItemStack</code>.
     * 
     * @param item the ItemStack to build
     */
    public ItemBuilder(ItemStack item) {
        super(item);
    }
    
    
    /**
     * Copies this and constructs a new <code>BannerBuilder</code>.
     * 
     * @see com.karuslabs.commons.item.meta.BannerBuilder
     * 
     * @return a BannerBuilder
     */
    public BannerBuilder asBanner() {
        return new BannerBuilder(this);
    }
    
    /**
     * Copies this and constructs a new <code>BlockStateBuilder</code>.
     * 
     * @see com.karuslabs.commons.item.meta.BlockStateBuilder
     * 
     * @return a BlockStateBuilder
     */
    public BlockStateBuilder asBlockState() {
        return new BlockStateBuilder(this);
    }
    
    /**
     * Copies this and constructs a new <code>BookBuilder</code>.
     * 
     * @see com.karuslabs.commons.item.meta.BookBuilder
     * 
     * @return a BookBuilder
     */
    public BookBuilder asBook() {
        return new BookBuilder(this);
    }
    
    /**
     * Copies this and constructs a new <code>EnchantmentStorageBuilder</code>.
     * 
     * @see com.karuslabs.commons.item.meta.EnchantmentStorageBuilder
     * 
     * @return a EnchantmentStorageBuilder
     */
    public EnchantmentStorageBuilder asEnchantmentStorage() {
        return new EnchantmentStorageBuilder(this);
    }
  
    /**
     * Copies this and constructs a new <code>FireworkEffectBuilder</code>.
     * 
     * @see com.karuslabs.commons.item.meta.FireworkEffectBuilder
     * 
     * @return a FireworkEffectBuilder
     */
    public FireworkEffectBuilder asFireworkEffect() {
        return new FireworkEffectBuilder(this);
    }
    
    /**
     * Copies this and constructs a new <code>FireworkBuilder</code>.
     * 
     * @see com.karuslabs.commons.item.meta.FireworkBuilder
     * 
     * @return a FireworkBuilder
     */
    public FireworkBuilder asFirework() {
        return new FireworkBuilder(this);
    }
    
    /**
     * Copies this and constructs a new <code>LeatherArmorBuilder</code>.
     * 
     * @see com.karuslabs.commons.item.meta.LeatherArmorBuilder
     * 
     * @return a LeatherArmorBuilder
     */
    public LeatherArmorBuilder asLeatherArmor() {
        return new LeatherArmorBuilder(this);
    }
    
    /**
     * Copies this and constructs a new <code>MapBuilder</code>.
     * 
     * @see com.karuslabs.commons.item.meta.MapBuilder
     * 
     * @return a MapBuilder
     */
    public MapBuilder asMap() {
        return new MapBuilder(this);
    }
    
    /**
     * Copies this and constructs a new <code>PotionBuilder</code>.
     * 
     * @see com.karuslabs.commons.item.meta.PotionBuilder
     * 
     * @return a PotionBuilder
     */
    public PotionBuilder asPotion() {
        return new PotionBuilder(this);
    }
    
    /**
     * Copies this and constructs a new <code>SkullBuilder</code>.
     * 
     * @see com.karuslabs.commons.item.meta.SkullBuilder
     * 
     * @return a SkullBuilder
     */
    public SkullBuilder asSkull() {
        return new SkullBuilder(this);
    }
    
    /**
     * Copies this and constructs a new <code>SpawnEggBuilder</code>.
     * 
     * @see com.karuslabs.commons.item.meta.SpawnEggBuilder
     * 
     * @return a SpawnEggBuilder
     */
    public SpawnEggBuilder asSpawnEgg() {
        return new SpawnEggBuilder(this);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected ItemBuilder getThis() {
        return this;
    }
    
}
