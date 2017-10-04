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

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.commons.locale.MessageTranslation.NONE;
import static java.util.Locale.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@TestInstance(PER_CLASS)
public class MessageTranslationTest {
    
    private MessageTranslation translation  = spy(new MessageTranslation("Translation", new ExternalControl(new EmbeddedResource("locale")), player -> null));
    private static Player player = when(mock(Player.class).getLocale()).thenReturn("zh_CN").getMock();
    
    
    @Test
    public void none_get() {
        assertSame(CachedResourceBundle.NONE, NONE.get(ITALY));
    }
    
    
    @Test
    public void none_format() {
        assertEquals("key", NONE.format("key"));
    }
    
    
    @Test
    public void none_locale() {
        NONE.format.setLocale(ENGLISH);
        NONE.locale(ITALY);
        
        assertEquals(ENGLISH, NONE.format.getLocale());
    }
    
    
    @Test
    public void format() {
        assertEquals("Japanese key", translation.locale(JAPAN).format("locale", "key"));
    }
    
    
    @ParameterizedTest
    @MethodSource("parameters")
    public void locale_player(Consumer<MessageTranslation> consumer, Locale expected) {
        doReturn(null).when(translation).locale((Locale) any());
        
        consumer.accept(translation);
        
        verify(translation).locale(expected);
    }
    
    static Stream<Arguments> parameters() {
        return Stream.of(
            of(wrap(translation -> translation.locale(player)), null),
            of(wrap(translation -> translation.localeOrDefault(player, ITALY)), ITALY),
            of(wrap(translation -> translation.localeOrDetected(player)), SIMPLIFIED_CHINESE)
        );
    }
    
    static Object wrap(Consumer<MessageTranslation> consumer) {
        return consumer;
    }
    
}
