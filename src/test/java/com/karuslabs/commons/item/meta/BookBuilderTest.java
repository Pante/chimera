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

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.junit.Test;

import static org.mockito.Mockito.*;


public class BookBuilderTest {
    
    private BookBuilder builder;
    private BookMeta meta;
    
    
    public BookBuilderTest() {
        meta = mock(BookMeta.class);
        builder = new BookBuilder((ItemStack) when(mock(ItemStack.class).getItemMeta()).thenReturn(meta).getMock());
    }
    
    
    @Test
    public void build() {
        builder.title("title").author("Pante").generation(BookMeta.Generation.COPY_OF_COPY).pages("pg 1", "pg 2");
        
        verify(meta).setTitle("title");
        verify(meta).setAuthor("Pante");
        verify(meta).setGeneration(BookMeta.Generation.COPY_OF_COPY);
        verify(meta).addPage("pg 1", "pg 2");
    }
    
}
