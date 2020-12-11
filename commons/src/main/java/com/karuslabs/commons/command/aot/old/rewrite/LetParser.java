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
package com.karuslabs.commons.command.aot.old.rewrite;

import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.annotations.Let;
import com.karuslabs.commons.command.aot.lexers.Lexer;
import com.karuslabs.commons.command.aot.parsers.NamespaceParser;
import com.karuslabs.puff.Logger;

import java.util.Map;
import javax.lang.model.element.Element;

public class LetParser extends NamespaceParser {

    public LetParser(Logger logger, Lexer lexer) {
        super(logger, lexer);
    }

    @Override
    protected void parse(Element element, Map<Identity, Command> namespace) {
        var line = element.getAnnotation(Let.class).value();
        if (line.equals(Let.INFERRED_ARGUMENT)) {
            line = "<" + element.getSimpleName().toString() + ">";
        }
        
        var tokens = lexer.lex(logger, element, line);
        if (tokens.isEmpty()) {
            return;
        }
        
        Command command = null;
        for (var token : tokens) {
            
        }
    }

}
