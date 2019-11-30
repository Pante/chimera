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
 * Signifies a plugin's loading phase and sequence.
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE})
public @interface Load {
    
    /**
     * The server initialization phase in which to load this plugin.
     * 
     * @return the phase
     */
    Phase during() default Phase.POSTWORLD;
    
    /**
     * The names of plugins to be loaded <b>after</b> this plugin if available at 
     * runtime. Unavailable plugins will be skipped. A plugin's name can neither 
     * be empty nor contain whitespaces.
     * 
     * @return the names of other plugins
     */
    String[] before() default {};
    
    /**
     * The names of plugins to be loaded <b>before</b> this plugin if available at
     * runtime. Unavailable plugins will prevent this plugin from fully loading.
     * 
     * @return the names of other plugins
     */
    String[] after() default {};
    
    /**
     * The names of plugins to be loaded optionally <b>before</b> this plugin if 
     * available at runtime. 
     * Unavailable plugins will be skipped.
     * 
     * @return the names of other plugins
     */
    String[] optionallyAfter() default {};
    
}
