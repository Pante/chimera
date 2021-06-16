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
import com.karuslabs.typist.*;
import com.karuslabs.typist.lexers.*;
import com.karuslabs.typist.lexers.Lexer.Memoizer;
import com.karuslabs.typist.parsers.CommandParser;
import com.karuslabs.utilitary.Logger;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.karuslabs.typist.Identity.Type.ARGUMENT;
import static org.mockito.Mockito.*;

@ExtendWith(ToolsExtension.class)
@Inline(name = "com.karuslabs.typist.lints.Cases", source = """
package com.karuslabs.typist.lints;

import com.karuslabs.elementary.junit.annotations.Case;
import com.karuslabs.typist.annotations.Command;

@Case("valid")
@Command("a <b> c")
class Valid {}

@Case("invalid")
@Command("<a> b c")
class invalid {}
""")
class ArgumentPositionLintTest {
    
    Logger logger = mock(Logger.class);
    Environment environment = new Environment();
    CommandParser parser = new CommandParser(logger, new CommandLexer(new ArgumentLexer(), LiteralLexer.aliasable(new Memoizer())));
    ArgumentPositionLint lint = new ArgumentPositionLint(logger);
    Cases cases = Tools.cases();
        
    @BeforeEach
    void before() {
        
    }
        
    @Test
    void lint() {
        parser.parse(environment, cases.one("valid"));
        Lint.lint(environment, lint);
        
        verifyNoInteractions(logger);
    }
    
    @Test
    void lint_invalid() {
        var element = cases.one("invalid");
        parser.parse(environment, element);
        Lint.lint(environment, lint);
        
        verify(logger).error(element, new Identity(ARGUMENT, "a"), "is at an invalid position", "command should not start with an argument");
    }

} 
