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
package com.karuslabs.typist.generation.chunks;

import com.karuslabs.typist.Command;
import com.karuslabs.typist.Binding.*;
import com.karuslabs.typist.Binding.Pattern.Group;
import com.karuslabs.utilitary.Source;

import java.util.*;

import org.apache.commons.text.StringEscapeUtils;

import static com.karuslabs.typist.Binding.Pattern.Group.*;
import static com.karuslabs.typist.generation.chunks.Constants.*;
import static com.karuslabs.utilitary.Texts.quote;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * A generator which emits the instantiation of a command and its subsequent assignment
 * to a local variable.
 */
public record CommandInstantiation(Map<Pattern, Lambda> lambdas, int[] counter) {
    
    /**
     * Emits the instantiation of a command and subsequent assignment to a local variable.
     * 
     * @param source the source
     * @param command the command
     * @return the generated local variable's name
     */
    public String emit(Source source, Command command) {
        var name = "command" + counter[0]++;
        switch (command.identity().type()) {
            case ARGUMENT -> argument(source, command, name);   
            case LITERAL -> literal(source, command, name);
        }
        
        return name;
    }
    
    /**
     * Generates the instantiation of an {@code Argument}.
     * 
     * @param source the source
     * @param command the command
     * @param name the generated variable's name
     */
    void argument(Source source, Command command, String name) {
        source.line("var ", name, " = new Argument<>(")
              .indent()
               .line(quote(StringEscapeUtils.escapeJava(command.identity().name())), ",");
                parameter(source, command, ARGUMENT_TYPE, NULL, ",");
         source.line("\"\",");
                parameter(source, command, COMMAND, NULL, ",");
                parameter(source, command, Group.REQUIREMENT, Constants.REQUIREMENT, ",");
                parameter(source, command, SUGGESTION_PROVIDER, NULL, "");
        source.unindent()
              .line(");");
    }
    
    /**
     * Generates the instantiation of a {@code Literal}.
     * 
     * @param source the source
     * @param command the command
     * @param name the generated variable's name
     */
    void literal(Source source, Command command, String name) {
        source.line("var ", name, " = new Literal<>(")
              .indent()
                .line(quote(StringEscapeUtils.escapeJava(command.identity().name())), ",")
                .line("\"\",");
                 parameter(source, command, COMMAND, NULL, ",");
                 parameter(source, command, Group.REQUIREMENT, Constants.REQUIREMENT, "");
        source.unindent()
              .line(");");
        
        for (var alias : command.aliases()) {
            source.line("Literal.alias", Source.arguments(name, quote(StringEscapeUtils.escapeJava(alias))), ";");
        }
    }
    
    /**
     * Generates the parameter for a command's instantiation.
     * 
     * @param source the source
     * @param command the command
     * @param group the group in which the parameter belongs
     * @param defaultValue the default value
     * @param separator the separator
     */
    void parameter(Source source, Command command, Pattern.Group group, String defaultValue, String separator) {
        var binding = command.groups().get(group);
        if (binding == null) {
            source.line(defaultValue, separator);
            return;
        }

        if (binding instanceof Field field) {
            var reciever = binding.site().getModifiers().contains(STATIC) ? command.site().getQualifiedName() : SOURCE;
            source.line(reciever, ".", field.site().getSimpleName(), separator);
            
        } else if (binding instanceof Method method) {
            lambdas.get(binding.pattern()).emit(source, command, method, separator);
        }
    }
    
}
