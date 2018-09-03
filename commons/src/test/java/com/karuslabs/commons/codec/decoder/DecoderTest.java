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

package com.karuslabs.commons.codec.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DecoderTest {
    
    static final String ENCODED = "codec/decoder/encoded.json";
    static final String UNSUPPORTED = "codec/decoder/unsupported.xml";
    
    
    Decoder<String, String> decoder = new Decoder<>("Default value") {
        @Override
        protected ObjectMapper defaultMapper(String format) {
            return Decoder.JSON;
        }
        @Override
        protected String exceptional(IOException e) {
            return "error";
        }
    };
    
    
    @Test
    void from_file() throws URISyntaxException {
        assertEquals("Default value", decoder.from(new File(getClass().getClassLoader().getResource(ENCODED).toURI())));
    }
    
    
    @Test
    void from_file_exception() throws URISyntaxException {
        assertEquals("error", decoder.from(new File(getClass().getClassLoader().getResource(UNSUPPORTED).toURI())));
    }
    
    
    @Test
    void from_inputstream() {
        assertEquals("Default value", decoder.from(getClass().getClassLoader().getResourceAsStream(ENCODED), "json"));
    }
    
    
    @Test
    void from_inputstream_exception() {
        assertEquals("error", decoder.from(getClass().getClassLoader().getResourceAsStream(UNSUPPORTED), "json"));
    }
    
    
    @ParameterizedTest
    @MethodSource({"mapper_provider"})
    void mapper(String format, ObjectMapper expected) {
        assertSame(expected, decoder.mapper(format));
    }
    
    static Stream<Arguments> mapper_provider() {
        return Stream.of(of("json", Decoder.JSON), of("properties", Decoder.PROPERTIES), of("yml", Decoder.YAML), of("yaml", Decoder.YAML));
    }
    
    
    @Test
    void defaultMapper() {
        assertEquals("Unsupported format: xml", assertThrows(UnsupportedOperationException.class, () -> new Decoder<>(null){}.defaultMapper("xml")).getMessage());
    }
    
    
    @Test
    void value() {
        assertNull(decoder.value());
    }
    
    
    @Test
    void exceptional() {
        assertThrows(UncheckedIOException.class, () -> new Decoder<>(null){}.exceptional(mock(IOException.class)));
    }
    
}
