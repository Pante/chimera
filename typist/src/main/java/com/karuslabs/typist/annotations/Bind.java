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
package com.karuslabs.typist.annotations;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Binds the annotated fields and methods to the given commands. The part of command
 * to which the annotated field or method, is bound is inferred using its signature.
 * <br><br>
 * The annotated target may be bound to multiple commands and commands that matches 
 * a given pattern. A pattern is defined as a sequence of arguments and literals.
 * <br><br>
 * For example, given the following commands, {@code "tell <player> <message>", "broadcast <message> <location>"}
 * a binding pattern of {@code <message>} will be applied to the {@code <message>} arguments
 * in the both commands.
 * <br><br>
 * A command should match {@code literal ( argument|literal)*}. In which, an argument 
 * should match {@code <name>} while a literal should match {@code name(|alias)*}.
 */
@Documented
@Retention(SOURCE)
@Target({FIELD, METHOD})
public @interface Bind {
    
    /**
     * The commands to which this annotated target should be bound.
     * 
     * @return the commands
     */
    String[] value() default {};
    
    /**
     * The patterns that commands to which this annotation target is bound should match.
     * 
     * @return the patterns
     */
    String[] pattern() default {};
    
}
