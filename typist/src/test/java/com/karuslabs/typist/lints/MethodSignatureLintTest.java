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

import javax.lang.model.element.Element;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.mockito.Mockito.*;

@ExtendWith(ToolsExtension.class)
@Inline(name = "com.karuslabs.typist.lints.Cases", source = """
package com.karuslabs.typist.lints;

import com.karuslabs.elementary.junit.annotations.Case;
import com.karuslabs.typist.annotations.*;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.*;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import org.bukkit.command.CommandSender;

@Case("class")
@Command("a <b>")
class Cases {
    
    @Case("command_no_args")
    @Bind("a <b>")
    int command_no_args(CommandContext<CommandSender> c) { return 1; }
    
    @Case("commands_with_args")
    @Bind("a <b>")
    int command_with_args(@Let String a, CommandContext<CommandSender> c, @Let String b) { return 1; }
                                                            
    @Case("command_with_objects")
    @Bind("a <b>")
    int command_with_objects(CommandContext<CommandSender> c, @Let Object b) { return 1; }
    
    @Case("command_no_context")
    @Bind("a <b>")
    int command_no_context() { return 1; }
    
    @Case("command_multiple_context")
    @Bind("a <b>")
    int command_multiple_context(CommandContext<CommandSender> a, CommandContext<CommandSender> b) { return 1; }
    
    @Case("command_throws_runtime")
    @Bind("a <b>")
    int command_throws_runtime(CommandContext<CommandSender> a) throws CommandSyntaxException, NullPointerException { return 1; }
    
    @Case("command_throws_checked")
    @Bind("a <b>")
    int command_throws_checked(CommandContext<CommandSender> a) throws CommandSyntaxException, IOException { return 1; }
    
    
    @Case("execution_no_args")
    @Bind("a <b>")
    void execution_no_args(CommandContext<CommandSender> c, CommandSender s) {}
    
    @Case("execution_with_args")
    @Bind("a <b>")
    void execution_with_args(@Let String a, CommandContext<CommandSender> c, @Let String b, CommandSender sender) {}
                                                            
    @Case("execution_with_objects")
    @Bind("a <b>")
    void execution_with_objects(CommandContext<CommandSender> c, CommandSender sender, @Let Object b) {}
    
    @Case("execution_no_context")
    @Bind("a <b>")
    void execution_no_context(CommandSender s) {}
    
    @Case("execution_multiple_context")
    @Bind("a <b>")
    void execution_multiple_context(CommandContext<CommandSender> a, CommandContext<CommandSender> b, CommandSender s) {}
    
    @Case("execution_no_sender")
    @Bind("a <b>")
    void execution_no_sender(CommandContext<CommandSender> a) {}
    
    @Case("execution_multiple_sender")
    @Bind("a <b>")
    void execution_multiple_sender(CommandContext<CommandSender> c, CommandSender a, CommandSender b) {}
    
    @Case("execution_throws_runtime")
    @Bind("a <b>")
    void execution_throws_runtime(CommandContext<CommandSender> a, CommandSender s) throws CommandSyntaxException, NullPointerException { return 1; }
    
    @Case("execution_throws_checked")
    @Bind("a <b>")
    void execution_throws_checked(CommandContext<CommandSender> a, CommandSender s) throws CommandSyntaxException, IOException { return 1; }
    
    
    @Case("requirement")
    @Bind("a <b>")
    boolean requirement(CommandSender s) { return true; }
    
    @Case("requirement_no_args")
    @Bind("a <b>")
    boolean requirement_no_args() { return true; }
    
    @Case("requirement_with_args")
    @Bind("a <b>")
    boolean requirement_with_args(CommandSender a, CommandSender b) { return true; }
    
    @Case("requirement_throws_runtime")
    @Bind("a <b>")
    boolean requirement_throws_runtime(CommandSender s) throws NullPointerException { return true; }
    
    @Case("requirement_throws_checked")
    @Bind("a <b>")
    boolean requirement_throws_checked(CommandSender s) throws IOException { return true; }
    
    
    @Case("provider_no_args")
    @Bind("a <b>")
    CompletableFuture<Suggestions> provider_no_args(CommandContext<CommandSender> c, SuggestionsBuilder s) { return null;}
    
    @Case("provider_with_args")
    @Bind("a <b>")
    CompletableFuture<Suggestions> provider_with_args(@Let String a, CommandContext<CommandSender> c, @Let String b, SuggestionsBuilder sender) { return null;}
    
    @Case("provider_no_context")
    @Bind("a <b>")
    CompletableFuture<Suggestions> provider_no_context(SuggestionsBuilder s) { return null;}
    
    @Case("provider_multiple_context")
    @Bind("a <b>")
    CompletableFuture<Suggestions> provider_multiple_context(CommandContext<CommandSender> a, CommandContext<CommandSender> b, SuggestionsBuilder s) { return null;}
    
    @Case("provider_no_builder")
    @Bind("a <b>")
    CompletableFuture<Suggestions> provider_no_builder(CommandContext<CommandSender> a) { return null;}
    
    @Case("provider_multiple_builders")
    @Bind("a <b>")
    CompletableFuture<Suggestions> provider_multiple_sender(CommandContext<CommandSender> c, SuggestionsBuilder a, SuggestionsBuilder b) { return null;}
    
    @Case("provider_throws_runtime")
    @Bind("a <b>")
    CompletableFuture<Suggestions> provider_throws_runtime(CommandContext<CommandSender> a, SuggestionsBuilder s) throws CommandSyntaxException, NullPointerException { return 1; }
    
    @Case("provider_throws_checked")
    @Bind("a <b>")
    CompletableFuture<Suggestions> provider_throws_checked(CommandContext<CommandSender> a, SuggestionsBuilder s) throws CommandSyntaxException, IOException { return 1; }
                                                            
    @Case("field")
    @Bind("a <b>")
    ArgumentType<String> field;
    
}
""")
class MethodSignatureLintTest {
    
