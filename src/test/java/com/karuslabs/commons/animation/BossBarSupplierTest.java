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
package com.karuslabs.commons.animation;

import org.bukkit.Server;
import org.bukkit.boss.*;

import org.junit.jupiter.api.Test;

import static com.karuslabs.commons.animation.BossBarSupplier.builder;
import static org.bukkit.boss.BarColor.BLUE;
import static org.bukkit.boss.BarStyle.SEGMENTED_10;
import static org.mockito.Mockito.*;


class BossBarSupplierTest {
    
    @Test
    void get() {
        Server server = when(mock(Server.class).createBossBar(any(), any(), any(), any())).thenReturn(mock(BossBar.class)).getMock();
        BossBar bar = builder(server).message("message").colour(BLUE).style(SEGMENTED_10).progress(0.5).flags().build().get();
        
        verify(server).createBossBar("message", BLUE, SEGMENTED_10);
        verify(bar).setProgress(0.5);
    }
    
}
