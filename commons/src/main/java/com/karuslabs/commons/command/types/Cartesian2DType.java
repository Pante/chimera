/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.commons.command.types;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.server.v1_13_R2.ArgumentVec2;

import org.bukkit.Location;


public abstract class Cartesian2DType<T> extends CartesianType<T> {
    
    static final ArgumentVec2 VECTOR_2D = new ArgumentVec2(true);
    
    
    @Override
    protected void suggest(SuggestionsBuilder builder, CommandContext<?> context, Location location, String[] parts) {
        switch (parts.length) {
            case 0:
                builder.suggest(String.valueOf(location.getX()));
                builder.suggest(location.getX() + " " + location.getZ());
                break;
                
            case 1:
                builder.suggest(parts[0] + " " + location.getZ());
                break;
        }
    }
    
    
    @Override
    public ArgumentType<?> mapped() {
        return VECTOR_2D;
    }
    
}
