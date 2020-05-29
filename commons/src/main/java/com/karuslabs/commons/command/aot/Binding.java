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
package com.karuslabs.commons.command.aot;


/**
 * Represents the types that can be bound to a token.
 */
public enum Binding {

    COMMAND("A", "Command<CommandSender>", "command"), 
    TYPE("An", "ArgumentType<?>", "type"),
    REQUIREMENT("A", "Predicate<CommandSender>", "requirement"),
    SUGGESTIONS("A", "SuggestionProvider<CommandSender>", "suggestions");
    
    /**
     * The grammatical article for a binding.
     */
    public final String article;
    /**
     * The type that this binding represents.
     */
    public final String signature;
    private final String value;
    

    private Binding(String article, String signature, String value) {
        this.article = article;
        this.signature = signature;
        this.value = value;
    }
    
    
    @Override
    public String toString() {
        return value;
    }
    
}
