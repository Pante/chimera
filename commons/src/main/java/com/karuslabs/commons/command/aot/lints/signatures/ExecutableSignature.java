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
package com.karuslabs.commons.command.aot.lints.signatures;

import com.karuslabs.annotations.processor.*;
import com.karuslabs.commons.command.OptionalContext;
import com.karuslabs.commons.command.aot.Mirrors.Method;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import org.bukkit.command.CommandSender;

public class ExecutableSignature extends ContextualSignature {
    
    private final TypeMirror source;
    private final TypeMirror optional;
    boolean sourceDeclared;
    boolean contextDeclared;
    
    public ExecutableSignature(Typing typing) {
        super(typing);
        source = typing.type(CommandSender.class);
        optional = typing.specialize(OptionalContext.class, CommandSender.class);
        sourceDeclared = false;
        contextDeclared = false;
    }

    @Override
    protected void lint(Logger logger, Method method, VariableElement parameter) {
        if (typing.types.isSubtype(source, parameter.asType())) {
            if (sourceDeclared) {
                logger.error("Only one CommandSender should be declared");
                
            } else {
                sourceDeclared = true;
            }
            
        } else if (typing.types.isSubtype(optional, parameter.asType())) {
            if (contextDeclared) {
                logger.error("Only one OptionalContext<CommandSender> should be declared");
                
            } else {
                sourceDeclared = true;
            }
            
        } else {
            logger.error("Parameter should be a CommandSender, OptionalContext<CommandSender> or be annotated with @Infer");
        }
    }
    
    @Override
    protected void clear() {
        sourceDeclared = false;
        contextDeclared = false;
    }

}
