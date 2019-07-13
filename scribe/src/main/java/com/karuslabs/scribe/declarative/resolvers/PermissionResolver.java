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
package com.karuslabs.scribe.declarative.resolvers;

import com.karuslabs.scribe.Resolver;
import com.karuslabs.scribe.declarative.*;

import java.util.*;
import java.util.regex.Matcher;
import javax.annotation.processing.Messager;

import static javax.tools.Diagnostic.Kind.WARNING;


public class PermissionResolver extends Resolver<Plugin> {

    public PermissionResolver(Messager messager) {
        super(messager);
    }

    
    @Override
    public void resolve(Plugin plugin, Map<String, Object> map) {
        var permissions = new HashMap<String, Object>();
        var matcher = PERMISSION.matcher("");
        
        for (var permission : plugin.permissions()) {
            resolve(matcher, permission, permissions);
        }
    }
    
    protected void resolve(Matcher matcher, Permission permission, Map<String, Object> permissions) {
        if (!matcher.reset(permission.name()).matches()) {
            messager.printMessage(WARNING, "Potentially malformed permission: " + permission.name());
        }
        
        var information = new HashMap<String, Object>();
        
        if (permission.description() != null) {
            information.put("description", permission.description());
        }
        
        information.put("default", permission.implicit().value);
        
        var children = new HashMap<String, Boolean>();
        for (var child : permission.children().keySet()) {
            children.put(child, true);
        }
        
        information.put("children", children);
        permissions.put(permission.name(), information);
        
        for (var child : permission.children().values()) {
            resolve(matcher, child, permissions);
        }
    }
    
}
