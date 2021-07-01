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
import com.karuslabs.elementary.junit.annotations.*;
import com.karuslabs.typist.*;
import com.karuslabs.typist.Binding.*;
import com.karuslabs.typist.lexers.*;
import com.karuslabs.typist.lexers.Lexer.Memoizer;
import com.karuslabs.typist.parsers.*;
import com.karuslabs.typist.parsers.BindParser.Captor;
import com.karuslabs.utilitary.*;
import java.util.Map;
import javax.lang.model.element.VariableElement;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ToolsExtension.class)
@Inline(name = "com.karuslabs.typist.generation.chunks.Lambdas", source = """
package com.karuslabs.typist.generation.chunks;
                            
import com.karuslabs.elementary.junit.annotations.Case;
import com.karuslabs.typist.annotations.*;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
    
@Case("class")
@Command("a <b> <c>")
class Lambdas {
    @Case("b")
    @Bind(pattern = "<b>")
    public static final ArgumentType<? extends Player> players = null;
    @Case("c")
    @Bind(pattern = "<c>")
    public final ArgumentType<Integer> count = null;
    
    @Case("static_closure")
    @Bind(pattern = "<c>")
    public static final int staticClosure(CommandContext<CommandSender> context) {
        return 1;
    }
    
    @Case("instance_closure")
    @Bind(pattern = "<c>")
    public final int instanceClosure(CommandContext<CommandSender> context) {
        return 1;
    }
    
    @Case("desugar_order")
    @Bind(pattern = "<c>")
    public final int desugarOrder(@Case("desugar_b") @Let Player b, CommandContext<CommandSender> context, @Case("desugar_c") @Let int c) {
        return 1;
    }
    
    @Case("execution")
    @Bind(pattern = "<c>")
    public final void execution(CommandContext<CommandSender> context, CommandSender sender) {}
    
    @Case("static_method_reference")
    @Bind(pattern = "<c>")
    public static final boolean staticMethodReference(CommandSender sender) {
        return true;
    }
    
    @Case("instance_method_reference")
    @Bind(pattern = "<c>")
    public final boolean instanceMethodReference(CommandSender sender) {
        return true;
    }
    
}

""")
class LambdaTest {
    
    Logger logger = mock(Logger.class);
    Captor captor = new Captor(new Types(Tools.elements(), Tools.typeMirrors()));
    Lexer lexer = new CommandLexer(new ArgumentLexer(), LiteralLexer.aliasable(new Memoizer()));
    CommandParser commands = new CommandParser(logger, lexer);
    BindParser bindings = new BindParser(captor, logger, lexer);
    LetParser parser = new LetParser(logger, new ArgumentLexer());
    Environment environment = spy(new Environment());
    Source source = new Source();
    Types types = new Types(Tools.elements(), Tools.typeMirrors());
    int[] counter = new int[] {0};
    Cases cases = Tools.cases();
    
    void parse(String binding, String... lets) {
        commands.parse(environment, cases.one("class"));
        bindings.parse(environment, cases.one("b"));
        bindings.parse(environment, cases.one("c"));
        bindings.parse(environment, cases.one(binding));
        for (var let : lets) {
            parser.parse(environment, cases.one(let));
        }
    }
    
    Command command(String label) {
        return environment.method(cases.one(label)).get(0);
    }
    
    
    @Test
    void emit_static_closure() {
        parse("static_closure");
        var command = command("static_closure");
        
        Lambda.command(types, counter).emit(source, command, (Method) command.binding(Pattern.COMMAND), ";");
        
        assertEquals("""
            (context) -> {
                return com.karuslabs.typist.generation.chunks.Lambdas.staticClosure(context);
            };
            """.replace("\n", System.lineSeparator()), source.toString());
    }
    
    @Test
    void emit_instance_closure() {
        parse("instance_closure");
        var command = command("instance_closure");
        
        Lambda.command(types, counter).emit(source, command, (Method) command.binding(Pattern.COMMAND), ";");
        
        assertEquals("""
            (context) -> {
                return source.instanceClosure(context);
            };
            """.replace("\n", System.lineSeparator()), source.toString());
    }
    
    @Test
    void emit_desugared() {
        parse("desugar_order", "desugar_b", "desugar_c");
        var command = command("desugar_order");
        
        Lambda.command(types, counter).emit(source, command, (Method) command.binding(Pattern.COMMAND), ";");
        
        assertEquals("""
            (context) -> {
                var b0 = context.getArgument("b", org.bukkit.entity.Player.class);
                var c1 = context.getArgument("c", int.class);
                return source.desugarOrder(b0, context, c1);
            };
            """.replace("\n", System.lineSeparator()), source.toString());
    }
    
    @Test
    void emit_execution() {
        parse("execution");
        var command = command("execution");
        
        Lambda.execution(types, counter).emit(source, command, (Method) command.binding(Pattern.EXECUTION), ";");
        
        assertEquals("""
            (sender, context) -> {
                source.execution(context, sender);
            };
            """.replace("\n", System.lineSeparator()), source.toString());
    }
    
    @Test
    void emit_static_method_reference() {
        parse("static_method_reference");
        var command = command("static_method_reference");
        
        Lambda.METHOD_REFERENCE.emit(source, command, (Method) command.binding(Pattern.REQUIREMENT), ";");
        
        assertEquals("com.karuslabs.typist.generation.chunks.Lambdas::staticMethodReference;" +  System.lineSeparator(), source.toString());
    }
    
    @Test
    void emit_instance_method_reference() {
        parse("instance_method_reference");
        var command = command("instance_method_reference");
        
        Lambda.METHOD_REFERENCE.emit(source, command, (Method) command.binding(Pattern.REQUIREMENT), ";");
        
        assertEquals("source::instanceMethodReference;" +  System.lineSeparator(), source.toString());
    }
    
    @Test
    void match_throws_exception() {
        var closure = new Closure(types, counter, Map.of(), false);
        assertEquals(
            "Unable to resolve parameter: org.bukkit.entity.Player b",
            assertThrows(IllegalStateException.class, () -> closure.match((VariableElement) cases.one("desugar_b"))).getMessage()
        );
    }

}
