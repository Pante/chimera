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

package com.karuslabs.commons.codec.encoders;

import com.karuslabs.commons.codec.decoders.LocaleDecoder;

import java.io.File;
import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Locale.*;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class LocaleEncoderTest {
    
    static final String FOLDER = LocaleEncoderTest.class.getClassLoader().getResource("codec/encoder/").getPath();
    static final File JSON = new File(FOLDER, "locales.json");
    static final File PROPERTIES = new File(FOLDER, "locales.properties");
    static final File YAML = new File(FOLDER, "locales.yml");
    static final Map<UUID, Locale> LOCALES = Map.of(randomUUID(), CANADA_FRENCH, randomUUID(), KOREAN, randomUUID(), JAPAN);
    
    
    @Test
    void to_json() {
        LocaleEncoder.encode().to(JSON, LOCALES);
        assertEquals(LOCALES, LocaleDecoder.decode().from(JSON));
    }
    
    
    @Test
    void to_properties() {
        LocaleEncoder.encode().to(PROPERTIES, LOCALES);
        assertEquals(LOCALES, LocaleDecoder.decode().from(PROPERTIES));
    }
    
    
    @Test
    void to_yaml() {
        LocaleEncoder.encode().to(YAML, LOCALES);
        assertEquals(LOCALES, LocaleDecoder.decode().from(YAML));
    }
    
    
    @AfterAll
    static void delete_files() {
        JSON.delete();
        PROPERTIES.delete();
        YAML.delete();
    }
    
}