    Logger logger = mock(Logger.class);
    Lexer lexer = new CommandLexer(new ArgumentLexer(), LiteralLexer.aliasable(new Memoizer()));
    CommandParser commands = new CommandParser(logger, lexer);
    BindParser bindings = new BindParser(new Captor(new Types(Tools.elements(), Tools.typeMirrors())), logger, lexer);
    MethodSignatureLint lint = new MethodSignatureLint(logger, new Types(Tools.elements(), Tools.typeMirrors()));
    Environment environment = new Environment();
    Cases cases = Tools.cases();
    
    @ParameterizedTest
    @CsvSource("command_no_args, commands_with_args, command_throws_runtime, command_with_objects")
    void lint_valid_command(String label) {
        lint(label);
        verifyNoInteractions(logger);
    }
    
    @ParameterizedTest
    @CsvSource("command_no_context, command_multiple_context, command_throws_checked")
    void lint_invalid_command(String label) {
        var element = lint(label);
        verify(logger).error(element, "Method signature should match \"int method(CommandContext<CommandSender>, @Let T...) throws CommandSyntaxException\"");
    }
    
    
    @ParameterizedTest
    @CsvSource("execution_no_args, execution_with_args, execution_with_objects, execution_throws_runtime, execution_with_objects")
    void lint_valid_execution(String label) {
        lint(label);
        verifyNoInteractions(logger);
    }
    
    
    @ParameterizedTest
    @CsvSource("execution_no_context, execution_multiple_context, execution_throws_checked, execution_no_sender, execution_multiple_sender")
    void lint_invalid_execution(String label) {
        var element = lint(label);
        verify(logger).error(element, "Method signature should match \"void method(CommandContext<CommandSender>, CommandSender, @Let T...) throws CommandSyntaxException\"");
    }
    
    
    @ParameterizedTest
    @CsvSource("requirement, requirement_throws_runtime")
    void lint_valid_requirement(String label) {
        lint(label);
        verifyNoInteractions(logger);
    }
    
    @ParameterizedTest
    @CsvSource("requirement_no_args, requirement_with_args, requirement_throws_checked")
    void lint_invalid_requirement(String label) {
        var element = lint(label);
        verify(logger).error(element, "Method signature should match Predicate<CommandSender>");
    }
    
    
    @ParameterizedTest
    @CsvSource("provider_no_args, provider_with_args, provider_throws_runtime")
    void lint_valid_provider(String label) {
        lint(label);
        verifyNoInteractions(logger);
    }
    
    @ParameterizedTest
    @CsvSource("provider_no_context, provider_multiple_context, provider_throws_checked, provider_no_builder, provider_multiple_builder")
    void lint_invalid_provider(String label) {
        var element = lint(label);
        verify(logger).error(element, "Method signature should match \"CompletableFuture<Suggestions> method(CommandContext<CommandSender>, SuggestionBuilder, @Let T...) throws CommandSyntaxException\"");
    }
    
    @Test
    void lint_field() {
        lint("field");
        verifyNoInteractions(logger);
    }
    
    Element lint(String label) {
        commands.parse(environment, cases.one("class"));
        bindings.parse(environment, cases.one(label));
        
        Lint.lint(environment, lint);
        
        return cases.one(label);
    }

} 
