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
 * Represents the namespace of a command to which the annotated {@code CommandExecutor} is registered.
 * i.e. {@code @Namespace({"parent", "child"})} will register the annotated {@code CommandExecutor} to 
 * the subcommand, {@code "child"} of the command, {@code "parent"}.
 * 
 * This annotation may be validated at compile-time using the {@link com.karuslabs.commons.command.annotation.checkers.NamespaceChecker}.
 */
@Documented
@Target({TYPE})
@Retention(RUNTIME)
@Repeatable(Namespaces.class)
public @interface Namespace {
    
    /**
     * The namespace of the command.
     * 
     * The namespace cannot be empty.
     * 
     * @return the namespace
     */
    String[] value();
    
}
