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
package com.karuslabs.commons.command;

import com.karuslabs.commons.command.completion.Completion;
import com.karuslabs.commons.locale.MessageTranslation;

import java.lang.reflect.AnnotatedElement;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;


public class References implements AutoCloseable {
    
    private Map<String, Command> commands;
    private Map<String, Completion> completions;
    private Map<String, MessageTranslation> translations;
    private Map<String, Entry<AnnotatedElement, CommandExecutor>> executors;
    
    
    public References() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
    }
    
    public References(Map<String, Command> commands, Map<String, Completion> completions, Map<String, MessageTranslation> translations, Map<String, Entry<AnnotatedElement, CommandExecutor>> executors) {
        this.commands = commands;
        this.completions = completions;
        this.translations = translations;
        this.executors = executors;
    }

    
    public References command(String key, Command command) {
        commands.put(key, command);
        return this;
    }
    
    public References completion(String key, Completion completion) {
        completions.put(key, completion);
        return this;
    }
    
    
    public References executor(String key, CommandExecutor executor) {
        return executor(key, executor.getClass(), executor);
    }
    
    public References executor(String key, AnnotatedElement element, CommandExecutor executor) {
        return executor(key, new SimpleEntry<>(element, executor));
    }
    
    public References executor(String key, Entry<AnnotatedElement, CommandExecutor> executor) {
        executors.put(key, executor);
        return this;
    }
    
    
    public References translation(String key, MessageTranslation translation) {
        translations.put(key, translation);
        return this;
    }
    
    
    public Command getCommand(String key) {
        return commands.get(key);
    }

    public Completion getCompletion(String key) {
        return completions.get(key);
    }
    
    public Entry<AnnotatedElement, CommandExecutor> getExecutor(String key) {
        return executors.get(key);
    }

    public MessageTranslation getTranslation(String key) {
        return translations.get(key);
    }

    
    @Override
    public void close() {
        commands.clear();
        executors.clear();
        translations.clear();
    }
    
}
