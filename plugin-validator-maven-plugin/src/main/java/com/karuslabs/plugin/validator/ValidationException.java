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
package com.karuslabs.plugin.validator;

import org.bukkit.configuration.ConfigurationSection;


<<<<<<< HEAD:spigot-plugin-processor-maven-plugin/src/main/java/com/karuslabs/spigot/plugin/processor/ProcessorException.java
/**
 * Thrown to indicate that an error has occurred while processing the document.
 */
public class ProcessorException extends RuntimeException {
    
    /**
     * Constructs a {@code ProcessorException} with the specified detail message.
     * 
     * @param message the detail message
     */
    public ProcessorException(String message) {
        super(message);
    }
        
    /**
     * Constructs a {@code ProcessorException} with the specified {@code ConfigurationSection}, key and type.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @param type the type
     */
    public ProcessorException(ConfigurationSection config, String key, String type) {
=======
public class ValidationException extends RuntimeException {
    
    public ValidationException(String message) {
        super(message);
    }
        
    public ValidationException(ConfigurationSection config, String key, String type) {
>>>>>>> refs/remotes/origin/master:plugin-validator-maven-plugin/src/main/java/com/karuslabs/plugin/validator/ValidationException.java
        super("Invalid type for: " + config.getCurrentPath() + "." + key + ", value must be a " + type);
    }
    
}
