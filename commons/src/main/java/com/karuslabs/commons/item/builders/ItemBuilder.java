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

public final class ItemBuilder extends Builder<ItemMeta, ItemBuilder> {
    
    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }
    
    ItemBuilder(Material material) {
        super(material);
    }
    
    
    public BannerBuilder banner() {
        return new BannerBuilder(this);
    }
    
    public BlockDataBuilder blockData() {
        return new BlockDataBuilder(this);
    }
     
    public BlockStateBuilder blockState() {
        return new BlockStateBuilder(this);
    }
    
    public BookBuilder book() {
        return new BookBuilder(this);
    }
    
    public CompassBuilder compass() {
        return new CompassBuilder(this);
    }
    
    public CrossbowBuilder crossbow() {
        return new CrossbowBuilder(this);
    }
    
    public EnchantmentStorageBuilder enchantmentStorage() {
        return new EnchantmentStorageBuilder(this);
    }
    
    public FireworkBuilder firework() {
        return new FireworkBuilder(this);
    }
    
    public FireworkEffectBuilder fireworkEffect() {
        return new FireworkEffectBuilder(this);
    }
    
    public HeadBuilder head() {
        return new HeadBuilder(this);
    }
    
    public KnowledgeBookBuilder knowledgeBook() {
        return new KnowledgeBookBuilder(this);
    }
    
    public LeatherArmourBuilder leatherArmour() {
        return new LeatherArmourBuilder(this);
    }
    
    public MapBuilder map() {
        return new MapBuilder(this);
    }
    
    public PotionBuilder potion() {
        return new PotionBuilder(this);
    }
    
    public SuspiciousStewBuilder stew() {
        return new SuspiciousStewBuilder(this);
    }
    
    public TropicalFishBucketBuilder tropicalFishBucket() {
        return new TropicalFishBucketBuilder(this);
    }
    
    @Override
    ItemBuilder self() {
        return this;
    }
    
}
