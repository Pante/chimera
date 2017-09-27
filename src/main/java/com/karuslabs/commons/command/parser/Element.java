/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.command.parser;

import java.util.Map;
import javax.annotation.Nonnull;

import org.bukkit.configuration.ConfigurationSection;


public abstract class Element<T> {
    
    private Map<String, T> declarations;
    
    
    public Element(@Nonnull Map<String, T> declarations) {
        this.declarations = declarations;
    }
    
    
    public void declare(ConfigurationSection config, String key) {
        declarations.put(key, parse(config, key));
    }

    
    public @Nonnull T parse(ConfigurationSection config, String key) {
        if (config.get(key) == null) {
            return handleNull(config, key);
            
        } else if (config.isString(key)) {
            return getDeclaration(key, config.getCurrentPath());
            
        } else if (check(config, key)) {
            return handle(config, key);
            
        } else {
            throw new ParserException("Invalid value for: " + config.getCurrentPath() + "." + key);
        }
    }
    
    protected @Nonnull T handleNull(@Nonnull ConfigurationSection config, @Nonnull String key) {
        throw new ParserException("Missing key:" + config.getCurrentPath() + "." + key);
    }
    
    public T getDeclaration(String key, String path) {
        T declared = declarations.get(key);
        if (declared != null) {
            return declared;
            
        } else {
            throw new ParserException("Missing declaration for: " + path + "." + key);
        }
    }
    
    protected abstract boolean check(@Nonnull ConfigurationSection config, @Nonnull String key);
    
    protected abstract @Nonnull T handle(@Nonnull ConfigurationSection config, @Nonnull String key);
    
    
    public Map<String, T> getDeclarations() {
        return declarations;
    }
    
}
