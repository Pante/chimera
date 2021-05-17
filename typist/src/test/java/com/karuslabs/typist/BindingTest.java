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
package com.karuslabs.typist;

import com.karuslabs.commons.command.Execution;
import com.karuslabs.elementary.junit.*;
import com.karuslabs.elementary.junit.annotations.*;
import com.karuslabs.typist.Binding.*;
import com.karuslabs.typist.Binding.Pattern.Group;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.lang.model.element.*;

import org.bukkit.command.CommandSender;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.typist.Binding.Pattern.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.mock;

class BindingTest {

    @ParameterizedTest
    @MethodSource("pattern_parameters")
    void pattern(Pattern pattern, Group group, String article, String type, String noun) {
        assertEquals(group, pattern.group);
        assertEquals(article, pattern.article);
        assertEquals(type, pattern.type);
        assertEquals(noun, pattern.noun);
        
        assertEquals(type, pattern.type);
    }
    
    static Stream<Arguments> pattern_parameters() {
        return Stream.of(
            of(ARGUMENT_TYPE, Group.ARGUMENT_TYPE, "An", "ArgumentType<?>", "type"),
            of(COMMAND, Group.COMMAND, "A", "Command<CommandSender>", "command"),
            of(EXECUTION, Group.COMMAND, "An", "Execution<CommandSender>", "command"),
            of(REQUIREMENT, Group.REQUIREMENT, "A", "Predicate<CommandSender>", "requirement"),
            of(SUGGESTION_PROVIDER, Group.SUGGESTION_PROVIDER, "A", "SuggestionProvider<CommandSender>", "suggestions")
        );
    }
    
}

@ExtendWith(ToolsExtension.class)
@Introspect("BindingTest")
class FieldTest {
 
    Types types = new Types(Tools.elements(), Tools.typeMirrors());
    Cases cases = Tools.cases();
    
    @ParameterizedTest
    @MethodSource("capture_parameters")
    void capture(String label, Pattern pattern) {
        var field = Field.capture(types, (VariableElement) cases.one(label));
        
        assertEquals(cases.one(label), field.site);
        assertEquals(pattern, field.pattern);
    }
    
    @Case ArgumentType<String> argument;
    @Case Execution<CommandSender> execution;
    @Case com.mojang.brigadier.Command<CommandSender> command;
    @Case Predicate<CommandSender> requirement;
    @Case SuggestionProvider<CommandSender> provider;
    
    static Stream<Arguments> capture_parameters() {
        return Stream.of(
            of("argument", Pattern.ARGUMENT_TYPE),
            of("execution", Pattern.EXECUTION),
            of("command", Pattern.COMMAND),
            of("requirement", Pattern.REQUIREMENT),
            of("provider", Pattern.SUGGESTION_PROVIDER)
        );
    }
    
    @Test
    void capture_null() {
        assertNull(Field.capture(types, (VariableElement) cases.one("something")));
    }
    
    @Case String something;
    
    
}

@ExtendWith(ToolsExtension.class)
@Introspect("BindingTest")
class MethodTest {
    
    Types types = new Types(Tools.elements(), Tools.typeMirrors());
    Cases cases = Tools.cases();
    Method method = Method.capture(types, (ExecutableElement) cases.one("command_method"));
    
    @ParameterizedTest
    @MethodSource("capture_parameters")
    void capture(String label, Pattern pattern) {
        var method = Method.capture(types, (ExecutableElement) cases.one(label));
        
        assertEquals(cases.one(label), method.site);
        assertEquals(pattern, method.pattern);
    }
    
    @Case int command_method() { return 0; }
    @Case void execution_method() {}
    @Case boolean requirement_method() { return true; }
    @Case CompletableFuture<Suggestions> provider_method() { return null; }
    
    static Stream<Arguments> capture_parameters() {
        return Stream.of(
            of("command_method", Pattern.COMMAND),
            of("execution_method", Pattern.EXECUTION),
            of("requirement_method", Pattern.REQUIREMENT),
            of("provider_method", Pattern.SUGGESTION_PROVIDER)
        );
    }
    
    @Test
    void capture_null() {
        assertNull(Method.capture(types, (ExecutableElement) cases.one("something_method")));
    }
    
    @Case ArgumentType<?> something_method() { return null; }
    
    
    @Test
    void parameter() {
        var command = mock(Command.class);
        var reference = new Reference(0, (VariableElement) cases.one("thing"), command);
        
        method.parameter(command, reference);
        assertEquals(Map.of(0, reference), method.parameters(command));
    }
    
    @Case String thing;
    
}