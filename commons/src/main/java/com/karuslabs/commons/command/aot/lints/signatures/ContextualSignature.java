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

import com.karuslabs.smoke.Typing;
import com.karuslabs.smoke.Logger;
import com.karuslabs.annotations.VisibleForOverride;
import com.karuslabs.commons.command.aot.Mirrors;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

public abstract class ContextualSignature implements Signature {
    
    protected final Typing typing;
    protected final TypeMirror exception;
    
    public ContextualSignature(Typing typing) {
        this.typing = typing;
        exception = typing.type(CommandSyntaxException.class);
    }
    
    @Override
    public void lint(Logger logger, Mirrors.Method method, ExecutableElement executable) {
        exception(logger, executable);
        
        var pointers = method.parameters;
        var parameters = executable.getParameters();
        
        for (int i = 0; i < parameters.size(); i++) {
            if (pointers.containsKey(i)) {
                continue;
            }
            
            lint(logger.zone(parameters.get(i)), method, parameters.get(i));
        }
        
        clear();
    }
    
    protected void exception(Logger logger, ExecutableElement executable) {
        var thrown = executable.getThrownTypes();
        if (thrown.size() > 1 || (thrown.size() == 1 && !typing.types.isSubtype(thrown.get(0), exception))) {
            logger.zone(executable).error("Method may only throw a CommandSyntaxException");
        }
    }
    
    protected abstract void lint(Logger logger, Mirrors.Method method, VariableElement parameter);
    
    @VisibleForOverride
    protected void clear() {};
    
}
