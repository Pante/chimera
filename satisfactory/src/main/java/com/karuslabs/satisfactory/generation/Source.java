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
package com.karuslabs.Satisfactory.generation;

import java.util.Collection;

public class Source implements CharSequence {
    
    public static String arguments(Object... parameters) {
        var builder = new StringBuilder().append('(');
        for (int i = 0; i < parameters.length; i++) {
            builder.append(parameters[i]);
            if (i < parameters.length - 1) {
                builder.append(", ");
            }
        }
        return builder.append(')').toString();
    }
    
    public static String arguments(Collection<?> parameters) {
        var builder = new StringBuilder().append('(');
        int i = 0;
        for (var parameter : parameters) {
            builder.append(parameter);
            if (i < parameters.size() - 1) {
                builder.append(", ");
            }
            
            i++;
        }
        
        return builder.append(')').toString();
    }
    
    
    private final StringBuilder builder = new StringBuilder();
    private String indentation = "";
    
    public Source assign(String name, String value) {
        return assign("var", name, value);
    }
    
    public Source assign(String type, String name, String value) {
        builder.append(indentation).append(type).append(" ").append(name).append(" = ").append(value).append(";").append(System.lineSeparator());
        return this;
    }
    
    public Source cast(String name, String type, String value) {
        builder.append(indentation).append("var ").append(name).append(" = (").append(type).append(") ").append(value).append(";").append(System.lineSeparator());
        return this;
    }
    
    public Source pack(String pack) {
        builder.append("package ").append(pack).append(";").append(System.lineSeparator());
        return this;
    }
    
    public Source include(String pack) {
        builder.append("import ").append(pack).append(";").append(System.lineSeparator());
        return this;
    }
    
    
    public Source line() {
        builder.append(System.lineSeparator());
        return this;
    }
    
    public Source line(Object element) {
        builder.append(indentation).append(element).append(System.lineSeparator());
        return this;
    }
    
    public Source line(Object... elements) {
        builder.append(indentation);
        for (var element : elements) {
            builder.append(element);
        }
        
        builder.append(System.lineSeparator());
        return this;
    }
    
    public Source append(Object value) {
        builder.append(value);
        return this;
    }
    
    
    public Source indent() {
        indentation = indentation.isEmpty() ? "    " : indentation + "    ";
        return this;
    }
    
    public Source unindent() {
        indentation = indentation.length() <= 4 ? "" : indentation.substring(0, indentation.length() - 4);
        return this;
    }
    
    public Source indentation(int indentation) {
        this.indentation = " ".repeat(indentation);
        return this;
    }


    @Override
    public char charAt(int index) {
        return builder.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return builder.subSequence(start, end);
    }
    
    @Override
    public int length() {
        return builder.length();
    }
    
    @Override
    public String toString() {
        return builder.toString();
    }

}
