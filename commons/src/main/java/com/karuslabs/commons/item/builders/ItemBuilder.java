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


public class ItemBuilder extends Builder<ItemMeta, ItemBuilder> {
    
    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }
    
    ItemBuilder(Material material) {
        super(material);
    }
    
    
    /**
     * Returns a {@code BannerBuilder} for this {@code ItemStack}.
     * 
     * @return a BannerBuilder
     */
    public BannerBuilder asBanner() {
        return new BannerBuilder(this);
    }
    
    /**
     * Returns a {@code BlockStateBuilder} for this {@code ItemStack}.
     * 
     * @return a BlockStateBuilder
     */
    public BlockStateBuilder asBlockState() {
        return new BlockStateBuilder(this);
    }
    
    /**
     * Returns a {@code BookBuilder} for this {@code ItemStack}.
     * 
     * @return a BookBuilder
     */
    public BookBuilder asBook() {
        return new BookBuilder(this);
    }
    
    /**
     * Returns a {@code EnchantmentStorageBuilder} for this {@code ItemStack}.
     * 
     * @return a EnchantmentStorageBuilder
     */
    public EnchantmentStorageBuilder asEnchantmentStorage() {
        return new EnchantmentStorageBuilder(this);
    }
    
    /**
     * Returns a {@code FireworkBuilder} for this {@code ItemStack}.
     * 
     * @return a FireworkBuilder
     */
    public FireworkBuilder asFirework() {
        return new FireworkBuilder(this);
    }
    
    public FireworkEffectBuilder asFireworkEffect() {
        return new FireworkEffectBuilder(this);
    }
    
    public KnowledgeBookBuilder asKnowledgeBook() {
        return new KnowledgeBookBuilder(this);
    }
    
    /**
     * Returns a {@code LeatherArmourBuilder} for this {@code ItemStack}.
     * 
     * @return a LeatherArmourBuilder
     */
    public LeatherArmourBuilder asLeatherArmour() {
        return new LeatherArmourBuilder(this);
    }
    
    /**
     * Returns a {@code MapBuilder} for this {@code ItemStack}.
     * 
     * @return a MapBuilder
     */
    public MapBuilder asMap() {
        return new MapBuilder(this);
    }
    
    /**
     * Returns a {@code PotionBuilder} for this {@code ItemStack}.
     * 
     * @return a PotionBuilder
     */
    public PotionBuilder asPotion() {
        return new PotionBuilder(this);
    }
    
    /**
     * Returns a {@code SkullBuilder} for this {@code ItemStack}.
     * 
     * @return a SkullBuilder
     */
    public SkullBuilder asSkull() {
        return new SkullBuilder(this);
    }
    
    public TropicalFishBucketBuilder asBucket() {
        return new TropicalFishBucketBuilder(this);
    }

    
    @Override
    protected ItemBuilder self() {
        return this;
    }
    
}
