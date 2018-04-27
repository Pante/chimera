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
package com.karuslabs.plugin.lint;

import com.karuslabs.plugin.lint.lints.*;

import java.util.*;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import org.bukkit.configuration.ConfigurationSection;

import static com.karuslabs.plugin.lint.lints.Lint.*;
import com.karuslabs.plugin.lint.lints.Lint;


public class Lints {
    
    Map<String, Lint> required;
    Map<String, Lint> validators;
    
    
    public Lints(Map<String, Lint> required, Map<String, Lint> validators) {
        this.required = required;
        this.validators = validators;
    }
    
    
    public void check(ConfigurationSection config) throws MojoExecutionException {
        required.forEach((key, processor) -> processor.check(config, key));
        
        Set<String> keys = config.getKeys(false);
        if (validators.keySet().containsAll(keys)) {
            try {
                keys.forEach(key -> {
                    Lint descriptor = validators.get(key);
                    if (descriptor != null) {
                        descriptor.check(config, key);
                    }
                });
                
            } catch (LintException e) {
                throw new MojoExecutionException(e.getMessage(), e);
            }
            
        } else{
            keys.removeAll(validators.keySet());
            throw new MojoExecutionException("Invalid keys: " + keys.toString() + " in plugin.yml, key must be valid: " + validators.keySet());
        }
    }
    
    
    public static Lints simple(Log log, List<String> elements) {
        Map<String, Lint> required = new HashMap<>(3);
        required.put("name", STRING);
        required.put("version", STRING);
        required.put("main", STRING);
        
        Map<String, Lint> lints = new HashMap<>(15);
        lints.put("name", NONE);
        lints.put("version", NONE);
        lints.put("main", NONE);
        lints.put("description", STRING);
        lints.put("load", new EnumerationLint("STARTUP", "POSTWORLD"));
        lints.put("author", STRING);
        lints.put("authors", STRING_LIST);
        lints.put("website", STRING);
        lints.put("database", BOOLEAN);
        lints.put("depend", STRING_LIST);
        lints.put("prefix", STRING);
        lints.put("softdepend", STRING_LIST);
        lints.put("loadbefore", STRING_LIST);
        lints.put("commands", new DelegateLint(new CommandLint()));
        lints.put("permissions", new DelegateLint(new PermissionLint()));
        
        return new Lints(required, lints);
    }
    
}
