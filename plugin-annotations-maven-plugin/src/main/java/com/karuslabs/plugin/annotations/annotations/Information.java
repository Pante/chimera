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

import static com.karuslabs.plugin.annotations.annotations.Load.POSTWORLD;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Represents the information about a command.
 */
@Documented
@Target({TYPE})
@Retention(RUNTIME)
public @interface Information {
    
    /**
     * The description for the plugin.
     * 
     * The default is the project description.
     * 
     * @return the description
     */
    String description() default "${project.description}";
    
    /**
     * The author of the plugin.
     * 
     * The default is the author.
     * 
     * @return the auhtor
     */
    String author() default "";
    
    /**
     * The authors of the plugin.
     * 
     * The default is the project developers.
     * 
     * @return the authors
     */
    String[] authors() default {"${project.developers}"};
    
    /**
     * The prefix for the plugin.
     * 
     * The default is empty.
     * 
     * @return the prefix
     */
    String prefix() default "";
    
    /**
     * The website for the plugin.
     * 
     * The default is the project URL.
     * 
     * @return the website
     */
    String website() default "${project.url}";
    
    /**
     * Whether the plugin uses a database.
     * 
     * The default is {@code false}.
     * 
     * @return true if the plugin uses a database; else false
     */
    boolean database() default false;
    
    /**
     * The dependencies for the plugin.
     * 
     * The default is none.
     * 
     * @return the dependencies
     */
    String[] depend() default {};
    
    /**
     * The soft dependencies for the plugin.
     * 
     * The default is none.
     * 
     * @return the soft dependencies
     */
    String[] softdepend() default {};
    
    /**
     * The plugins to load before the plugin.
     * 
     * The default is none.
     * 
     * @return the plugins
     */
    String[] loadbefore() default {};
    
    /**
     * The period when the plugin should be loaded,
     * 
     * The default is {@code POSTWORLD}.
     * 
     * @return the period when the plugin should be loaded
     */
    Load load() default POSTWORLD;
    
}
