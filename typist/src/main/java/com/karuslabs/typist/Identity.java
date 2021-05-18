
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
package com.karuslabs.typist;

import java.util.*;

/**
 * Represents the identity of a command.
 */
public final class Identity {
    
    /**
     * The command's type.
     */
    public final Type type;
    /**
     * The command's name.
     */
    public final String name;
    
    /**
     * Creates an identity with the givne type and name.
     * 
     * @param type the type
     * @param name the name
     */
    public Identity(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        
        if (!(other instanceof Identity)) {
            return false;
        } 
        
        var identity = (Identity) other;
        return type == identity.type && name.equals(identity.name);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.type);
        hash = 59 * hash + Objects.hashCode(this.name);
        return hash;
    }
    
    @Override
    public String toString() {
        return type.format(name);
    }
    
    /**
     * Represents the type of a command.
     */
    public static enum Type {
        
        ARGUMENT("An", "argument"), LITERAL("A", "literal");
        
        /**
         * The article used to describe this type.
         */
        public final String article;
        /**
         * The noun used to describe this pattern.
         */
        public final String noun;
        
        private Type(String article, String noun) {
            this.article = article;
            this.noun = noun;
        }
        
        /**
         * Formats the given value.
         * 
         * @param value the value to be formatted
         * @return the formatted value
         */
        public String format(String value) {
            switch (this) {
                case ARGUMENT:
                    return "<" + value + ">";
                default:
                    return value;
            }
        }
        
        @Override
        public String toString() {
            return noun;
        }
    }
    
}
