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
import com.karuslabs.typist.parsers.*;
import com.karuslabs.typist.parsers.BindParser.Captor;
import com.karuslabs.utilitary.Logger;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.mockito.Mockito.*;

@ExtendWith(ToolsExtension.class)
@Inline(name = "", source = """
package com.karuslabs.typist.lints;

import com.karuslabs.commons.command.Execution;
import com.karuslabs.elementary.junit.annotations.Case;
import com.karuslabs.typist.annotations.*;
import org.bukkit.command.CommandSender;

@Case("class")
@Command({"a b"})
class Cases {
    
    @Case("valid_method")
    @Bind("a b")
    public final void valid() {}
                            
    @Case("valid_field")
    @Bind("a b")
    public final Execution<CommandSender> valid_field = null;
    
    @Case("invalid_method")
    @Bind("a b")
    void invalid_method() {}
    
    @Case("invalid_field")
    @Bind("a b")
    Execution<CommandSender> invalid_field;
    
}
""")
class PublicFinalBindingLintTest {
    
    Logger logger = mock(Logger.class);
    Lexer lexer = new CommandLexer(new ArgumentLexer(), LiteralLexer.aliasable(new Memoizer()));
    CommandParser commands = new CommandParser(logger, lexer);
    BindParser bindings = new BindParser(new Captor(new Types(Tools.elements(), Tools.typeMirrors())), logger, lexer);
    PublicFinalBindingLint lint = new PublicFinalBindingLint(logger, new Types(Tools.elements(), Tools.typeMirrors()));
    Environment environment = new Environment();
    Cases cases = Tools.cases();
    
    @ParameterizedTest
    @CsvSource("valid_method, valid_field")
    void lint(String label) {
        var element = cases.one(label);
        
        commands.parse(environment, cases.one("class"));
        bindings.parse(environment, element);
        Lint.lint(environment, lint);
        
        verifyNoInteractions(logger);
    }
    
    @ParameterizedTest
    @CsvSource({"invalid_method, Method should be public and final", "invalid_field, Field should be public and final"})
    void lint_invalid(String label, String message) {
        var element = cases.one(label);
        
        commands.parse(environment, cases.one("class"));
        bindings.parse(environment, element);
        Lint.lint(environment, lint);
        
        verify(logger).error(element, message);
    }

} 
