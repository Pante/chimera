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
package com.karuslabs.typist.parsers;

import com.karuslabs.typist.annotations.Pack;
import com.karuslabs.typist.Environment;
import com.karuslabs.typist.generation.Out;
import com.karuslabs.utilitary.Logger;
import com.karuslabs.utilitary.type.Find;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.regex.*;
import javax.lang.model.element.Element;

import static com.karuslabs.typist.annotations.Pack.RELATIVE_PACKAGE;

/**
 * A {@code Parser} that parses a package and name from a {@code @Pack} annotation 
 * on an element. The package and name is stored for the subsequent generation of
 * a Java source file.
 */
public class PackageParser implements Parser {

    static final Matcher PACKAGE = Pattern.compile("(([a-zA-Z$_][a-zA-Z\\d$_]+)|[a-zA-Z$])(\\.(([a-zA-Z$_][a-zA-Z\\d$_]+)|[a-zA-Z$]))*").matcher("");
    static final Matcher FILE = Pattern.compile("(([a-zA-Z$_][a-zA-Z\\d$_]+)|[a-zA-Z$])").matcher("");
    
    private final Logger logger;
    
    /**
     * Creates a {@code PackageParser} with the given logger.
     * 
     * @param logger the logger
     */
    public PackageParser(Logger logger) {
        this.logger = logger;
    }
    
    /**
     * Parses the package and name of the Java source file to be generated, from 
     * a {@code @Pack} annotation on an element. The package and name is stored in 
     * the given environment for the subsequent generation of a Java source file. 
     * An error is reported if the given elements does not contain exactly 1 {@code @Pack} 
     * annotation.
     * 
     * @param environment the environment
     * @param elements the annotated elements
     */
    @Override
    public void parse(Environment environment, Set<? extends Element> elements) {
        if (elements.size() == 1) {
            parse(environment, elements.toArray(new Element[0])[0]);
            
        } else if (elements.isEmpty()) {
            logger.error(null, "Project does not contain a @Pack annotation, should contain a @Pack annotation");
            
        } else if (elements.size() > 1) {
            for (var element : elements) {
                logger.error(element, "Project contains " + elements.size() + " @Pack annotations, should contain one @Pack annotation");
            }
        }
    }

    /**
     * Parses the package and name of the Java source file to be generated, from 
     * a {@code @Pack} annotation on an element. The package and name is stored in 
     * the given environment for the subsequent generation of a Java source file.
     * An error is reported if the package or name is invalid.
     * 
     * @param environment the environment
     * @param element the annotated element
     */
    @Override
    public void parse(Environment environment, Element element) {
        var pack = element.getAnnotation(Pack.class);
        if (!RELATIVE_PACKAGE.equals(pack.age()) && !PACKAGE.reset(pack.age()).matches()) {
            logger.error(element, "Invalid package name");
        }
        
        if (!FILE.reset(pack.file()).matches()) {
            logger.error(element, "Invalid file name");
        }
        
        if (logger.error()) {
            return;
        }
        
        var name = RELATIVE_PACKAGE.equals(pack.age()) ? element.accept(Find.PACKAGE, null).getQualifiedName().toString() : pack.age();
        environment.out = new Out(element, name, pack.file());
    }

    @Override
    public Class<? extends Annotation> annotation() {
        return Pack.class;
    }

}
