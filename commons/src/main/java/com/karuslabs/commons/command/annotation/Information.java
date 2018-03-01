/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.command.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Represents the information for the commands to which the annotated {@code CommandExecutor} are registered.
 */
@Documented
@Target({TYPE})
@Retention(RUNTIME)
public @interface Information {
    
    /**
     * The aliases for the command(s).
     * 
     * The default value is empty.
     * 
     * @return the aliases.
     */
    String[] aliases() default {};
    
    /**
     * The description for the command(s).
     * 
     * The default value is empty.
     * 
     * @return the description
     */
    String description() default "";
    
    /**
     * The permission for the command(s).
     * 
     * The default value is empty.
     * 
     * @return the permission
     */
    String permission() default "";
    
    /**
     * The permission message for the command(s).
     * 
     * The default value is empty.
     * 
     * @return the permission message
     */
    String message() default "";
    
    /**
     * The usage for the command(s).
     * 
     * The default value is empty.
     * 
     * @return the usage
     */
    String usage() default "";
    
}
