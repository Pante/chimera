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

import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.*;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.meta.*;

import static org.mockito.Mockito.*;


class StubBukkit {
    
    private static final Field SERVER;
    
    static {
        try {
            SERVER = Bukkit.class.getDeclaredField("server");
            SERVER.setAccessible(true);
            
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    public static <T extends ItemMeta> T meta(Class<T> type) {
        try {
            T meta = mock(type, withSettings().extraInterfaces(Damageable.class));
            ItemFactory factory = when(mock(ItemFactory.class).getItemMeta(any())).thenReturn(meta).getMock();
            Server server = when(mock(Server.class).getItemFactory()).thenReturn(factory).getMock();
            
            SERVER.set(null, server);
            return meta;
            
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static OfflinePlayer offline() {
        try {
            var player = mock(OfflinePlayer.class);
            Server server = when(mock(Server.class).getOfflinePlayer(any(UUID.class))).thenReturn(player).getMock();
            SERVER.set(null, server);
            return player;
            
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
    
}
