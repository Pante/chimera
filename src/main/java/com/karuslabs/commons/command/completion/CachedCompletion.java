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

import java.util.List;
import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;


public class CachedCompletion implements Completion {
    
    private List<String> completions;
    
    
    public CachedCompletion(String... completions) {
        this(asList(completions));
    }
    
    public CachedCompletion(List<String> completions) {
        this.completions = completions;
    }
    
    
    @Override
    public @Nonnull List<String> complete(CommandSender sender, String argument) {
        return completions.stream().filter(possibility -> possibility.startsWith(argument)).collect(toList());
    }
    
    
    public List<String> getCompletions() {
        return completions;
    }
    
}
