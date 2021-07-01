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
import com.karuslabs.typist.annotations.Bind;
import com.karuslabs.typist.lexers.*;
import com.karuslabs.typist.lexers.Lexer.Memoizer;
import com.karuslabs.typist.parsers.BindParser.Captor;
import com.karuslabs.utilitary.Logger;

import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.utilitary.type.Find.TYPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(ToolsExtension.class)
@Inline(name = "com.karuslabs.typist.parsers.Cases", source = """
package com.karuslabs.typist.parsers;

import com.karuslabs.elementary.junit.annotations.Case;
import com.karuslabs.typist.annotations.*;

@Case("class")
@Command({"a <b> <b> c"})
class Cases {

    @Case("namespace")
    @Bind("a <b> <b>")
    public final void namespace() {}

    @Case("invalid_namespace")
    @Bind("<b> c")
    public final void invalid_namespace() {}

    @Case("invalid")
    @Bind({"<a|b>"})
    public final void invalid() {}

    @Case("pattern_1")
    @Bind(pattern = "<b> c")
    public final void pattern_1() {}

    @Case("pattern_2")
    @Bind(pattern = "a <b>")
    public final void pattern_2() {}

    @Case("pattern_3")
    @Bind(pattern = "<b>")
    public final void pattern_3() {}

    @Case("invalid_pattern")
    @Bind(pattern = "a <b> c")
    public final void invalid_pattern() {}

    @Case("invalid_method")
    @Bind("<b>")
    double invalid_method() { return 0; }

    @Case("invalid_field")
    @Bind("<b>")
    Object invalid_field;
                                                              
    @Case("empty")
    @Bind({})
    void empty() {}
                                                              
    @Case("empty_name")
    @Bind("")
    void empty_name() {}

}

class Unannotated {

    @Case("unannotated")
    @Bind("a")
    void test() {}

}
""")
class BindParserTest {
    
    Environment environment = spy(new Environment());
    Logger logger = mock(Logger.class);
    Captor captor = new Captor(new Types(Tools.elements(), Tools.typeMirrors()));
    Lexer lexer = new CommandLexer(new ArgumentLexer(), LiteralLexer.aliasable(new Memoizer()));
    BindParser parser = new BindParser(captor, logger, lexer);
    Cases cases = Tools.cases();
    
    @BeforeEach
    void before() {
        new CommandParser(logger, lexer).parse(environment, cases.one("class"));
    }
    
    @Test
    void parse_namespace() {
        var element = cases.one("namespace");
        
        parser.parse(environment, element);
        
        assertEquals(1, environment.method(element).size());
    }
    
    @Test
    void parse_invalid_namespace() {
        var element = cases.one("invalid_namespace");
        
        parser.parse(environment, element);
        
        verify(logger).error(element, "\"<b> c\" does not exist");
        verify(environment, never()).method(any());
    }
    
    @ParameterizedTest
    @MethodSource("parse_pattern_parameters")
    void parse_pattern(String label, int expected) {
        var element = cases.one(label);
        
        parser.parse(environment, element);
        
        assertEquals(expected, environment.method(element).size());
    }
    
    static Stream<Arguments> parse_pattern_parameters() {
        return Stream.of(
            of("pattern_1", 1),
            of("pattern_2", 1),
            of("pattern_3", 2)
        );
    }
    
    @Test
    void parse_invalid_pattern() {
        var element = cases.one("invalid_pattern");
        
        parser.parse(environment, element);
        
        verify(logger).error(element, "Pattern: \"a <b> c\" does not exist");
        verify(environment, never()).method(any());
    }
    
    @Test
    void parse_invalid_method() {
        var element = cases.one("invalid_method");
        
        parser.parse(environment, element);
        
        verify(logger).error(element, "Method: invalid_method returns an invalid type, should be an int, void, boolean or CompletableFuture<Suggestions>");
        verify(environment, never()).method(any());
    }
    
    @Test
    void parse_invalid_field() {
        var element = cases.one("invalid_field");
        
        parser.parse(environment, element);
        
        verify(logger).error(element, "Field: invalid_field has an invalid type, should be an ArgumentType<?>, Command<CommandSender>, Exeuction<CommandSender>, Predicate<CommandSender> or SuggestionProvider<CommandSender>");
        verify(environment, never()).method(any());
    }
    
    @Test
    void parse_empty() {
        var element = cases.one("empty");
        
        parser.parse(environment, element);
        
        verify(logger).error(element, "@Bind should not be empty");
        verify(environment, never()).method(any());
    }
    
    @Test
    void parse_empty_name() {
        var element = cases.one("empty_name");
        
        parser.parse(environment, element);
        
        verify(logger).error(element, "Command should not be blank");
        verify(environment, never()).method(any());
    }
    
    @Test
    void parse_unannotated() {
        var element = cases.one("unannotated");
        
        parser.parse(environment, element);
        
        verify(logger).error(element.accept(TYPE, null), "Class should be annotated with @Command");
        verify(environment, never()).method(any());
    }
    
    @Test
    void annotation() {
        assertEquals(Bind.class, parser.annotation());
    }
    
    @Test
    void defaultAction() {
        var element = cases.one("class");
        
        assertNull(element.accept(captor, logger));
        verify(logger).error(element, "@Bind is used on an invalid target", "should annotate a field or method");
    }

} 
