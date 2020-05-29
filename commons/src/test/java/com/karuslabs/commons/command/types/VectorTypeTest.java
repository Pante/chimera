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

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;


class VectorTypeTest {
    
    @ParameterizedTest
    @CsvSource({"'   -1.4  3.6', -1.4, 3.6", "' -1.4  3.6   2.7', -1.4, 3.6"})
    void parse_2D_vector(String line, double x, double z) throws CommandSyntaxException {
        var vector = VectorType.FLAT.parse(new StringReader(line));
        
        assertEquals(x, vector.getX(), 0);
        assertEquals(0, vector.getY(), 0);
        assertEquals(z, vector.getZ(), 0);
    }
    
    
    @ParameterizedTest
    @CsvSource({"' -1.4  3.6   2.7', -1.4, 3.6, 2.7"})
    void parse_3D_vector(String line, double x, double y, double z) throws CommandSyntaxException {
        var vector = VectorType.CUBIC.parse(new StringReader(line));
        
        assertEquals(x, vector.getX(), 0);
        assertEquals(y, vector.getY(), 0);
        assertEquals(z, vector.getZ(), 0);
    }
    
    
    @Test
    void getExamples_2D() {
        assertEquals(List.of("0 0", "0.0 0.0"), VectorType.FLAT.getExamples());
    }
    
    
    @Test
    void getExamples_3D() {
        assertEquals(List.of("0 0 0", "0.0 0.0 0.0"), VectorType.CUBIC.getExamples());
    }

} 
