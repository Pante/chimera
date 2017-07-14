/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.command.completion;

import com.karuslabs.commons.command.argument.Arguments;
import com.karuslabs.commons.command.*;

import java.util.*;
import static java.util.stream.Collectors.toList;


public class ListCompleter implements Completer {
    
    private List<String> possibilities;
    
    
    public ListCompleter(List<String> possibilities) {
        this.possibilities = possibilities;
    }
    
    
    @Override
    public List<String> complete(CommandContext context, Arguments args) {
        String argument = args.getLastString();
        if (argument.isEmpty()) {
            return Collections.EMPTY_LIST;
            
        } else {
            return possibilities.stream().filter(possibility -> possibility.startsWith(argument)).collect(toList());
        }
    }
    
    
    public List<String> getPossibilities() {
        return possibilities;
    }
    
}
