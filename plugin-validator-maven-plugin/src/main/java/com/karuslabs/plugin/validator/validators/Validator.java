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
package com.karuslabs.plugin.validator.validators;

import com.karuslabs.plugin.validator.ValidationException;

import org.bukkit.configuration.ConfigurationSection;


/**
 * Represents a processor which checks if a value in the {@code ConfigurationSection} is of a type.
 */
@FunctionalInterface
public interface Validator {
    
<<<<<<< HEAD:spigot-plugin-processor-maven-plugin/src/main/java/com/karuslabs/spigot/plugin/processor/processors/Processor.java
    /**
     * Checks if the value associated with the specified key is of a type.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     */
    public void execute(ConfigurationSection config, String key);
    
    
    /**
     * A processor which checks if the value associated with the specified key is a boolean.
     */
    public static final Processor BOOLEAN = (config, key) -> {
=======
    public void validate(ConfigurationSection config, String key);
    
    
    public static final Validator BOOLEAN = (config, key) -> {
>>>>>>> refs/remotes/origin/master:plugin-validator-maven-plugin/src/main/java/com/karuslabs/plugin/validator/validators/Validator.java
        if (!config.isBoolean(key)) {
            throw new ValidationException(config, key, "boolean");
        }
    };
    
<<<<<<< HEAD:spigot-plugin-processor-maven-plugin/src/main/java/com/karuslabs/spigot/plugin/processor/processors/Processor.java
    /**
     * A processor which checks if the value associated with the specified key is a String.
     */
    public static final Processor STRING = (config, key) -> {
=======
    public static final Validator STRING = (config, key) -> {
>>>>>>> refs/remotes/origin/master:plugin-validator-maven-plugin/src/main/java/com/karuslabs/plugin/validator/validators/Validator.java
        if (!config.isString(key)) {
            throw new ValidationException(config, key, "String");
        }
    };
    
<<<<<<< HEAD:spigot-plugin-processor-maven-plugin/src/main/java/com/karuslabs/spigot/plugin/processor/processors/Processor.java
    /**
     * A processor which checks if the value associated with the specified key is a list of Strings.
     */
    public static final Processor STRING_LIST = (config, key) -> {
=======
    public static final Validator STRING_LIST = (config, key) -> {
>>>>>>> refs/remotes/origin/master:plugin-validator-maven-plugin/src/main/java/com/karuslabs/plugin/validator/validators/Validator.java
        if (!config.isList(key)) {
            throw new ValidationException(config, key, "List of Strings");
        }
    };
    
<<<<<<< HEAD:spigot-plugin-processor-maven-plugin/src/main/java/com/karuslabs/spigot/plugin/processor/processors/Processor.java
    /**
     * An empty processor.
     */
    public static final Processor NONE = (config, key) -> {};
=======
    public static final Validator NONE = (config, key) -> {};
>>>>>>> refs/remotes/origin/master:plugin-validator-maven-plugin/src/main/java/com/karuslabs/plugin/validator/validators/Validator.java
    
}
