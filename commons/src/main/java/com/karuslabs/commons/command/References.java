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

import java.util.*;


public class References {
    
    private Map<String, Command> commands;
    private Map<String, Completion> completions;
    private Map<String, MessageTranslation> translations;
    
    
    public References() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>());
    }
    
    public References(Map<String, Command> commands, Map<String, Completion> completions, Map<String, MessageTranslation> translations) {
        this.commands = commands;
        this.completions = completions;
        this.translations = translations;
    }

    
    public References command(String key, Command command) {
        commands.put(key, command);
        return this;
    }
    
    public References completion(String key, Completion completion) {
        completions.put(key, completion);
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

    public MessageTranslation getTranslation(String key) {
        return translations.get(key);
    }
    
}
