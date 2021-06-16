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
import com.karuslabs.elementary.junit.annotations.*;
import com.karuslabs.typist.*;
import com.karuslabs.typist.lexers.*;
import com.karuslabs.typist.lexers.Lexer.Memoizer;
import com.karuslabs.typist.parsers.*;
import com.karuslabs.typist.parsers.BindParser.Captor;
import com.karuslabs.utilitary.Logger;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(ToolsExtension.class)
@Inline(name = "com.karuslabs.typist.lints.Cases", source = """
package com.karuslabs.typist.lints;

import com.karuslabs.elementary.junit.annotations.Case;
import com.karuslabs.typist.annotations.*;

import com.mojang.brigadier.arguments.ArgumentType;

@Case("valid")
@Command("a <b>")
class Valid {
    @Case("valid_binding")
    public final @Bind("a <b>") ArgumentType<String> field = null;
}

@Case("no_binding")
@Command("a <b>")
class NoBinding {}

@Case("invalid")
@Command("a")
class Invalid {
    @Case("invalid_binding")
    public final @Bind("a") ArgumentType<String> field = null;
}
""")
class ArgumentTypeLintTest {
    
    Logger logger = mock(Logger.class);
    Lexer lexer = new CommandLexer(new ArgumentLexer(), LiteralLexer.aliasable(new Memoizer()));
    CommandParser commands = new CommandParser(logger, lexer);
    BindParser bindings = new BindParser(new Captor(new Types(Tools.elements(), Tools.typeMirrors())), logger, lexer);
    ArgumentTypeLint lint = new ArgumentTypeLint(logger);
    Environment environment = new Environment();
    Cases cases = Tools.cases();
    
    @Test
    void lint() {
        commands.parse(environment, cases.one("valid"));
        bindings.parse(environment, cases.one("valid_binding"));
        
        Lint.lint(environment, lint);
        
        verifyNoInteractions(logger);
    }
    
    @Test
    void lint_no_binding() {
        var element = cases.one("no_binding");
        commands.parse(environment, element);
        
        Lint.lint(environment, lint);
        
        verify(logger).error(element, "No ArgumentType<T> is bound to \"a <b>\"");
    }
    
    @Test
    void lint_invalid_binding() {
        commands.parse(environment, cases.one("invalid"));
        bindings.parse(environment, cases.one("invalid_binding"));
        
        Lint.lint(environment, lint);
        
        verify(logger).error(cases.one("invalid_binding"), "ArgumentType<T> should only be bound to an argument");
    }

} 
