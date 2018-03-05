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
package com.karuslabs.plugin.annotations.annotations;

import java.lang.annotation.*;

import static com.karuslabs.plugin.annotations.annotations.Default.OP;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Represents a permission.
 */
@Documented
@Target({TYPE})
@Retention(RUNTIME)
@Repeatable(Permissions.class)
public @interface Permission {
    
     /**
      * The name of the permission.
      * 
      * @return the name
      */
    String name();
     
    /**
     * The description of the permission.
     * 
     * The default is empty.
     * 
     * @return the description
     */
    String description() default "";
    
    /**
     * The default value for the permission.
     * 
     * THe default is {@code OP}.
     * 
     * @return the value
     */
    Default value() default OP;
    
    /**
     * The children of the permission.
     * 
     * The default is none.
     * 
     * @return the children
     */
    Child[] children() default {};
    
}
