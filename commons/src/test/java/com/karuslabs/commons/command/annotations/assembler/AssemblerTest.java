/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.commons.command.annotations.assembler;

import com.karuslabs.commons.command.OptionalContext;
import com.karuslabs.commons.command.annotations.*;
import com.karuslabs.commons.command.types.EnchantmentType;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class AssemblerTest {
    
    Assembler<String> assembler = new Assembler<>();
    
    
    @Test
    void assemble() {
        var command = new TestCommand();

        TestCommand.assertCommand(command, assembler.assemble(command).get("a"));
    }
    
    
    @Test
    void bind() {
        var bound = new Binded();
        assembler.bind(bound);
        var bindings = assembler.bindings;
        
        assertEquals(3, bindings.map().size());
        assertSame(bound.a, bindings.get("a", ArgumentType.class));
        assertSame(bound.b, bindings.get("something", SuggestionProvider.class));
        assertSame(bound.c, bindings.get("c", ArgumentType.class));
    }
    
    
    static class Binded {
        
        private @Bind EnchantmentType a = new EnchantmentType();
        private @Bind("something") SuggestionProvider b = (context, builder) -> builder.buildFuture();
        private static @Bind ArgumentType<String> c = StringArgumentType.word();
        private ArgumentType<String> unbound = StringArgumentType.word();
        
    }
    
    
    @Test
    void bind_existing_throws_exception() {
        assertEquals(
            "@Bind(\"a\") b already exists",
            assertThrows(IllegalArgumentException.class, () -> assembler.bind(new Duplicate())).getMessage()
        );
    }
    
    
    static class Duplicate {
        
        @Bind ArgumentType<String> a = StringArgumentType.word();
        @Bind("a") ArgumentType<String> b = StringArgumentType.word();
        
    }
    
    
    @Test
    void bind_invalid_type_throws_exception() throws ReflectiveOperationException {
        var invalid_field = InvalidType.class.getDeclaredField("invalid_field");
        var field = InvalidType.class.getDeclaredField("field");

        assertEquals(
            "Invalid @Bind annotated field: invalid_field, field must be an ArgumentType or SuggestionProvider",
            assertThrows(IllegalArgumentException.class, () -> assembler.bind(new InvalidType(), invalid_field, field.getAnnotation(Bind.class))).getMessage()
        );
        
    }
    
    
    static class InvalidType {
        
        private @Bind ArgumentType<?> field;
        private String invalid_field;
        
    }
    
    
    @Test
    void generate() throws CommandSyntaxException {
        var object = new Generate();
        
        assembler.generate(object);
        var a = assembler.commands.get("a").get();
        var b = assembler.commands.get("b").get();
        
        a.getCommand().run(null);
        assertEquals(1, object.command);
        
        b.getCommand().run(null);
        assertEquals(1, object.executable);
    }
    
    
    static class Generate {
        
        static int method;
        
        int command;
        int executable;
        
        
        @Literal(namespace = "a")
        int command(CommandContext<String> context) {
            command++;
            return 1;
        }
        
        
        @Literal(namespace = "b")
        void executable(OptionalContext<String> context) {
            executable++;
        }
        
    }
    
    
    @Test
    void emit() throws ReflectiveOperationException {
       var signature = new InvalidSignature();
       var method = signature.getClass().getDeclaredMethod("method", OptionalContext.class);
        assertEquals(
            "Invalid signature for method in class com.karuslabs.commons.command.annotations.assembler.AssemblerTest$InvalidSignature, signaure must match Command or Executable",
            assertThrows(IllegalArgumentException.class, () -> assembler.emit(signature, method)).getMessage()
        );
        
    }
        
    
    static class InvalidSignature {
        
        int method(OptionalContext<String> context) {
            return 1;
        }
        
    }
    
    
    @Test
    void emit_throws_exception() throws NoSuchMethodException {
        assertEquals(
            "Failed to generate lambda from " + assembler.getClass(),
            assertThrows (RuntimeException.class, () -> assembler.emit(assembler, AssemblerTest.class.getDeclaredMethod("emit_throws_exception"), Assembler.COMMAND_SIGNATURE, AssemblerTest.class, "idk")).getMessage()
        );
        
    }

} 
