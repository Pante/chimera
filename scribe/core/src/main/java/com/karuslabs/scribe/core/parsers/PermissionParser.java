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
package com.karuslabs.scribe.core.parsers;

import com.karuslabs.scribe.annotations.*;
import com.karuslabs.scribe.core.Environment;

import java.util.*;
import java.util.regex.Matcher;

import static com.karuslabs.annotations.processor.Messages.format;


public class PermissionParser<T> extends Parser<T> {

    Set<String> names;
    Matcher matcher;
    
    
    public PermissionParser(Environment<T> environment) {
        super(environment, Set.of(Permission.class, Permissions.class));
        names = new HashSet<>();
        matcher = PERMISSION.matcher("");
    }

    @Override
    protected void parse(T type) {
        var permissions = new HashMap<String, Object>();
        
        for (var permission : environment.resolver.all(type, Permission.class)) {
            check(type, permission);
            parse(permission, permissions);
        }
        
        environment.mappings.put("permissions", permissions);
    }
    
    protected void check(T type, Permission permission) {
        String name = permission.value();
        
        checkMalformed(type, permission);
        
        if (Set.of(permission.children()).contains(name)) {
            environment.warn(type, format(name, "inherits itself"));
        }
        
        if (!names.add(name)) {
            environment.error(type, format(name, "already exists"));
        }
    }
    
    protected void checkMalformed(T type, Permission permission) {
        String name = permission.value();
        if (!matcher.reset(name).matches()) {
            environment.warn(type, format(name, "may be malformed"));
        }
        
        for (var child : permission.children()) {
            if (!matcher.reset(child).matches()) {
                environment.warn(type, format(child, "may be malformed"));
            }
        }
    }
    
    
    protected void parse(Permission permission, Map<String, Object> permissions) {
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
