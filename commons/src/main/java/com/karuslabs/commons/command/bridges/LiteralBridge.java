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
package com.karuslabs.commons.command.bridges;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.server.v1_13_R2.CommandListenerWrapper;


public class LiteralBridge extends Bridge {
    
    public static LiteralBuilder literal(String name) {
        return new LiteralBuilder(new LiteralBridge(), LiteralArgumentBuilder.<CommandListenerWrapper>literal(name));
    }
    
    
    public static class LiteralBuilder extends Builder<LiteralBuilder, LiteralBridge, LiteralArgumentBuilder<CommandListenerWrapper>> {

        private LiteralBuilder(LiteralBridge bridge, LiteralArgumentBuilder builder) {
            super(bridge, builder);
        }

        @Override
        protected LiteralBuilder self() {
            return this;
        }
        
    }
    
}
