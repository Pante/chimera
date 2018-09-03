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

package com.karuslabs.commons.codec.encoder;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import java.io.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EncoderTest {
    
    ObjectWriter writer = mock(ObjectWriter.class);
    File file = new File("unsupported.xml");
    OutputStream stream = mock(OutputStream.class);
    
    
    Encoder<String, JsonNode> encoder = spy(new Encoder<String, JsonNode>() {
        @Override
        public JsonNode encode(JsonNodeFactory factory, String value) {
            return NullNode.instance;
        }
        @Override
        protected ObjectWriter defaultWriter(String format) {
            return writer;
        }
        @Override
        protected void exceptional(IOException e) {
            
        }
    });
    
    Encoder<String, JsonNode> unmodified = new Encoder<>() {
        @Override
        public JsonNode encode(JsonNodeFactory factory, String value) {
            return NullNode.instance;
        }   
    };
    
    
    @Test
    void to_file() throws IOException {
        encoder.to(file, "");
        
        verify(writer).writeValue(file, NullNode.instance);
    }
    
    
    @Test
    void to_file_exception() throws IOException {
        doThrow(IOException.class).when(writer).writeValue(any(File.class), any(JsonNode.class));
        encoder.to(file, "");
        
        verify(encoder).exceptional(any());
    }
    
    
    @Test
    void to_stream() throws IOException {
        encoder.to(stream, "xml", "");
        
        verify(writer).writeValue(stream, NullNode.instance);
    }
    
    
    @Test
    void to_stream_exception() throws IOException {
        doThrow(IOException.class).when(writer).writeValue(any(OutputStream.class), any(JsonNode.class));
        encoder.to(stream, "xml", "");
        
        verify(encoder).exceptional(any());
    }
    
    
    @ParameterizedTest
    @MethodSource({"writer_provider"})
    void writer(String format, ObjectWriter expected) {
        assertSame(expected, encoder.writer(format));
    }
    
    static Stream<Arguments> writer_provider() {
        return Stream.of(of("json", Encoder.JSON), of("properties", Encoder.PROPERTIES), of("yml", Encoder.YAML), of("yaml", Encoder.YAML));
    }
    
    
    @Test
    void defaultWritter() {
        assertEquals("Unsupported format: xml", assertThrows(UnsupportedOperationException.class, () -> unmodified.defaultWriter("xml")).getMessage());
    }
    
    
    @Test
    void exceptional() {
        assertThrows(UncheckedIOException.class, () -> unmodified.exceptional(mock(IOException.class)));
    }
    
}
