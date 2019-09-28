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
package com.karuslabs.scribe.parser.parslets;

import com.karuslabs.scribe.parser.Parslet;

import java.util.*;
import java.util.regex.Matcher;
import javax.tools.Diagnostic.Kind;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.scribe.parser.Parser.WORD;
import static javax.tools.Diagnostic.Kind.ERROR;


public class LoadParslet implements Parslet {
    
    private Matcher matcher;
    
    
    public LoadParslet() {
        matcher = WORD.matcher("");
    }
    
    
    @Override
    public void parse(Map<String, Object> source, Map<String, Kind> errors) {
        parsePhase(source.get("load"), errors);
        parseList("loadbefore", source.get("loadbefore"), errors);
        parseList("softdepend", source.get("softdepend"), errors);
        parseList("depend", source.get("depend"), errors);
    }
    
    protected void parsePhase(Object value, Map<String, Kind> errors) {
        var phase = (String) value;
        if (phase != null && !phase.equalsIgnoreCase("startup") && !phase.equalsIgnoreCase("postworld")) {
            errors.put("Invalid load phase: '" + phase + "', load must be either STARTUP or POSTWORLD", ERROR);
        }
    }
    
    protected void parseList(String type, @Nullable Object value, Map<String, Kind> errors) {
        if (value != null) {
            if (value instanceof List<?>) {
                var names = (List<String>) value;
                for (var name : names) {
                    if (!matcher.reset(name).matches()) {
                        errors.put("Invalid name: '" + name + "', " + type + " must contain only alphanumeric characters and '_'", ERROR);
                    }
                }
                
            } else {
                errors.put("Wrong type for 'permission', expected: 'List<String>', actual: '" +  value.getClass() + "'", ERROR);
            }
        }
    }
    
}
