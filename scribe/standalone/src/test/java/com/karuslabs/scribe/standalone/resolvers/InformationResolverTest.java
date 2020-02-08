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
package com.karuslabs.scribe.standalone.resolvers;

import com.karuslabs.scribe.annotations.Information;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static javax.tools.Diagnostic.Kind.ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Information(authors = {"Pante"}, description = "description", url = "http://wwww.repo.karuslabs.com", prefix = "prefix")
class InformationResolverTest {
    
    Element element = when(mock(Element.class).getAnnotation(Information.class)).thenReturn(InformationResolverTest.class.getAnnotation(Information.class)).getMock();
    Messager messager = mock(Messager.class);
    InformationResolver resolver = new InformationResolver(messager);
    Information invalid = new Information() {
        @Override
        public String[] authors() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String description() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String url() {
            return "htt://wwww.repo.karuslabs.com";
        }

        @Override
        public String prefix() {
            throw new UnsupportedOperationException("Not supported yet."); 
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    Information empty = new Information() {
        @Override
        public String[] authors() {
            return new String[]{};
        }

        @Override
        public String description() {
            return "";
        }

        @Override
        public String url() {
            return "";
        }

        @Override
        public String prefix() {
            return "";
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Information.class;
        }
    };
    
    
    @Test
    void resolve_element() {
        var results = new HashMap<String, Object>();
        
        resolver.resolve(element, results);
        
        assertArrayEquals(new String[] {"Pante"}, (String[]) results.get("authors"));
        assertEquals("description", results.get("description"));
        assertEquals("http://wwww.repo.karuslabs.com", results.get("website"));
        assertEquals("prefix", results.get("prefix"));
    }
    
    
    @Test
    void check() {
        resolver.check(invalid, element);
        verify(messager).printMessage(ERROR, "Invalid URL: htt://wwww.repo.karuslabs.com, htt://wwww.repo.karuslabs.com is not a valid URL", element);
    }
    
    
    @Test
    void resolve_information() {
        var results = new HashMap<String, Object>();
        
        resolver.resolve(element.getAnnotation(Information.class), results);
        
        assertArrayEquals(new String[] {"Pante"}, (String[]) results.get("authors"));
        assertEquals("description", results.get("description"));
        assertEquals("http://wwww.repo.karuslabs.com", results.get("website"));
        assertEquals("prefix", results.get("prefix"));
    }
    
    
    @Test
    void resolve_information_empty() {
        when(element.getAnnotation(Information.class)).thenReturn(empty);
        var results = new HashMap<String, Object>();
        
        resolver.resolve(element, results);
    }

}
