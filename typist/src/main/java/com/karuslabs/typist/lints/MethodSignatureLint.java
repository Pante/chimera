/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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

import com.karuslabs.typist.annotations.Let;
import com.karuslabs.utilitary.Logger;
import com.karuslabs.typist.*;
import com.karuslabs.typist.Binding.Pattern;
import com.karuslabs.satisfactory.Method;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.*;
import javax.lang.model.element.ExecutableElement;

import static com.karuslabs.satisfactory.Assertions.*;
import static com.karuslabs.satisfactory.sequence.Sequences.*;
import static com.karuslabs.typist.Binding.Pattern.*;

/**
 * A {@code Lint} which verifies that the signature of methods annotated with {@code @Bind}
 * are valid.
 */
public class MethodSignatureLint extends TypeLint {

    private final Map<Pattern, Method> methods = new EnumMap<>(Pattern.class);
    
    public MethodSignatureLint(Logger logger, Types types) {
        super(logger, types);
    
        var exceptions = equal(
            max(1, subtype(CommandSyntaxException.class)),
            min(0, subtype(RuntimeException.class))
        );
        
        methods.put(COMMAND, method(parameters(equal(
                times(1, variable(type(types.context))),
                min(0, variable(annotations(contains(Let.class))))
            )), exceptions
        ).or("Method signature should match \"int method(CommandContext<CommandSender>, @Let T...) throws CommandSyntaxException\""));
        
        methods.put(EXECUTION, method(parameters(equal(
                times(1, variable(type(types.context).or(type(types.optionalContext)))),
                times(1, variable(type(types.sender), annotations(contains(no(Let.class))))),
                min(0, variable(annotations(contains(Let.class))))
            )), exceptions
        ).or("Method signature should match \"void method(CommandContext<CommandSender>, CommandSender, @Let T...) throws CommandSyntaxException\""));
        
        methods.put(REQUIREMENT, method(
            parameters(equal(times(1, variable(type(types.sender))))),
            exceptions(each(subtype(RuntimeException.class)))
        ).or("Method signature should match Predicate<CommandSender>"));
        
        methods.put(SUGGESTION_PROVIDER, method(parameters(equal(
                times(1, variable(type(types.context))),
                times(1, variable(type(SuggestionsBuilder.class), annotations(contains(no((Let.class))))))
            )), exceptions
        ).or("Method signature should match \"CompletableFuture<Suggestions> method(CommandContext<CommandSender>, SuggestionBuilder, @Let T...) throws CommandSyntaxException\""));
    }

    @Override
    public void lint(Environment environment, Command command) {
        for (var binding : command.bindings().values()) {
            if (!(binding instanceof Binding.Method)) {
                continue;
            }

            var method = methods.get(binding.pattern());
            if (!method.test(types, (ExecutableElement) binding.site())) {
                logger.error(binding.site(), method.condition());
            }
        }
    }

}
