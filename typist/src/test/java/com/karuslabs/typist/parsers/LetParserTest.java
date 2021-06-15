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
import com.karuslabs.elementary.junit.annotations.Inline;
import com.karuslabs.typist.*;
import com.karuslabs.typist.Binding.Method;
import com.karuslabs.typist.annotations.Let;
import com.karuslabs.typist.lexers.*;
import com.karuslabs.typist.lexers.Lexer.Memoizer;
import com.karuslabs.typist.parsers.BindParser.Captor;
import com.karuslabs.utilitary.Logger;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.karuslabs.typist.Identity.Type.ARGUMENT;
import static com.karuslabs.utilitary.type.Find.EXECUTABLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ToolsExtension.class)
@Inline(name = "com.karuslabs.typist.parsers.Cases", source = """
package com.karuslabs.typist.parsers;

import com.karuslabs.elementary.junit.annotations.Case;
import com.karuslabs.typist.annotations.*;

@Case("class")
@Command("a <b> c")
class Cases {
                                                    
    @Bind(pattern = "c")
    void explicit(@Case("explicit") @Let("<b>") String value) {}
    
    @Bind(pattern = "c")
    void implicit(@Case("implicit") @Let String b) {}
    
    @Bind(pattern = "c")
    void invalid(@Case("invalid") @Let String d) {}
                                                              
    @Bind(pattern = "c")
    void empty(@Case("empty") @Let("a") String d) {}

}
""")
class LetParserTest {
    
    Logger logger = mock(Logger.class);
    Captor captor = new Captor(new Types(Tools.elements(), Tools.typeMirrors()));
    Lexer lexer = new CommandLexer(new ArgumentLexer(), LiteralLexer.aliasable(new Memoizer()));
    CommandParser commands = new CommandParser(logger, lexer);
    BindParser bindings = new BindParser(captor, logger, lexer);
    LetParser parser = new LetParser(logger, new ArgumentLexer());
    Environment environment = spy(new Environment());
    Cases cases = Tools.cases();
    
    @BeforeEach
    void before() {
        commands.parse(environment, cases.one("class"));
    }
    
    @ParameterizedTest
    @CsvSource("implicit, explicit")
    void parse(String label) {
        var parameter = cases.one(label);
        var method = parameter.accept(EXECUTABLE, null);
        
        bindings.parse(environment, method);
        parser.parse(environment, parameter);
        
        var command = environment.method(method).get(0);
        var binding = (Method) command.bindings().get(method);
        
        assertEquals(command.parent(), binding.parameters(command).get(0).value());
    }
    
    @Test
    void parse_invalid() {
        var parameter = cases.one("invalid");
        var method = parameter.accept(EXECUTABLE, null);
        
        bindings.parse(environment, method);
        parser.parse(environment, parameter);
        
        var binding = (Method) environment.method(method).get(0).bindings().get(method);
        
        assertTrue(binding.parameters().isEmpty());
        verify(logger).error(parameter, new Identity(ARGUMENT, "d"), "does not exist in \"a <b> c\"");
    }
    
    @Test
    void parse_empty() {
        var parameter = cases.one("empty");
        var method = parameter.accept(EXECUTABLE, null);
        
        bindings.parse(environment, method);
        parser.parse(environment, parameter);
        
        var binding = (Method) environment.method(method).get(0).bindings().get(method);
        
        assertTrue(binding.parameters().isEmpty());
    }
    
    
    
    @Test
    void annotation() {
        assertEquals(Let.class, parser.annotation());
    }

} 
