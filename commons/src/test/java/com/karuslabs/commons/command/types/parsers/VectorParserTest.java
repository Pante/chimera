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
package com.karuslabs.commons.command.types.parsers;

import com.karuslabs.commons.util.Position;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.logging.Logger;
import java.util.stream.Stream;

import org.bukkit.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class VectorParserTest {
    
    @Test
    void parseWorld() throws CommandSyntaxException {
        World world = mock(World.class);
        Logger logger = mock(Logger.class);
        
        Server server = when(mock(Server.class).getWorld("world_name")).thenReturn(world).getMock();
        when(server.getLogger()).thenReturn(logger);
        
        Bukkit.setServer(server);
        
        assertEquals(world, VectorParser.parseWorld(new StringReader("world_name")));
    }
    
    
    @Test
    void parseWorld_throws_exception() throws CommandSyntaxException {
        assertEquals(
            "Unknown world:name at position 4: name<--[HERE]", 
            assertThrows(CommandSyntaxException.class, () -> VectorParser.parseWorld(new StringReader("name"))).getMessage()
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("parse2DPosition_parameters")
    void parse2DPosition(String line, Position expected) throws CommandSyntaxException {
        assertEquals(expected, VectorParser.parse2DPosition(new StringReader(line)));
    }
    
    static Stream<Arguments> parse2DPosition_parameters() {
        return Stream.of(
            of("^1.0  ^2.0", new Position(1, 0, 2).rotate(true)),
            of("1.0  ~2.0", new Position(1, 0, 2).relative(Position.Z, true))
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("parse3DPosition_parameters")
    void parse3DPosition(String line, Position expected) throws CommandSyntaxException {
        assertEquals(expected, VectorParser.parse3DPosition(new StringReader(line)));
    }
    
    static Stream<Arguments> parse3DPosition_parameters() {
        return Stream.of(
            of("^1.0  ^2.0  ^3.0", new Position(1, 2, 3).rotate(true)),
            of(" 1.0  ~2.0   3.0", new Position(1, 2, 3).relative(Position.Y, true))
        );
    }
    
    
    @ParameterizedTest
    @CsvSource("'^1 2 3', 1 ^2 3")
    void parse_throws_exception(String line) throws CommandSyntaxException {
        assertEquals(
            "Cannot mix world and local coordinates (everything must either use ^ or not)",
            assertThrows(CommandSyntaxException.class, () -> VectorParser.parse3DPosition(new StringReader(line))).getRawMessage().toString()
        );
    }
    
    
    @ParameterizedTest
    @CsvSource({"'   -1.4  3.6', -1.4, 3.6", "' -1.4  3.6   2.7', -1.4, 3.6"})
    void parse2DVector(String line, double x, double z) throws CommandSyntaxException {
        var vector = VectorParser.parse2DVector(new StringReader(line));
        
        assertEquals(x, vector.getX(), 0);
        assertEquals(0, vector.getY(), 0);
        assertEquals(z, vector.getZ(), 0);
    }
    
    
    @ParameterizedTest
    @CsvSource({"' -1.4  3.6   2.7', -1.4, 3.6, 2.7"})
    void parse3DVector(String line, double x, double y, double z) throws CommandSyntaxException {
        var vector = VectorParser.parse3DVector(new StringReader(line));
        
        assertEquals(x, vector.getX(), 0);
        assertEquals(y, vector.getY(), 0);
        assertEquals(z, vector.getZ(), 0);
    }

} 
