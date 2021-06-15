/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
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
package com.karuslabs.typist.parsers;

import com.karuslabs.elementary.junit.*;
import com.karuslabs.elementary.junit.annotations.*;
import com.karuslabs.typist.Environment;
import com.karuslabs.typist.annotations.Pack;
import com.karuslabs.utilitary.Logger;

import java.util.Set;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ToolsExtension.class)
@Inline(name = "com.karuslabs.typist.parsers.Cases", source = """
package com.karuslabs.typist.parsers;
                                                              
import com.karuslabs.elementary.junit.annotations.Case;
import com.karuslabs.typist.annotations.Pack;
            
@Case("relative")
@Pack
class Relative {}

@Case("defined")
@Pack(age = "com.something", file = "Help")
class Defined {}

@Case("invalid_package")
@Pack(age = "com.karuslabs.1d")
class InvalidPackage {}
                                                              
@Case("invalid_file")
@Pack(file = "1.1")
class InvalidFile {}
""")
class PackageParserTest {
    
    Logger logger = spy(Tools.logger());
    PackageParser parser = new PackageParser(logger);
    Environment environment = new Environment();
    Cases cases = Tools.cases();
    
    @Test
    void parse_relative() {
        var relative = cases.one("relative");
        
        parser.parse(environment, Set.of(relative));
        
        assertNotNull(environment.out);
        assertEquals("com.karuslabs.typist.parsers", environment.out.folder());
        assertEquals("Commands", environment.out.file());
    }
    
    @Test
    void parse_defined() {
        var defined = cases.one("defined");
        
        parser.parse(environment, Set.of(defined));
        
        assertNotNull(environment.out);
        assertEquals("com.something", environment.out.folder());
        assertEquals("Help", environment.out.file());
    }
    
    @Test
    void parse_multiple() {
        var relative = cases.one("relative");
        var defined = cases.one("defined");
        
        parser.parse(environment, Set.of(relative, defined));
        
        verify(logger).error(relative, "Project contains 2 @Pack annotations, should contain one @Pack annotation");
        verify(logger).error(defined, "Project contains 2 @Pack annotations, should contain one @Pack annotation");
        assertTrue(environment.namespaces.isEmpty());
    }
    
    @Test
    void parse_none() {
        parser.parse(environment, Set.of());
        
        verify(logger).error(null, "Project does not contain a @Pack annotation, should contain a @Pack annotation");
        assertTrue(environment.namespaces.isEmpty());
    }
    
    @Test
    void parse_invalid_package() {
        var invalid = cases.one("invalid_package");
        
        parser.parse(environment, invalid);
        
        verify(logger).error(invalid, "Invalid package name");
        assertTrue(environment.namespaces.isEmpty());
    }
    
    @Test
    void parse_invalid_file() {
        var invalid = cases.one("invalid_file");
        
        parser.parse(environment, invalid);
        
        verify(logger).error(invalid, "Invalid file name");
        assertTrue(environment.namespaces.isEmpty());
    }
    
    @Test
    void annotation() {
        assertEquals(Pack.class, parser.annotation());
    }

}
