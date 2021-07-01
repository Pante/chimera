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
package com.karuslabs.typist.generation.chunks;

import com.karuslabs.elementary.junit.*;
import com.karuslabs.elementary.junit.annotations.Inline;
import com.karuslabs.typist.*;
import com.karuslabs.typist.Binding.Pattern;
import com.karuslabs.typist.lexers.*;
import com.karuslabs.typist.lexers.Lexer.Memoizer;
import com.karuslabs.typist.parsers.*;
import com.karuslabs.typist.parsers.BindParser.Captor;
import com.karuslabs.utilitary.*;

import java.util.Map;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.karuslabs.typist.Identity.Type.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ToolsExtension.class)
@Inline(name = "com.karuslabs.typist.generation.chunks.Commands", source = """
package com.karuslabs.typist.generation.chunks;

import com.karuslabs.elementary.junit.annotations.Case;
import com.karuslabs.typist.annotations.*;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.util.function.Predicate;
import org.bukkit.command.CommandSender;

@Case("class")
@Command("a|a1 <b>")
class Commands {
    
    @Case("type") @Bind(pattern = {"a", "<b>"}) ArgumentType<?> type;
    @Case("predicate") @Bind(pattern = {"a", "<b>"}) Predicate<CommandSender> predicate;
    @Case("provider") @Bind(pattern = {"a", "<b>"}) SuggestionProvider<CommandSender> provider;
    
    @Case("execute")
    @Bind(pattern = {"a", "<b>"})
    void execute(CommandContext<CommandSender> context, CommandSender sender) {}
    
}
""")
class CommandInstantiationTest {
    
    Logger logger = mock(Logger.class);
    Captor captor = new Captor(new Types(Tools.elements(), Tools.typeMirrors()));
    Lexer lexer = new CommandLexer(new ArgumentLexer(), LiteralLexer.aliasable(new Memoizer()));
    CommandParser commands = new CommandParser(logger, lexer);
    BindParser bindings = new BindParser(captor, logger, lexer);
    Environment environment = spy(new Environment());
    Types types = new Types(Tools.elements(), Tools.typeMirrors());
    Source source = new Source();
    int[] counter = new int[] {0};
    CommandInstantiation instantiation = new CommandInstantiation(Map.of(Pattern.EXECUTION, Lambda.execution(types, counter)), counter);
    Cases cases = Tools.cases();
    
    void parse(String... labels) {
        commands.parse(environment, cases.one("class"));
        for (var label : labels) {
            bindings.parse(environment, cases.one(label));
        }
    }
    
    Command command() {
        return environment.namespace(cases.one("class")).get(new Identity(LITERAL, "a"));
    }
    
    @Test
    void argument() {
        parse("type", "predicate", "provider", "execute");
        assertEquals("command0", instantiation.emit(source, command().children().get(new Identity(ARGUMENT, "b"))));
        assertEquals("""
            var command0 = new Argument<>(
                "b",
                source.type,
                "",
                (sender, context) -> {
                    source.execute(context, sender);
                },
                source.predicate,
                source.provider
            );
            """.replace("\n", System.lineSeparator()), source.toString());
    }
    
    @Test
    void argument_default() {
        parse("type");
        assertEquals("command0", instantiation.emit(source, command().children().get(new Identity(ARGUMENT, "b"))));
        assertEquals("""
            var command0 = new Argument<>(
                "b",
                source.type,
                "",
                null,
                REQUIREMENT,
                null
            );
            """.replace("\n", System.lineSeparator()), source.toString());
    }
    
    @Test
    void literal() {
        parse("type", "predicate", "provider", "execute");
        assertEquals("command0", instantiation.emit(source, command()));
        assertEquals("""
            var command0 = new Literal<>(
                "a",
                "",
                (sender, context) -> {
                    source.execute(context, sender);
                },
                source.predicate
            );
            Literal.alias(command0, "a1");
            """.replace("\n", System.lineSeparator()), source.toString());
    }
    
    @Test
    void literal_default() {
        parse("type");
        assertEquals("command0", instantiation.emit(source, command()));
        assertEquals("""
            var command0 = new Literal<>(
                "a",
                "",
                null,
                REQUIREMENT
            );
            Literal.alias(command0, "a1");
            """.replace("\n", System.lineSeparator()), source.toString());
    }
    
    

} 
