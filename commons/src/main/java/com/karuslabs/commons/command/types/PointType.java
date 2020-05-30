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

import com.karuslabs.commons.util.Point;
import com.karuslabs.commons.util.Point.Axis;

import com.mojang.brigadier.*;
import com.mojang.brigadier.exceptions.*;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.List;

import static com.karuslabs.commons.util.Point.Axis.*;


/**
 * A {@code Point} type.
 * 
 * @see VectorType
 */
public abstract class PointType extends DynamicExampleType<Point> {

    /**
     * A 2D {@code Point} type.
     */
    public static final PointType FLAT = new Point2DType();
    /**
     * A 3D {@code Point} type.
     */
    public static final PointType CUBIC = new Point3DType();
    
    
    static final SimpleCommandExceptionType MIXED = new SimpleCommandExceptionType(new LiteralMessage(
        "Cannot mix world and local coordinates (everything must either use ^ or not)"
    )); 
    
    
    private final Axis[] axes;
    
    
    /**
     * Creates a {@code PointType} with the given examples and axes.
     * 
     * @param examples the examples
     * @param axes the axes that this type supports
     */
    PointType(List<String> examples, Axis... axes) {
        super(examples);
        this.axes = axes;
    }
    
    
    @Override
    public Point parse(StringReader reader) throws CommandSyntaxException {
        var point = new Point();
        
        if (reader.peek() == '^') {
            point.rotation(true);
        }
        
        for (var axis : axes) {
            parse(reader, point, axis);
        }
        
        return point;
    }
    
    /**
     * Parses a value from {@code reader} and sets it on the given axis of {@code point}.
     * 
     * @param reader the reader
     * @param point the point
     * @param axis the axis
     * @throws CommandSyntaxException if the input was invalid
     */
    void parse(StringReader reader, Point point, Axis axis) throws CommandSyntaxException {
        reader.skipWhitespace();
        
        if (point.rotation() ^ (reader.peek() == '^')) {
            throw MIXED.createWithContext(reader);
        }
        
        if (reader.peek() == '^') {
            reader.skip();
            
        } else if (reader.peek() == '~') {
            reader.skip();
            point.relative(axis, true);
        }
        
        point.set(axis, reader.readDouble());  
    }
    
}



/**
 * A 2D {@code Point} type.
 */
class Point2DType extends PointType implements Cartesian2DType<Point> {

    Point2DType() {
        super(List.of("0 0", "0.0 0.0", "^ ^", "~ ~"), X, Z);
    }
    
    
    @Override
    public void suggest(SuggestionsBuilder builder, String[] parts) {
        if (builder.getRemaining().isBlank()) {
            builder.suggest("~").suggest("~ ~");
            
        } else if (parts.length == 1) {
            var prefix = builder.getRemaining().charAt(0) == '^' ? '^' : '~';
            builder.suggest(parts[0] + " " + prefix);
        }
    }
    
}


/**
 * A 3D {@code Point} type.
 */
class Point3DType extends PointType implements Cartesian2DType<Point> {

    Point3DType() {
        super(List.of("0 0 0", "0.0 0.0 0.0", "^ ^ ^", "~ ~ ~"), X, Y, Z);
    }
    
    
    @Override
    public void suggest(SuggestionsBuilder builder, String[] parts) {
        if (builder.getRemaining().isBlank()) {
            builder.suggest("~").suggest("~ ~").suggest("~ ~ ~");
            return;
        }
        
        var prefix = builder.getRemaining().charAt(0) == '^' ? '^' : '~';
        if (parts.length == 1) {
            builder.suggest(parts[0] + " " + prefix)
                   .suggest(parts[0] + " " + prefix + " " + prefix);
            
        } else if (parts.length == 2) {
            builder.suggest(parts[0] + " " + parts[1] + " " + prefix);
        }
    }

}