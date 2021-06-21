/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.commons.item.builders;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A {@code ItemStack} builder.
 */
public final class ItemBuilder extends Builder<ItemMeta, ItemBuilder> {
    
    /**
     * Creates a {@code ItemBuilder} for the given material.
     * 
     * @param material the material
     * @return an {@code ItemBuilder}
     */
    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }
    
    ItemBuilder(Material material) {
        super(material);
    }
    
    /**
     * Creates a {@code BannerBuilder} for this {@code ItemStack}.
     * 
     * @return a {@code BannerBuilder}
     */
    public BannerBuilder banner() {
        return new BannerBuilder(this);
    }
    
    /**
     * Creates a {@code BlockDataBuilder} for this {@code ItemStack}.
     * 
     * @return a {@code BlockDataBuilder}
     */
    public BlockDataBuilder blockData() {
        return new BlockDataBuilder(this);
    }
     
    /**
     * Creates a {@code BlockStateBuilder} for this {@code ItemStack}.
     * 
     * @return a {@code BlockStateBuilder}
     */
    public BlockStateBuilder blockState() {
        return new BlockStateBuilder(this);
    }
    
    /**
     * Creates a {@code BookBuilder} for this {@code ItemStack}.
     * 
     * @return a {@code BookBuilder}
     */
    public BookBuilder book() {
        return new BookBuilder(this);
    }
    
    /**
     * Creates a {@code BundleBuilder} for this {@code ItemStack}.
     * 
     * @return a {@code BundleBuilder}
     */
    public BundleBuilder bundle() {
        return new BundleBuilder(this);
    }
    
    /**
     * Creates a {@code CompassBuilder} for this {@code ItemStack}.
     * 
     * @return a {@code CompassBuilder}
     */
    public CompassBuilder compass() {
        return new CompassBuilder(this);
    }
    
    /**
     * Creates a {@code CrossBowBuilder} for this {@code ItemStack}.
     * 
     * @return a {@code CrossBowBuilder}
     */
    public CrossbowBuilder crossbow() {
        return new CrossbowBuilder(this);
    }
    
    /**
     * Creates an {@code EnchantmentStorageBuilder} for this {@code ItemStack}.
     * 
     * @return an {@code EnchantmentStorageBuilder}
     */
    public EnchantmentStorageBuilder enchantmentStorage() {
        return new EnchantmentStorageBuilder(this);
    }
    
    /**
     * Creates a {@code FireworkBuilder} for this {@code ItemStack}.
     * 
     * @return a {@code FireworkBuilder}
     */
    public FireworkBuilder firework() {
        return new FireworkBuilder(this);
    }
    
    /**
     * Creates a {@code FireworkEffectBuilder} for this {@code ItemStack}.
     * 
     * @return a {@code FireworkEffectBuilder}
     */
    public FireworkEffectBuilder fireworkEffect() {
        return new FireworkEffectBuilder(this);
    }
    
    /**
     * Creates a {@code HeadBuilder} for this {@code ItemStack}.
     * 
     * @return a {@code HeadBuilder}
     */
    public HeadBuilder head() {
        return new HeadBuilder(this);
    }
    
    /**
     * Creates a {@code KnowledgeBookBuilder} for this {@code ItemStack}.
     * 
     * @return a {@code KnowledgeBookBuilder}
     */
    public KnowledgeBookBuilder knowledgeBook() {
        return new KnowledgeBookBuilder(this);
    }
    
    /**
     * Creates a {@code LeatherArmourBuilder} for this {@code ItemStack}.
     * 
     * @return a {@code LeatherArmourBuilder}
     */
    public LeatherArmourBuilder leatherArmour() {
        return new LeatherArmourBuilder(this);
    }
    
    /**
     * Creates a {@code MapBuilder} for this {@code ItemStack}.
     * 
     * @return a {@code MapBuilder}
     */
    public MapBuilder map() {
        return new MapBuilder(this);
    }
    
    /**
     * Creates a {@code PotionBuilder} for this {@code ItemStack}.
     * 
     * @return a {@code PotionBuilder}
     */
    public PotionBuilder potion() {
        return new PotionBuilder(this);
    }
    
    /**
     * Creates a {@code SuspiciousStewBuilder} for this {@code ItemStack}.
     * 
     * @return a  {@code SuspiciousStewBuilder}
     */
    public SuspiciousStewBuilder stew() {
        return new SuspiciousStewBuilder(this);
    }
    
    /**
     * Creates a {@code TropicalFishBucketBuilder} for this {@code ItemStack}.
     * 
     * @return a  {@code TropicalFishBucketBuilder}
     */
    public TropicalFishBucketBuilder tropicalFishBucket() {
        return new TropicalFishBucketBuilder(this);
    }
    
    @Override
    ItemBuilder self() {
        return this;
    }
    
}
