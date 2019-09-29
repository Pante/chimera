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
 * Signifies an optional {@code permission} attribute and its description, default
 * value and children.
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE})
@Repeatable(Permissions.class)
public @interface Permission {
    
    /**
     * The permission.
     * 
     * @return the permission
     */
    String value();
    
    /**
     * A description of this permission.
     * 
     * @return a description of this permission
     */
    String description() default "";
    
    /**
     * The default value of this permission.
     * 
     * @return the default value of this permission
     */
    Default implicit() default Default.OP;
    
    /**
     * The children of this permission. Each child inherits its default value from
     * this parent permission.
     * 
     * @return the children of this permission
     */
    String[] children() default {};
    
}
