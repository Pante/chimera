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
package com.karuslabs.commons.command.aot.lints;

import com.karuslabs.annotations.processor.*;
import com.karuslabs.commons.command.aot.Identifier;
import com.karuslabs.commons.command.aot.Identifier.Type;
import com.karuslabs.commons.command.aot.Mirrors.*;

import javax.lang.model.type.DeclaredType;

import static com.karuslabs.annotations.processor.Messages.quote;

public class PointerValueLint implements Lint {

    private final Typing typing;
    
    PointerValueLint(Typing typing) {
        this.typing = typing;
    }
    
    @Override
    public void lint(Logger logger, Identifier identifier, Command command) {
        for (var member : command.members.values()) {
            if (member instanceof Method) {
                var method = (Method) member;
                for (var pointer : method.parameters.values()) {
                    lint(logger, command, pointer);
                }
            }
        }
    }
    
    void lint(Logger logger, Command command, Pointer pointer) {
        logger.zone(pointer.site);
        
        var value = pointer.value;
        if (!find(value, command)) {
            logger.error(pointer.site.getSimpleName(), "points to an invalid argument", "pointed argument should be a parent of " + quote(command.identifier));
        }
        
        if (value.identifier.type != Type.ARGUMENT) {
            logger.error(pointer.site.getSimpleName(), "points to an invalid command", "pointed command should be an argument");
            return;
        }
        
        for (var member : value.members.values()) {
            if (member.type == Member.Type.ARGUMENT_TYPE) {
                var type = member.site.asType();
                if (!(type instanceof DeclaredType)) {
                    throw new IllegalArgumentException("Expected a DeclaredType while checking ArgumentType field " + quote(member.site.getSimpleName()));
                }
                
                var argumentType = (DeclaredType) type;
                var parameters = argumentType.getTypeArguments();
                if (parameters.isEmpty()) {
                    logger.zone(member.site).warn(member.site.getSimpleName(), "contains a raw type parameter");
                    
                } else {
                    typing.types.isSubtype(parameters.get(0), pointer.site.asType());
                }
            }
        }
    }
    
    boolean find(Command command, Command value) {
        if (command.equals(value)) {
            return true;
        }
        
        var found = false;
        for (var child : command.children.values()) {
            found |= find(child, value);
        }
        
        return found;
    }

}
