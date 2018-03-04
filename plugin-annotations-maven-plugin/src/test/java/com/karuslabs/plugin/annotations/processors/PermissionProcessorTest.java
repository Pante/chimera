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
package com.karuslabs.plugin.annotations.processors;

import com.karuslabs.commons.configuration.Configurations;
import com.karuslabs.plugin.annotations.annotations.*;

import java.util.stream.Stream;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class PermissionProcessorTest {
    
    PermissionProcessor processor = spy(new PermissionProcessor());
    ConfigurationSection config = Configurations.from(getClass().getClassLoader().getResourceAsStream("annotations.yml"));
    
    
    @Permission(name = "a", description = "a description", children = {@Child(name = "b")})
    @Permission(name = "b", value = Default.NOT_OP)
    static class Valid extends JavaPlugin {}
   
    @Permission(name = "a")
    @Permission(name = "a")
    static class Conflicting_Permissions extends JavaPlugin {}
    
    @Permission(name = "a", children = {@Child(name = "b")})
    static class Invalid_Child extends JavaPlugin {}
    
    @Permission(name = "a")
    @Permission(name = "b", children = {@Child(name = "a"), @Child(name = "a")})
    static class Conflicting_Children extends JavaPlugin {}
    
    
    @Test
    void process() {
        processor.process(Valid.class, config);
        
        assertEquals("a description", config.getString("permissions.a.description"));
        assertEquals(true, config.getBoolean("permissions.a.children.b"));
        assertEquals("not op", config.getString("permissions.b"));
    }
    
    
    @ParameterizedTest
    @MethodSource("process_ThrowsException_parameters")
    void process_ThrowsExceptions(Class<? extends JavaPlugin> type, String message) {
        assertEquals(
            message,
            assertThrows(ProcessorException.class, () -> processor.process(type, config)).getMessage()
        );
    }
    
    static Stream<Arguments> process_ThrowsException_parameters() {
        return Stream.of(
            of(Conflicting_Permissions.class, "Conflicting permissions: a, permissions must be unique"),
            of(Invalid_Child.class, "Invalid child permission: b, child permission must refer to a valid permission"),
            of(Conflicting_Children.class, "Conflicting child permissions: a, children must be unique")
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("isAnnotated_parameters")
    void isAnnotated(Class<? extends JavaPlugin> type, boolean expected) {
        assertEquals(expected, processor.isAnnotated(type));
    }
    
    static Stream<Arguments> isAnnotated_parameters() {
        return Stream.of(of(Valid.class, true), of(Conflicting_Permissions.class, true), of(JavaPlugin.class, false));
    }
    
}
