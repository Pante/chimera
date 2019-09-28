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
import javax.tools.Diagnostic.Kind;

import static javax.tools.Diagnostic.Kind.ERROR;


public class InformationParslet implements Parslet {

    @Override
    public void parse(Map<String, Object> source, Map<String, Kind> errors) {
       parseAuthor(source, errors);
       parseAuthors(source, errors);
    }
    
    protected void parseAuthor(Map<String, Object> source, Map<String, Kind> errors) {
        var author = source.get("author");
        if (author != null && !(author instanceof String)) {
            errors.put("Wrong type for 'permission', expected: 'String', actual: '" +  author.getClass() + "'", ERROR);
        }
    }
    
    protected void parseAuthors(Map<String, Object> source, Map<String, Kind> errors) {
        var authors = source.get("authors");
        if (authors != null && !(authors instanceof List<?>)) {
            errors.put("Wrong type for 'permission', expected: 'List<String>', actual: '" +  authors.getClass() + "'", ERROR);
        }
    }
    
}
