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
package com.karuslabs.plugin.annotations.resolvers;

import com.karuslabs.commons.configuration.Configurations;
import com.karuslabs.plugin.annotations.annotations.Command;

import java.util.stream.Stream;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class CommandResolverTest {
    
    CommandResolver resolver = spy(new CommandResolver());
    ConfigurationSection config = Configurations.from(getClass().getClassLoader().getResourceAsStream("annotations.yml"));
    
    
    @Command(name = "a", aliases = {"b", "c"}, description = "a description", permission = "a.permission", message = "a message", usage = "a usage")
    static class Valid extends JavaPlugin {
        
    }
    
    
    @Command(name = "b")
    @Command(name = "b")
    static class Invalid extends JavaPlugin {
        
    }
    
    
    @Test
    void resolve() {
        resolver.resolve(Valid.class, config);
        
        assertTrue(config.isConfigurationSection("commands.a"));
        assertEquals(asList("b", "c"), config.getStringList("commands.a.aliases"));
        assertEquals("a description", config.getString("commands.a.description"));
        assertEquals("a.permission", config.getString("commands.a.permission"));
        assertEquals("a message", config.getString("commands.a.permission-message"));
        assertEquals("a usage", config.getString("commands.a.usage"));
    }
    
    
    @Test
    void resolve_ThrowsException() {
        assertEquals("Conflicting command names: b, command names must be unique",
            assertThrows(ResolutionException.class, () -> resolver.resolve(Invalid.class, config)).getMessage()
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("isResolvable_parameters")
    void isResolvable(Class<? extends JavaPlugin> type, boolean expected) {
        assertEquals(expected, resolver.isResolvable(type));
    }
    
    static Stream<Arguments> isResolvable_parameters() {
        return Stream.of(of(Valid.class, true), of(Invalid.class, true), of(JavaPlugin.class, false));
    }
    
}
