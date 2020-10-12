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
package com.karuslabs.commons.command.aot.parsers;

import com.karuslabs.annotations.processor.*;
import com.karuslabs.commons.command.aot.Identifier;
import com.karuslabs.commons.command.aot.Mirrors.*;
import com.karuslabs.commons.command.aot.annotations.Infer;
import com.karuslabs.commons.command.aot.lexers.Lexer;

import java.util.Map;
import javax.lang.model.element.*;

public class InferParser extends TokenParser {
    
    public InferParser(Logger logger, Lexer lexer) {
        super(logger, lexer);
    }

    @Override
    protected void process(Element element, Map<Identifier, Command> namespace) {
        var line = element.getAnnotation(Infer.class).value();
        if (line.equals(Infer.INFERRED_ARGUMENT)) {
            line = "<" + element.getSimpleName().toString() + ">";
        }
        
        Command command = null;
        
        var tokens = lexer.lex(logger, line);
        if (tokens.isEmpty()) {
            return;
        }
        
        for (var token : lexer.lex(logger, line)) {
            command = namespace.get(token.identifier);
            if (command == null) {
                logger.error(line, "does not exist");
                return;
            }
            
            namespace = command.children;
        }
        
        var executable = element.accept(Filter.EXECUTABLE, null);
        var member = command.members.get(executable);
        if (member instanceof Method) {
            var index = executable.getParameters().indexOf(element);
            ((Method) member).parameters.put(index, new Pointer(index, (VariableElement) element, command));
            
        } else {
            logger.error(executable.getSimpleName(), "is not annotated with @Bind", "method should be annotated with @Bind");
        }
        
    }
    
}
