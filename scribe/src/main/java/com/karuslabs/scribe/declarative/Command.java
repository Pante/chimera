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
package com.karuslabs.scribe.declarative;


public class Command {
    
    private static final String[] EMPTY = new String[] {};
    
    
    private String name;
    private String[] aliases;
    private String description;
    private String syntax;
    private String permission;
    private String message;
    
    
    public Command(String name) {
        this.name = name;
        aliases = EMPTY;
        description = "";
        syntax = "";
        permission = "";
        message = "";
    }
    
    
    public Command aliases(String... aliases) {
        this.aliases = aliases;
        return this;
    }
    
    public Command description(String description) {
        this.description = description;
        return this;
    }
    
    public Command syntax(String syntax) {
        this.syntax = syntax;
        return this;
    }
    
    public Command permission(String permission) {
        this.permission = permission;
        return this;
    }
    
    public Command message(String message) {
        this.message = message;
        return this;
    }
    

    public String name() {
        return name;
    }

    public String[] aliases() {
        return aliases;
    }

    public String description() {
        return description;
    }

    public String syntax() {
        return syntax;
    }

    public String permission() {
        return permission;
    }

    public String message() {
        return message;
    }
    
}
