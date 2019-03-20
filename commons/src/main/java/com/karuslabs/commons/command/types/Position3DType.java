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

import com.karuslabs.commons.command.types.parsers.VectorParser;
import com.karuslabs.commons.util.Position;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.*;


/**
 * A 3D position type that matches {@code (^|~)[x coordinate] (^|~)[y coordinate] (^|~)[z coordinate]}.
 * <ul>
 * <li>{@code ^} denotes relativity to the direction in which the source is facing</li>
 * <li>{@code ~} denotes absolute relativity to the location of the source</li> 
 * </ul>
 */
public class Position3DType extends Cartesian3DType<Position> {
    
    static final Collection<String> EXAMPLES = List.of("0 0 0", "0.0 0.0 0.0", "^ ^ ^", "~ ~ ~");
    
    
    /**
     * Returns a 3D position from the string returned by the given {@code StringReader}.
     * 
     * @see VectorParser#parse3DPosition(StringReader)
     * 
     * @param reader the reader
     * @return a 2D vector
     * @throws CommandSyntaxException if a 3D position cannot be parsed from the given
     *                                string
     */
    @Override
    public Position parse(StringReader reader) throws CommandSyntaxException {
        return VectorParser.parse3DPosition(reader);
    }
    
    
    /**
     * Suggests the remaining relativity prefixes based on the number and relativity 
     * of the already entered coordinates contained in the given parts.
     * 
     * @param builder the builder
     * @param context the context
     * @param parts the parts of the argument split by a whitespace
     */
    @Override
    protected void suggest(SuggestionsBuilder builder, CommandContext<?> context, String[] parts) {
        if (builder.remaining.isEmpty()) {
            builder.suggest("~").suggest("~ ~").suggest("~ ~ ~");
            return;
        }
        
        var prefix = builder.remaining.charAt(0) == '^' ? '^' : '~';
        if (parts.length == 1) {
            builder.suggest(parts[0] + " " + prefix)
                   .suggest(parts[0] + " " + prefix + " " + prefix);
            
        } else if (parts.length == 2) {
            builder.suggest(parts[0] + " " + parts[1] + " " + prefix);
        }
    }
    

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
    
}
