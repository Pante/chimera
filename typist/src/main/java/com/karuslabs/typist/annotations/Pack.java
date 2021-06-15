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
package com.karuslabs.typist.annotations;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Denotes the package and name of the Java source file is generated. By default, 
 * should no value be specified, the package is inferred to be the annotated type's.
 * The generated Java source file is always named {@code Commands.java}.
 */
@Documented
@Retention(SOURCE)
@Target({PACKAGE, TYPE})
public @interface Pack {

    /**
     * A value which denotes that the package of the generated Java source file
     * should be the same as the annotated type's.
     */
    static final String RELATIVE_PACKAGE = "${relative}";
    /**
     * The default name of the generated Java source file if unspecified.
     */
    static final String FILE_NAME = "Commands";
    
    /**
     * The package in which a Java source file is generated.
     * 
     * @return the package
     */
    String age() default RELATIVE_PACKAGE;
    
    /**
     * The name of the generated Java source file.
     * 
     * @return the name
     */
    String file() default FILE_NAME;

}
