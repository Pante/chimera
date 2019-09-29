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

import com.karuslabs.scribe.annotations.constants.Phase;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Signifies the optional loading phase and sequence of this plugin.
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE})
public @interface Load {
    
    /**
     * The phase of the server initialization in which this plugin is loaded.
     * 
     * @return the phase
     */
    Phase during() default Phase.POSTWORLD;
    
    /**
     * The names of plugins to be loaded <b>after</b> this plugin. Specified plugins 
     * are ignored if unavailable at runtime.
     * 
     * @return the plugins to be loaded after this plugin
     */
    String[] before() default {};
    
    /**
     * The names of plugins to be loaded <b>before</b> this plugin. This plugin
     * will not load if any of the specified plugins is unavailable at runtime.
     * 
     * @return the plugins to be loaded before this plugin
     */
    String[] after() default {};
    
    /**
     * The names of plugins to be optionally loaded <b>before</b> this plugin. 
     * Specified plugins are ignored if unavailable at runtime.
     * 
     * @return the plugins to be optionally loaded before this plugin
     */
    String[] optionallyAfter() default {};
    
}
