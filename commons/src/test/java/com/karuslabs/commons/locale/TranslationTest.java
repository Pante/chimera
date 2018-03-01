/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.locale;

import com.karuslabs.commons.locale.resources.EmbeddedResource;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.bukkit.entity.Player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Locale.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class TranslationTest {
    
    static Player player = when(mock(Player.class).getLocale()).thenReturn("zh_CN").getMock();
    
    Translation translation = spy(new Translation("Translation", new ExternalControl(new EmbeddedResource("locale")), player -> null));
    
    
    @ParameterizedTest
    @MethodSource("parameters")
    void get(Consumer<Translation> consumer, Locale expected) {
        doReturn(null).when(translation).get((Locale) any());
        
        consumer.accept(translation);
        
        verify(translation).get(expected);
    }
    
    static Stream<Arguments> parameters() {
        return Stream.of(
            of(wrap(translation -> translation.get(player)), null),
            of(wrap(translation -> translation.getOrDefault(player, ITALY)), ITALY),
            of(wrap(translation -> translation.getOrDetected(player)), CHINA)
        );
    }
    
    static Object wrap(Consumer<Translation> consumer) {
        return consumer;
    }
    
    
    @Test
    void get_Locale() {
        assertEquals("Japanese {0}", translation.get(JAPAN).getString("locale"));
    }
    
}
