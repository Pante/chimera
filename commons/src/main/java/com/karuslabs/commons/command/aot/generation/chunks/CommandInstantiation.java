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
package com.karuslabs.commons.command.aot.generation.chunks;

import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.Binding.*;
import com.karuslabs.commons.command.aot.Binding.Pattern.Group;
import com.karuslabs.Satisfactory.generation.Source;

import java.util.*;

import org.apache.commons.text.StringEscapeUtils;

import static com.karuslabs.commons.command.aot.Binding.Pattern.Group.*;
import static com.karuslabs.commons.command.aot.generation.chunks.Constants.*;
import static com.karuslabs.Satisfactory.Texts.quote;
import static javax.lang.model.element.Modifier.STATIC;

public class CommandInstantiation {
    
    private final Map<Pattern, Lambda> lambdas;
    private final int[] counter;
    
    public CommandInstantiation(Map<Pattern, Lambda> lambdas, int[] counter) {
        this.lambdas = lambdas;
        this.counter = counter;
    }
    
    public String emit(Source source, Command command) {
        var name = "command" + counter[0]++;
        switch (command.identity.type) {
            case ARGUMENT:
                argument(source, command, name);
                return name;
                
            case LITERAL:
                literal(source, command, name);
                return name;
                
            default:
                throw new UnsupportedOperationException("Unsupported command type: " + command.identity.type);
        }
    }
    
    void argument(Source source, Command command, String name) {
        source.line("var ", name, " = new Argument<>(")
              .indent()
                .line(quote(StringEscapeUtils.escapeJava(command.identity.name)), ",");
                parameter(source, command, ARGUMENT_TYPE, NULL, ",");
                parameter(source, command, COMMAND, NULL, ",");
                parameter(source, command, Group.REQUIREMENT, Constants.REQUIREMENT, ",");
                parameter(source, command, SUGGESTION_PROVIDER, NULL, "");
        source.unindent()
              .line(");");
    }
    
    void literal(Source source, Command command, String name) {
        source.line("var ", name, " = new Literal<>(")
              .indent()
                .line(quote(StringEscapeUtils.escapeJava(command.identity.name)), ",");
                 parameter(source, command, COMMAND, NULL, ",");
                 parameter(source, command, Group.REQUIREMENT, Constants.REQUIREMENT, "");
        source.unindent()
              .line(");");
        
        for (var alias : command.aliases) {
            source.line("Literal.alias", Source.arguments(name, quote(StringEscapeUtils.escapeJava(alias))), ";");
        }
    }
    
    void parameter(Source source, Command command, Pattern.Group group, String defaultValue, String separator) {
        var binding = command.groups.get(group);
        if (binding == null) {
            source.line(defaultValue, separator);
            return;
        }

        var reciever = binding.site.getModifiers().contains(STATIC) ? command.site.getQualifiedName() : SOURCE;
        if (binding instanceof Field) {
            source.line(reciever, ".", ((Field) binding).site.getSimpleName(), separator);
            
        } else if (binding instanceof Method) {
            lambdas.get(binding.pattern).emit(source, command, (Method) binding, separator);
        }
    }
    
}
