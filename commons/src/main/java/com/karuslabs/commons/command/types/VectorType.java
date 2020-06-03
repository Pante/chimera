/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.List;

import org.bukkit.util.Vector;


public abstract class VectorType extends DynamicExampleType<Vector> {
    
    public static final VectorType FLAT = new Vector2DType();
    public static final VectorType CUBIC = new Vector3DType();
    
    
    private final boolean cubic;
    
    
    VectorType(List<String> examples, boolean cubic) {
        super(examples);
        this.cubic = cubic;
    }
    
    
    @Override
    public Vector parse(StringReader reader) throws CommandSyntaxException {
        var vector = new Vector();
        
        reader.skipWhitespace();
        vector.setX(reader.readDouble());
        
        if (cubic) {
            reader.skipWhitespace();
            vector.setY(reader.readDouble());
        }
        
        reader.skipWhitespace();
        vector.setZ(reader.readDouble());
        
        return vector;
    }

}


class Vector2DType extends VectorType implements Cartesian2DType<Vector> {

    Vector2DType() {
        super(List.of("0 0", "0.0 0.0"), false);
    }
    
}


class Vector3DType extends VectorType implements Cartesian3DType<Vector> {

    Vector3DType() {
        super(List.of("0 0 0", "0.0 0.0 0.0"), true);
    }
    
}
