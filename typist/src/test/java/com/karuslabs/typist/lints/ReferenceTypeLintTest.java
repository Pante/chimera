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

import javax.lang.model.element.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.mockito.Mockito.*;

@ExtendWith(ToolsExtension.class)
@Inline(name = "com.karuslabs.typist.lints.Cases", source = """
import com.karuslabs.elementary.junit.annotations.Case;
import com.karuslabs.typist.annotations.*;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.command.CommandSender;

@Case("class")
@Command("a <b> <c>")
class Cases {
    
    @Case("b") @Bind(pattern = "<b>") ArgumentType<Integer> b;
    @Case("c") @Bind(pattern = "<c>") ArgumentType<String> c;
    @Case("object") @Bind(pattern = "<b>") ArgumentType<?> object;
    @Case("string_capture") @Bind(pattern = "<b>") ArgumentType<? extends String> capture;
    @Case("raw_type") @Bind(pattern = "<b>") ArgumentType raw_type;

    @Case("self")
    @Bind(pattern = "<b>")
    void self(CommandContext<CommandSender> context, CommandSender s, @Let int b) {}
    
    @Case("previous")
    @Bind(pattern = "<c>")
    void previous(CommandContext<CommandSender> context, CommandSender s, @Let int b) {}
    
    @Case("wildcard")
    @Bind(pattern = "<b>")
    void wildcard(CommandContext<CommandSender> context, CommandSender s, @Let Object b) {}
    
    @Case("capture")
    @Bind(pattern = "<b>")
    void capture(CommandContext<CommandSender> context, CommandSender s, @Let String b) {}
    
    @Case("raw")
    @Bind(pattern = "<b>")
    void raw(CommandContext<CommandSender> context, CommandSender s, @Let String b) {}
    
    @Case("invalid")
    @Bind(pattern = "<b>")
    void invalid(CommandContext<CommandSender> context, CommandSender s, @Let String b) {}
      
}
""")
class ReferenceTypeLintTest {
    
    Logger logger = mock(Logger.class);
    Lexer lexer = new CommandLexer(new ArgumentLexer(), LiteralLexer.aliasable(new Memoizer()));
    CommandParser commands = new CommandParser(logger, lexer);
    BindParser bindings = new BindParser(new Captor(new Types(Tools.elements(), Tools.typeMirrors())), logger, lexer);
    LetParser lets = new LetParser(logger, new ArgumentLexer());
    ReferenceTypeLint lint = new ReferenceTypeLint(logger, new Types(Tools.elements(), Tools.typeMirrors()));
    Environment environment = new Environment();
    Cases cases = Tools.cases();
    
    @ParameterizedTest
    @CsvSource({"self, b", "previous, b", "wildcard, object", "capture, string_capture"})
    void lint_valid(String method, String type) {
        lint(method, type);
        verifyNoInteractions(logger);
    }
    
    @Test
    void lint_raw() {
        var element = lint("raw", "raw_type");
        verify(logger).warn(element, "Parameter references an argument with a raw ArgumentType");
    }
    
    @Test
    void lint_invalid_object() {
        var element = lint("invalid", "object");
        verify(logger).error(element, "Parameter should be an Object");
    }
    
    @Test
    void lint_invalid_type() {
        var element = lint("invalid", "b");
        verify(logger).error(element, "Parameter should be a supertype of Integer");
    }
    
    
    Element lint(String method, String type) {
        var executable = (ExecutableElement) cases.one(method);
        var parameter = executable.getParameters().get(2);
        
        commands.parse(environment, cases.one("class"));
        bindings.parse(environment, cases.one(type));
        bindings.parse(environment, executable);
        lets.parse(environment, parameter);
        
        Lint.lint(environment, lint);
        
        return parameter;
    }

} 
