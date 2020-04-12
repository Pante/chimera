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
package com.karuslabs.commons.command.annotations.lint;

import com.karuslabs.annotations.VisibleForOverride;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;

import static javax.tools.Diagnostic.Kind.*;


public class Parser {
    
    static final Parser COMMAND = new Parser();
    static final Parser ARGUMENT = new ArgumentParser();
    static final Parser LITERAL = new LiteralParser();
    
    static final String[] EMPTY = new String[0];
    
    
    public String[] parse(Messager messager, String command, Element element) {
        if (command.isBlank()) {
            messager.printMessage(ERROR, "Invalid command: '" + command + "', command cannot be blank", element);
            return EMPTY;
        }
        
        if (command.startsWith(" ") || command.endsWith(" ")) {
            messager.printMessage(WARNING, "Command: '" + command + "' ", element);
        }
        
        var parts = command.split("\\s+");
        var error = false;
        
        for (var part : parts) {
            if (ambigious(part)) {
                messager.printMessage(
                    ERROR, 
                    "Invalid comand syntax: '" + part + " in '" + command + "', arguments must be enclosed by '<' and '>' while literals must not", 
                    element
                );
                error = true;
            }
        }
        
        error = error || !valid(messager, command, parts[parts.length - 1], element);
        return error ? EMPTY : parts;
    }
    
    boolean ambigious(String argument) {
        return (argument.startsWith("<") && !argument.endsWith(">"))
            || (!argument.startsWith("<") && argument.endsWith(">"));
    }
    
    @VisibleForOverride
    boolean valid(Messager messager, String command, String last, Element element) {
        return true;
    }
    
}

class ArgumentParser extends Parser {
    
    @Override
    boolean valid(Messager messager, String command, String last, Element element) {
        if (last.startsWith("<") && last.endsWith(">")) {
            return true;
        }
        
        messager.printMessage(ERROR, "Invalid command syntax: '" + last + "' in '" + command +"', an argument must be enclosed by '<' and '>'");
        return false;
    }
    
}

class LiteralParser extends Parser {
    
    @Override
    boolean valid(Messager messager, String command, String last, Element element) {
        if (!last.startsWith("<") || !last.endsWith(">")) {
            return true;
        }
        
        messager.printMessage(ERROR, "Invalid command syntax: '" + last + "' in '" + command +"', a liateral cannot be enclosed by '<' and '>'");
        return false;
    }
    
}



