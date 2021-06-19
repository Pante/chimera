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
package com.karuslabs.typist.lints;

import com.karuslabs.elementary.junit.*;
import com.karuslabs.elementary.junit.annotations.Inline;
import com.karuslabs.typist.Environment;
import com.karuslabs.typist.lexers.*;
import com.karuslabs.typist.lexers.Lexer.Memoizer;
import com.karuslabs.typist.parsers.CommandParser;
import com.karuslabs.utilitary.Logger;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(ToolsExtension.class)
@Inline(name = "com.karuslabs.typist.lints.Cases", source = """
package com.karuslabs.typist.lints;

import com.karuslabs.elementary.junit.annotations.Case;
import com.karuslabs.typist.annotations.Command;

@Case("valid_1")
@Command({"a b", "a b|b1"})
class Valid_1 {}
                                                            
@Case("valid_2")
@Command({"a <b>", "a c|b"})
class Valid_2 {}

@Case("alias_alias")
@Command({"a b|1", "a c|1"})
class AliasAlias {}

@Case("alias_name")
@Command({"a b|c", "a c"})
class AliasName {}

@Case("name_alias")
@Command({"a c", "a b|c"})
class NameAlias {}
""")
class ConflictingAliasLintTest {
    
    Logger logger = mock(Logger.class);
    CommandParser parser = new CommandParser(logger, new CommandLexer(new ArgumentLexer(), LiteralLexer.aliasable(new Memoizer())));
    ConflictingAliasLint lint = new ConflictingAliasLint(logger);
    Environment environment = new Environment();
    Cases cases = Tools.cases();
    
    @ParameterizedTest
    @CsvSource("valid_1, valid_2")
    void lint(String label) {
        parser.parse(environment, cases.one(label));
        Lint.lint(environment, lint);
        
        verifyNoInteractions(logger);
    }
    
    @Test
    void lint_alias_alias() {
        var element = cases.one("alias_alias");
        
        parser.parse(environment, element);
        Lint.lint(environment, lint);
        
        var captor = ArgumentCaptor.forClass(String.class);
        
        verify(logger).error(eq(element), captor.capture());
        var message = captor.getValue();
        
        assertTrue(message.equals("Alias: \"1\" of \"a b|1\" conflicts with alias of \"a c|1\"") 
                || message.equals("Alias: \"1\" of \"a c|1\" conflicts with alias of \"a b|1\""));
    }
    
    @ParameterizedTest
    @CsvSource("alias_name, name_alias")
    void lint_alias_name(String label) {
        var element = cases.one(label);
        
        parser.parse(environment, element);
        Lint.lint(environment, lint);
        
        verify(logger).error(element, "Alias: \"c\" of \"a b|c\" conflicts with \"a c\"");
    }

} 
