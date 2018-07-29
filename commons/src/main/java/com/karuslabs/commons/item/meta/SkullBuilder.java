/* 
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.item.meta;

import com.karuslabs.commons.item.Builder;

import java.util.UUID;
import javax.annotation.Nonnull;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import static org.bukkit.Bukkit.getOfflinePlayer;


public class SkullBuilder extends Builder<SkullBuilder, SkullMeta> {
    
    public SkullBuilder(ItemStack item) {
        super(item);
    }
    
    public SkullBuilder(Builder builder) {
        super(builder);
    }
    
    
    public SkullBuilder owner(Head head) {
        return owner(head.getID());
    }
    
    public SkullBuilder owner(UUID id) {
        return owner(getOfflinePlayer(id));
    }
    
    public SkullBuilder owner(OfflinePlayer player) {
        meta.setOwningPlayer(player);
        return this;
    }
    
    
    @Deprecated
    public SkullBuilder owner(String name) {
        meta.setOwner(name);
        return this;
    }
    
    
    @Override
    protected @Nonnull SkullBuilder self() {
        return this;
    }
    
}
