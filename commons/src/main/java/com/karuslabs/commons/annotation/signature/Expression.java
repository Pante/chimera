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
package com.karuslabs.commons.annotation.signature;

import java.util.regex.Pattern;


public abstract class Expression {
        
    public static final Expression ANY = new Expression() {
        @Override
        public boolean matches(CharSequence name) {
            return true;
        }
    };
    
    public static Expression exact(String name) {
        return new ExactExpression(name);
    }
    
    public static Expression regex(String regex) {
        return new RegexExpression(regex);
    }
    
    
    public abstract boolean matches(CharSequence name);
    
    
    static class ExactExpression extends Expression {
        
        private String name;
        
        
        ExactExpression(String name) {
            this.name = name;
        }
        
        
        @Override
        public boolean matches(CharSequence name) {
            return this.name.equals(name);
        }
        
    }
    
    static class RegexExpression extends Expression {
        
        private Pattern expression;
        
        
        RegexExpression(String expression) {
            this.expression = Pattern.compile(expression);
        }
        
        
        @Override
        public boolean matches(CharSequence name) {
            return expression.matcher(name).matches();
        }
        
    }
    
}
