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

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.meta.BookMeta.Generation;


/**
 * A book builder.
 */
public class BookBuilder extends Builder<BookMeta, BookBuilder> {
    
    private static final String[] EMPTY = new String[0];
    
    
    /**
     * Creates a {@code BookBuilder} for the given material.
     * 
     * @param material the material
     * @return a {@code BookBuilder}
     */
    public static BookBuilder of(Material material) {
        return new BookBuilder(material);
    }
    
    BookBuilder(Material material) {
        super(material);
    }
    
    BookBuilder(Builder<ItemMeta, ?> source) {
        super(source);
    }
    
    
    /**
     * Sets the author.
     * 
     * @param name the author
     * @return {@code this}
     */
    public BookBuilder author(String name) {
        meta.setAuthor(name);
        return this;
    }
    
    /**
     * Sets the generation.
     * 
     * @param generation the generation
     * @return {@code this}
     */
    public BookBuilder generation(Generation generation) {
        meta.setGeneration(generation);
        return this;
    }
    
    
    /**
     * Adds the given pages.
     * 
     * @param pages the pages
     * @return {@code this}
     */
    public BookBuilder pages(List<String> pages) {
        return pages(pages.toArray(EMPTY));
    }
    
    /**
     * Adds the given pages.
     * 
     * @param pages the pages
     * @return {@code this}
     */
    public BookBuilder pages(String... pages) {
        meta.addPage(pages);
        return this;
    }
    
    
    /**
     * Sets the title.
     * 
     * @param title the title
     * @return {@code this}
     */
    public BookBuilder title(String title) {
        meta.setTitle(title);
        return this;
    }
    

    @Override
    protected BookBuilder self() {
        return this;
    }
    
}
