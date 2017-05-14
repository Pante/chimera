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
package com.karuslabs.commons.item.meta;

import com.karuslabs.commons.item.Builder;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;


public class BookBuilder extends Builder<BookBuilder, BookMeta> {
    
    public BookBuilder(ItemStack item) {
        super(item);
    }
    
    public BookBuilder(Builder builder) {
        super(builder);
    }
    
    
    public BookBuilder title(String title) {
        meta.setTitle(title);
        return this;
    }
    
    public BookBuilder author(String author) {
        meta.setAuthor(author);
        return this;
    }
    
    public BookBuilder pages(String... pages) {
        meta.addPage(pages);
        return this;
    }
    
    public BookBuilder generation(BookMeta.Generation generation) {
        meta.setGeneration(generation);
        return this;
    }

    
    @Override
    protected BookBuilder getThis() {
        return this;
    }
    
}
