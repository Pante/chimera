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
import com.karuslabs.scribe.declarative.Plugin;

import java.util.Map;
import javax.annotation.processing.Messager;

import static javax.tools.Diagnostic.Kind.*;


public class PluginResolver extends Resolver<Plugin> {    
    
    public PluginResolver(Messager messager) {
        super(messager);
    }

    
    @Override
    public void resolve(Plugin plugin, Map<String, Object> map) {
        resolveMain(plugin, map);
        resolveName(plugin, map);
        resolveVersion(plugin, map);
        resolveAPIVersion(plugin, map);
    }
    
    protected void resolveMain(Plugin plugin, Map<String, Object> map) {
        try {
            var type = Class.forName(plugin.main());
            if (!org.bukkit.plugin.Plugin.class.isAssignableFrom(type)) {
                messager.printMessage(ERROR, "Invalid main class: " + plugin.main() + ", main class must inherit from " + org.bukkit.plugin.Plugin.class.getName());
            }
            
            var hasEmptyConstructor = false;
            for (var constructor : type.getDeclaredConstructors()) {
                if (constructor.getParameterTypes().length == 0) {
                    hasEmptyConstructor = true;
                    break;
                }
            }
            
            if (!hasEmptyConstructor) {
                messager.printMessage(ERROR, "Invalid main class: " + plugin.main() + ", main class must declare a constructor with no arguments");
            }
            
            map.put("main", plugin.main());
            
        } catch (ClassNotFoundException e) {
            messager.printMessage(ERROR, "Invalid main class: " + plugin.main() + ", main class could not be found");
        }
    }
    
    protected void resolveName(Plugin plugin, Map<String, Object> map) {
        if (!WORD.matcher(plugin.name()).matches()) {
            messager.printMessage(ERROR, "Invalid name: '" + plugin.name() + "', name must contain only alphanumeric characters and '_'");
        }
        
        map.put("name", plugin.name());
    }
    
    protected void resolveVersion(Plugin plugin, Map<String, Object> map) {
        if (!VERSIONING.matcher(plugin.version()).matches()) {
            messager.printMessage(WARNING, "Unconventional versioning scheme: '" + plugin.version() + "', "
                                         + "it is highly recommended that versions follows SemVer, https://semver.org/");
        }
        map.put("version", plugin.version());
    }
    
    protected void resolveAPIVersion(Plugin plugin, Map<String, Object> map) {
        if (plugin.api() != null) {
            map.put("api-version", plugin.api().version);
        }
    }
    
}
