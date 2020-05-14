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

import com.karuslabs.scribe.annotations.*;
import com.karuslabs.scribe.core.*;

import java.util.*;
import java.util.regex.Matcher;


public class PermissionResolver<T> extends Resolver<T> {

    Set<String> names;
    Matcher matcher;
    
    
    public PermissionResolver() {
        super(Set.of(Permission.class, Permissions.class));
        names = new HashSet<>();
        matcher = PERMISSION.matcher("");
    }

    @Override
    protected void resolve(T type) {
        var permissions = new HashMap<String, Object>();
        
        for (var permission : extractor.all(type, Permission.class)) {
            check(type, permission);
            resolve(permission, permissions);
        }
        
        resolution.mappings.put("permissions", permissions);
    }
    
    protected void check(T type, Permission permission) {
        String name = permission.value();
        
        checkMalformed(type, permission);
        
        if (Set.of(permission.children()).contains(name)) {
            resolution.warning(type, "Self-inheriting permission: '" + name + "'");
        }
        
        if (!names.add(name)) {
            resolution.error(type, "Conflicting permissions: '" + name + "', permissions must be unique");
        }
    }
    
    protected void checkMalformed(T type, Permission permission) {
        String name = permission.value();
        if (!matcher.reset(name).matches()) {
            resolution.warning(type, "Potentially malformed permission: '" + name + "'");
        }
        
        for (var child : permission.children()) {
            if (!matcher.reset(child).matches()) {
                resolution.warning(type, "Potentially malformed child permission: '" + child + "'");
            }
        }
    }
    
    
    protected void resolve(Permission permission, Map<String, Object> permissions) {
        var information = new HashMap<String, Object>();
        
        if (!permission.description().isEmpty()) {
            information.put("description", permission.description());
        }
        
        information.put("default", permission.implicit().toString());
        
        if (permission.children().length > 0) {
            var children = new HashMap<String, Boolean>();
            for (var child : permission.children()) {
                children.put(child, true);
            }

            information.put("children", children);
        }
        
        permissions.put(permission.value(), information);
    }
    
    
    @Override
    public void clear() {
        names.clear();
    }

}
