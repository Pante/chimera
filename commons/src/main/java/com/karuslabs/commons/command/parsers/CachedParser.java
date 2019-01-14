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
package com.karuslabs.commons.command.parsers;

import com.google.common.cache.*;

import com.karuslabs.commons.command.Dispatcher;

import com.mojang.brigadier.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.bukkit.command.CommandSender;


public class CachedParser implements Parser {
    
    private static final Supplier<Cache<String, ParseResults<CommandSender>>> SUPPLIER = () -> CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).build();
    
    
    protected Map<CommandSender, Cache<String, ParseResults<CommandSender>>> cache;
    protected Supplier<Cache<String, ParseResults<CommandSender>>> supplier;
    
    
    public CachedParser() {
        this(SUPPLIER);
    }
    
    public CachedParser(Supplier<Cache<String, ParseResults<CommandSender>>> supplier) {
        this.cache = new WeakHashMap<>();
        this.supplier = supplier;
    }
    
    
    @Override
    public ParseResults<CommandSender> accept(Dispatcher dispatcher, StringReader reader, CommandSender source) {
        ParseResults<CommandSender> results = null;
        
        var mapping = cache.get(source);
        if (mapping == null) {
            mapping = supplier.get();
            cache.put(source, mapping);
            
        } else {
            results = mapping.getIfPresent(reader.getString());
        }
        
        
        if (results == null) {
            results = dispatcher.defaultParse(reader, source);
            mapping.put(reader.getString(), results);
        }
        
        return results;
    }
    
    
    public Map<CommandSender, Cache<String, ParseResults<CommandSender>>> cache() {
        return cache;
    }
    
}
