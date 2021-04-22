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

import com.karuslabs.elementary.processor.Logger;
import com.karuslabs.old.Method;
import com.karuslabs.typist.*;
import com.karuslabs.typist.Binding.Pattern;
import com.karuslabs.commons.command.annotations.Let;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.*;

<<<<<<< Updated upstream
import static com.karuslabs.typist.Binding.Pattern.*;
import static com.karuslabs.old.Assertions.*;

=======
>>>>>>> Stashed changes
public class MethodSignatureLint extends TypeLint {

    private final Map<Pattern, Method> methods = new EnumMap<>(Pattern.class);
    
    public MethodSignatureLint(Logger logger, Types types) {
        super(logger, types);
        
        var annotated = min(0, variable().annotations(contains(Let.class)));
        var exceptions = match(
            max(1, subtype(CommandSyntaxException.class)),
            min(0, subtype(RuntimeException.class))
        );
        
        // Rename match to equal
        // No more builders, use vararg builder with type inference
        
        method(
            annotations(contains(no)),
            modifiers(equal(PUBLIC, FINAL)),
            supertype(),
            parameters(equal(
                range(1, 3, variable(type(types.cotext))),
                min(0, variable(annotations(contains(no(Let.class))))))
            )),
            exceptions,
        ).or("Method signature ");
        methods.put(COMMAND, method().parameters(match(
                exactly(1, variable().type(supertype(types.context))), 
                annotated
            )).exceptions(exceptions).condition(
                "Method signature should match T1 method(CommandContext<CommandSender>, !(@Let T1)...) throws CommandSyntaxException"
                "Parameters should contain 1 CommandContext<CommandSender>. Other parameters should be annotated with @Let.\n" +
                "In addition, method should only throw a CommandSyntaxException."
            ).get()
        );
        
        methods.put(EXECUTION, method().parameters(match(
                exactly(1, variable().type(supertype(types.context))),
                exactly(1, variable().type(supertype(types.sender)).annotations(not(contains(Let.class)))),
                annotated
            )).exceptions(exceptions).condition(
                "Parameters should contain 1 CommandContext<CommandSender> and 1 CommandSender. Other parameters should be annotated with @Let.\n" +
                "In addition, method should only throw a CommandSyntaxException."
            ).get()
        );
        
        methods.put(REQUIREMENT, method()
            .parameters(match(exactly(1, variable().type(supertype(types.sender)))))
            .exceptions(match(no(subtype(Exception.class))))
            .condition("Method should match Predicate<CommandSender>").get()
        );
        

        
        methods.put(SUGGESTION_PROVIDER, method().parameters(match(
                exactly(1, variable().type(supertype(types.context))),
                exactly(1, variable().type(supertype(SuggestionsBuilder.class)).annotations(not(contains(Let.class)))),
                annotated
           )).exceptions(exceptions).condition(
                "Parameters should contain 1 CommandContext<CommandSender> and 1 SuggestionsBuilder. Other parameters should be annotated with @Let.\n" +
                "In addition, method should only throw a CommandSyntaxException."
           ).get()
        );
    }

    @Override
    public void lint(Environment environment, Command command) {
        for (var binding : command.bindings.values()) {
            if (!(binding instanceof Binding.Method)) {
                continue;
            }
            
            var method = methods.get(binding.pattern);
            if (!method.test(types, binding.site)) {
                logger.error(binding.site, method.condition());
            }
        }
    }

}
