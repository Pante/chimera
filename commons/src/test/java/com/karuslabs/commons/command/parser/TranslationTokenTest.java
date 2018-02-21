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
package com.karuslabs.commons.command.parser;

import com.karuslabs.commons.command.References;
import com.karuslabs.commons.locale.*;
import com.karuslabs.commons.locale.resources.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.karuslabs.commons.configuration.Yaml.*;
import static com.karuslabs.commons.locale.MessageTranslation.NONE;
import static com.karuslabs.commons.locale.providers.Provider.DETECTED;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.mock;


@TestInstance(PER_CLASS)
class TranslationElementTest {
    
    References references = mock(References.class);
    TranslationToken token = new TranslationToken(references, null, null, DETECTED);
    
    
    @Test
    void getNull() {
        assertSame(NONE, token.getNull(null, null));
    }
    
    
    @Test
    void isAssignable() {
       assertTrue(token.isAssignable(COMMANDS, "help.translation"));
    }
    
    
    @ParameterizedTest
    @CsvSource({"translation, true", "empty, false", "bundle, false", "fail, false", "embedded, false", "folder, false"})
    void isAssignable(boolean expected, String key) {
        assertEquals(expected, token.isAssignable(INVALID, key));
    }
    
    
    @Test
    void get() {
        MessageTranslation translation = token.get(COMMANDS.getConfigurationSection("help"), "translation");
        Resource[] resources = ((ExternalControl) translation.getControl()).getResources();
        EmbeddedResource embedded = (EmbeddedResource) resources[0];
        ExternalResource file = (ExternalResource) resources[1];
        
        assertSame(DETECTED, translation.getProvider());
        assertEquals("Resource", translation.getBundleName());
        assertEquals(2, resources.length);
        assertEquals("path1/", embedded.getPath());
        assertEquals("path2", file.getPath());
    }
    
        
    @Test
    void getReference() {
        token.getReference("a key");
        references.getTranslation("a key");
    }
    
    @Test
    void register() {
        token.register("a key", NONE);
        references.translation("a key", NONE);
    }
    
    @Test
    void getDefaultReference() {
        assertSame(MessageTranslation.NONE, token.getDefaultReference());
    }
    
}
