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

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;


/**
 * Represents a builder for items with {@code BookMeta}.
 */
public class BookBuilder extends Builder<BookBuilder, BookMeta> {
    
    /**
     * Constructs a {@code BookBuilder} for the specified {@code ItemStack}.
     *  
     * @param item the ItemStack
     */
    public BookBuilder(ItemStack item) {
        super(item);
    }
    
    /**
     * Constructs a {@code BookBuilder} which copies the specified {@code Builder}.
     *  
     * @param builder the Builder to be copied
     */
    public BookBuilder(Builder builder) {
        super(builder);
    }
    
    
    /**
     * Sets the title.
     * 
     * @param title the title
     * @return this
     */
    public BookBuilder title(String title) {
        meta.setTitle(title);
        return this;
    }
    
    /**
     * Sets the author.
     * 
     * @param author the author
     * @return this
     */
    public BookBuilder author(String author) {
        meta.setAuthor(author);
        return this;
    }
    
    /**
     * Adds the specified pages.
     * 
     * @param pages the pages
     * @return this
     */
    public BookBuilder pages(String... pages) {
        meta.addPage(pages);
        return this;
    }
    
    /**
     * Sets the generation level.
     * 
     * @param generation the generation
     * @return this
     */
    public BookBuilder generation(Generation generation) {
        meta.setGeneration(generation);
        return this;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    protected @Nonnull BookBuilder getThis() {
        return this;
    }
    
}
