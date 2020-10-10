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
import com.karuslabs.commons.command.aot.Mirrors.Method;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import org.bukkit.command.CommandSender;

public class RequirementSignature implements Signature {

    private final Typing typing;
    private final TypeMirror source;
    boolean declared;

    public RequirementSignature(Typing typing) {
        this.typing = typing;
        source = typing.type(CommandSender.class);
    }
    
    @Override
    public void lint(Logger logger, Method method, ExecutableElement executable) {
        var parameters = executable.getParameters();
        if (parameters.size() != 1) {
            logger.zone(executable).error("Method should have only 1 parameter");
            
        } else if (!typing.types.isSubtype(source, parameters.get(0).asType())) {
            logger.zone(parameters.get(0)).error("Parameter should be a CommandSender");
        }
        
        if (!executable.getThrownTypes().isEmpty()) {
            logger.zone(executable).error("Method should not throw exceptions");
        }
    }

}
