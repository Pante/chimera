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
package com.karuslabs.commons.command.aot.semantics;

import com.karuslabs.annotations.processor.Filter;

import com.karuslabs.commons.command.aot.Environment;
import com.karuslabs.commons.command.aot.annotations.Bind;
import com.karuslabs.commons.command.aot.lexers.Lexer;
import com.karuslabs.commons.command.aot.tokens.Token;

import java.util.*;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.util.*;


public class BindAnalyzer extends SyntaxAnalyzer {

    List<Token> tokens;
    
    
    public BindAnalyzer(Lexer lexer, Environment environment, Messager messager, Elements elements, Types types) {
        super(lexer, environment, messager, elements, types);
        tokens = new ArrayList<>();
    }

    @Override
    public boolean analyze(Element element) {
        environment.initialize(this);
        this.element = element;
        var success = true;
        
        for (var value : element.getAnnotation(Bind.class).value()) {
            if (!lexer.lex(this, element, value, value)) {
                success = false;
            }
             
            environment.scope(element.accept(Filter.CLASS, null));
            if (tokens.size() > 1) {
                success &= qualified(element);
                
            } else {
                success &= local(element);
            }
            
            tokens.clear();
        }
        
        return success;
    }
    
    protected boolean qualified(Element element) {
        // TODO:
        return false;
    }
    
    protected boolean local(Element element) {
        // TODO:
        return false;
    }

}
