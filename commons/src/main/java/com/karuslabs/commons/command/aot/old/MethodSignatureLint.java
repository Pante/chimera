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
package com.karuslabs.commons.command.aot.old;

import com.karuslabs.commons.command.aot.Command;
import com.karuslabs.smoke.Logger;
import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.old.Mirrors.*;
import com.karuslabs.commons.command.aot.old.Signature;

import static javax.lang.model.element.Modifier.PUBLIC;

public class MethodSignatureLint implements Lint {
    
    private final Signature command;
    private final Signature executable;
    private final Signature requirement;
    private final Signature suggestionProvider;
    
    public MethodSignatureLint(Signature command, Signature executable, Signature requirement, Signature suggestionProvider) {
        this.command = command;
        this.executable = executable;
        this.requirement = requirement;
        this.suggestionProvider = suggestionProvider;
    }
    
    @Override
    public void lint(Logger logger, Identity identifier, Command command) {
        for (var member : command.members.values()) {
            if (member instanceof Method) {
                var method = (Method) member;
                if (!method.site.getModifiers().contains(PUBLIC)) {
                    logger.zone(member.site).error("Method should be public");
                }
                
                switch (method.type) {
                    case COMMAND:
                        this.command.lint(logger, method, method.site);
                        
                    case EXECUTABLE:
                        executable.lint(logger, method, method.site);
                        
                    case REQUIREMENT:
                        requirement.lint(logger, method, method.site);
                        
                    case SUGGESTION_PROVIDER:
                        suggestionProvider.lint(logger, method, method.site);
                        
                    default:
                        logger.zone(method.site).error("A " + method.type + " cannot be bound to a method");
                }
            }
        }
    }
    
}

