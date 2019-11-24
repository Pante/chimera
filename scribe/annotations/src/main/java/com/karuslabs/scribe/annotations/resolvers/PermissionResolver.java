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
package com.karuslabs.scribe.annotations.resolvers;

import com.karuslabs.scribe.annotations.Permission;
import com.karuslabs.scribe.annotations.processor.Resolver;

import java.util.*;
import java.util.regex.Matcher;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;

import static javax.tools.Diagnostic.Kind.*;


/**
 * A resolver that transforms a {@link Permission} annotation into a {@code permission}
 * section. 
 * 
 * Validation is performed to enforce the following constraints:
 * <ul>
 * <li>All permissions are unique</li>
 * </ul>
 * 
 * In addition, a compile-time warning will be issued in the following circumstances:
 * <ul>
 * <li>A permission inherits itself</li>
 * <li>A permission or its children does not match {@code \w+(\.\w+)*(.\*)?}</li>
 * </ul>
 */
public class PermissionResolver extends Resolver {
    
    Set<String> names;
    Matcher matcher;
    
    
    /**
     * Creates a {@code PermissionResolver} with the given messager.
     * 
     * @param messager the messager
     */
    public PermissionResolver(Messager messager) {
        super(messager);
        names = new HashSet<>();
        matcher = PERMISSION.matcher("");
    }

    
    /**
     * Validates, resolves and adds the element to the {@code permissions} section in the 
     * given results.
     * 
     * @param element the elements to be resolved
     * @param results the results which includes this resolution
     */
    @Override
    protected void resolve(Element element, Map<String, Object> results) {
        var permissions = new HashMap<String, Object>();
        
        for (var permission : element.getAnnotationsByType(Permission.class)) {
            check(element, permission);
            resolve(permission, permissions);
        }
        
        results.put("permissions", permissions);
    }
    
    /**
     * Determines if the given permission satisfies the constraints.
     * 
     * @param element the element
     * @param permission the permission
     */
    protected void check(Element element, Permission permission) {
        String name = permission.value();
        
        checkMalformed(element, permission);
        
        if (Set.of(permission.children()).contains(name)) {
            messager.printMessage(MANDATORY_WARNING, "Self-inheriting permission: '" + name + "'", element);
        }
        
        if (!names.add(name)) {
            messager.printMessage(ERROR, "Conflicting permissions: '" + name + "', permissions must be unique", element);
        }
    }
    
    /**
     * Determines if the given permission and its children are malformed.
     * 
     * @param element the element
     * @param permission the permission
     */
    protected void checkMalformed(Element element, Permission permission) {
        String name = permission.value();
        if (!matcher.reset(name).matches()) {
            messager.printMessage(MANDATORY_WARNING, "Potentially malformed permission: '" + name + "'", element);
        }
        
        for (var child : permission.children()) {
            if (!matcher.reset(child).matches()) {
                messager.printMessage(MANDATORY_WARNING, "Potentially malformed child permission: '" + child + "'", element);
            }
        }
    }
    
    
    /**
     * Resolves and adds the permission to the given permissions.
     * 
     * @param permission the permission to be resolved
     * @param permissions the permissions which includes this resolution
     */
    protected void resolve(Permission permission, Map<String, Object> permissions) {
        var information = new HashMap<String, Object>();
        
        if (!permission.description().isEmpty()) {
            information.put("description", permission.description());
        }
        
        information.put("default", permission.implicit().value);
        
        if (permission.children().length > 0) {
            var children = new HashMap<String, Boolean>();
            for (var child : permission.children()) {
                children.put(child, true);
            }

            information.put("children", children);
        }
        
        permissions.put(permission.value(), information);
    }
    
    
    /**
     * Clears the namespace for permissions.
     */
    @Override
    public void clear() {
        names.clear();
    }
    
}
