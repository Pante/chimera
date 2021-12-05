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

import com.karuslabs.annotations.Lazy;

import java.util.*;

import org.bukkit.*;
import org.bukkit.attribute.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A {@code ItemStack} builder.
 * 
 * @param <Meta> the type of the ItemMeta
 * @param <Self> {@code this}
 */
public abstract sealed class Builder<Meta extends ItemMeta, Self extends Builder> permits 
    AxolotlBucketBuilder, BannerBuilder, BlockDataBuilder, BlockStateBuilder, 
    BookBuilder, BundleBuilder, CompassBuilder, CrossbowBuilder, EnchantmentStorageBuilder, 
    FireworkBuilder, FireworkEffectBuilder, HeadBuilder, ItemBuilder,
    KnowledgeBookBuilder, LeatherArmourBuilder, MapBuilder, PotionBuilder,
    SuspiciousStewBuilder, TropicalFishBucketBuilder {
    
    final ItemStack item;
    final Meta meta;
    @Lazy List<String> lore;
    
    Builder(Material material) {
        item = new ItemStack(material);
        meta = (Meta) item.getItemMeta();
    }
    
    Builder(Builder<ItemMeta, ?> source) {
        item = source.item;
        meta = (Meta) source.meta;
    }
    
    /**
     * Sets the amount.
     * 
     * @param amount the amount
     * @return {@code this}
     */
    public Self amount(int amount) {
        item.setAmount(amount);
        return self();
    }
    
    /**
     * Sets the damage.
     * 
     * @param damage the damage
     * @return {@code this}
     */
    public Self damage(int damage) {
        ((Damageable) meta).setDamage(damage);
        return self();
    }
    
    /**
     * Sets the display name.
     * 
     * @param name the display name
     * @return {@code this}
     */
    public Self display(@Nullable String name) {
        meta.setDisplayName(name);
        return self();
    }
    
    /**
     * Sets the localised name.
     * 
     * @param name the name
     * @return {@code this}
     */
    public Self localised(@Nullable String name) {
        meta.setLocalizedName(name);
        return self();
    }
    
    /**
     * Adds a modifier to the given attribute.
     * 
     * @param attribute the attribute
     * @param modifier the attribute modifier
     * @return {@code this}
     */
    public Self attribute(Attribute attribute, AttributeModifier modifier) {
        meta.addAttributeModifier(attribute, modifier);
        return self();
    }
    
    /**
     * Set the custom model data.
     * 
     * @param data the custom model data
     * @return {@code this}
     */
    public Self model(@Nullable Integer data) {
        meta.setCustomModelData(data);
        return self();
    }
    
    /**
     * Sets the enchantment.
     * 
     * @param enchantment the enchantment
     * @param level the enchantment level
     * @return {@code this}
     */
    public Self enchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return self();
    }
    
    /**
     * Adds the given flags.
     * 
     * @param flags the flags
     * @return {@code this}
     */
    public Self flags(Collection<ItemFlag> flags) {
        return flags(flags.toArray(new ItemFlag[0]));
    }
    
    /**
     * Adds the given flags.
     * 
     * @param flags the flags
     * @return {@code this}
     */
    public Self flags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return self();
    }
    
    /**
     * Adds the given lore.
     * 
     * @param lines the lore
     * @return {@code this}
     */
    public Self lore(Collection<String> lines) {
        if (lore == null) {
            lore = new ArrayList<>(lines);
            
        } else {
           lore.addAll(lines);
        }
        return self();
    }
    
    /**
     * Adds the given lore.
     * 
     * @param lines the lore
     * @return {@code this}
     */
    public Self lore(String... lines) {
        if (lore == null) {
            lore = new ArrayList<>(lines.length);
        }
        
        lore.addAll(List.of(lines));

        return self();
    }
    
    /**
     * Sets the breakability.
     * 
     * @param unbreakable the breakability
     * @return {@code this}
     */
    public Self unbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return self();
    }
    
    /**
     * Builds an {@code ItemStack}.
     * 
     * @return the {@code ItemStack}
     */
    public ItemStack build() {
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    
    
    abstract Self self();
    
}
