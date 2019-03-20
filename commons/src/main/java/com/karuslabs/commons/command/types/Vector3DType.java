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

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.*;

import org.bukkit.util.Vector;


/**
 * A 3D vector type that matches {@code [x coordinate] [y coordinate] [z coordinate]}. 
 */
public class Vector3DType extends Cartesian3DType<Vector> {
    
    static final Collection<String> EXAMPLES = List.of("0 0 0", "0.0 0.0 0.0");
    
    
    /**
     * Returns a 3D vector from the string returned by the given {@code StringReader}.
     * 
     * @see VectorParser#parse3DVector(StringReader) 
     * 
     * @param reader the reader
     * @return a 3D vector
     * @throws CommandSyntaxException if a 3D vector cannot be parsed from the given
     *                                string
     */
    @Override
    public Vector parse(StringReader reader) throws CommandSyntaxException {
        return VectorParser.parse3DVector(reader);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    
}
