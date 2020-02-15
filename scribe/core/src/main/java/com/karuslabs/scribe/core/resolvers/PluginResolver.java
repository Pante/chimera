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
package com.karuslabs.scribe.core.resolvers;

import com.karuslabs.scribe.annotations.Plugin;
import com.karuslabs.scribe.core.Resolver;

import java.util.*;
import java.util.regex.Matcher;


public abstract class PluginResolver<T> extends Resolver<T> {

    Matcher matcher;
    
    
    public PluginResolver() {
        super(Set.of(Plugin.class));
        matcher = WORD.matcher("");
    }
    
    
    @Override
    protected boolean check(Set<T> types) {
        if (types.isEmpty()) {
            resolution.error("No @Plugin annotation found, plugin must contain a @Plugin annotation");
            return false;
            
        } else if (types.size() > 1) {
            for (var type : types) {
                resolution.error(type, "Invalid number of @Plugin annotations, plugin must contain only one @Plugin annotation");
            }
            return false;
            
        } else {
            return check(new ArrayList<>(types).get(0));
        }
    }
    
    protected abstract boolean check(T type);
    
    
    
    @Override
    protected void resolve(T type) {
        var plugin = extractor.single(type, Plugin.class);
        
        resolution.mappings.put("main", stringify(type));
        
        if (!matcher.reset(plugin.name()).matches()) {
            resolution.error(type, "Invalid name: '" + plugin.name() + "', name must contain only alphanumeric characters and '_'");
        }
        
        resolution.mappings.put("name", plugin.name());
        
        if (!VERSIONING.matcher(plugin.version()).matches()) {
            resolution.warning(
                type, 
                "Unconventional versioning scheme: '" + plugin.version() + "', " +
                "it is highly recommended that versions follows SemVer, https://semver.org/"
            );
        }
        
        resolution.mappings.put("version", plugin.version());
    }
    
    protected abstract String stringify(T type);

}
