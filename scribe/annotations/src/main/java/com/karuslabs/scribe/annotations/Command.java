/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.scribe.annotations;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Signifies an optional {@code command} attribute and its aliases, description,
 * syntax, permission and permission message.
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE})
@Repeatable(Commands.class)
public @interface Command {
    
    /**
     * The name of this command which can neither be empty nor contain spaces.
     * 
     * @return the command name
     */
    String name();
    
    /**
     * The aliases of this command which can neither be empty nor contain spaces.
     * 
     * @return the command aliases
     */
    String[] aliases() default {};
    
    /**
     * A description of this command. 
     * 
     * @return the description of this command
     */
    String description() default "";
    
    /**
     * The syntax of this command.
     * 
     * @return the syntax of this command
     */
    String syntax() default "";
    
    
    /**
     * The permission necessary to execute this command.
     * 
     * @return the permission necessary to execute this command
     */
    String permission() default "";
    
    /**
     * A message to be displayed when the command is executed without the necessary 
     * permission defined by {@link #permission()}.
     * 
     * @return the message to be displayed when this command is executed unpermitted
     */
    String message() default "";
    
}
