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

import com.karuslabs.plugin.validator.validators.*;

import java.util.*;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import org.bukkit.configuration.ConfigurationSection;

import static com.karuslabs.plugin.validator.validators.Validator.*;


public class Validators {
    
    Map<String, Validator> required;
    Map<String, Validator> validators;
    
    
    public Validators(Map<String, Validator> required, Map<String, Validator> validators) {
        this.required = required;
        this.validators = validators;
    }
    
    
    public void validate(ConfigurationSection config) throws MojoExecutionException {
        required.forEach((key, processor) -> processor.validate(config, key));
        
        Set<String> keys = config.getKeys(false);
        if (validators.keySet().containsAll(keys)) {
            try {
                keys.forEach(key -> {
                    Validator descriptor = validators.get(key);
                    if (descriptor != null) {
                        descriptor.validate(config, key);
                    }
                });
                
            } catch (ValidationException e) {
                throw new MojoExecutionException(e.getMessage(), e);
            }
            
        } else{
            keys.removeAll(validators.keySet());
            throw new MojoExecutionException("Invalid keys: " + keys.toString() + " in plugin.yml, key must be valid: " + validators.keySet());
        }
    }
    
    
    public static Validators simple(Log log, List<String> elements) {
        Map<String, Validator> required = new HashMap<>();
        required.put("name", STRING);
        required.put("version", STRING);
        required.put("main", STRING);
        
        Map<String, Validator> validators = new HashMap<>();
        validators.put("name", NONE);
        validators.put("version", NONE);
        validators.put("main", NONE);
        validators.put("description", STRING);
        validators.put("load", new EnumerationValidator("STARTUP", "POSTWORLD"));
        validators.put("author", STRING);
        validators.put("authors", STRING_LIST);
        validators.put("website", STRING);
        validators.put("database", BOOLEAN);
        validators.put("depend", STRING_LIST);
        validators.put("prefix", STRING);
        validators.put("softdepend", STRING_LIST);
        validators.put("loadbefore", STRING_LIST);
        validators.put("commands", new DelegateValidator(new CommandValidator()));
        validators.put("permissions", new DelegateValidator(new PermissionValidator()));
        
        return new Validators(required, validators);
    }
    
}
