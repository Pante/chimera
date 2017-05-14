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


public class ItemBuilder extends Builder<ItemBuilder, ItemMeta> {
    
    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }
    
    public ItemBuilder(ItemStack item) {
        super(item);
    }
    
    
    public BannerBuilder asBanner() {
        return new BannerBuilder(this);
    }
    
    public BlockStateBuilder asBlockState() {
        return new BlockStateBuilder(this);
    }
    
    public BookBuilder asBook() {
        return new BookBuilder(this);
    }
    
    public EnchantmentStorageBuilder asEnchantmentStorage() {
        return new EnchantmentStorageBuilder(this);
    }
    
    public FireworkEffectBuilder asFireworkEffect() {
        return new FireworkEffectBuilder(this);
    }
    
    public FireworkBuilder asFirework() {
        return new FireworkBuilder(this);
    }
    
    
    public LeatherArmorBuilder asLeatherArmor() {
        return new LeatherArmorBuilder(this);
    }
    
    public MapBuilder asMap() {
        return new MapBuilder(this);
    }
    
    public PotionBuilder asPotion() {
        return new PotionBuilder(this);
    }
    
    public SkullBuilder asSkull() {
        return new SkullBuilder(this);
    }
    
    public SpawnEggBuilder asSpawnEgg() {
        return new SpawnEggBuilder(this);
    }
    

    @Override
    protected ItemBuilder getThis() {
        return this;
    }
    
}
