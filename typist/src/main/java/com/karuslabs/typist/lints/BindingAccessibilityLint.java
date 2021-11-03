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
package com.karuslabs.typist.lints;

import com.karuslabs.utilitary.Logger;
import com.karuslabs.satisfactory.Assertion;
import com.karuslabs.typist.*;

import java.util.Set;
import javax.lang.model.element.*;

import static com.karuslabs.satisfactory.Assertions.contains;
import static javax.lang.model.element.Modifier.*;

/**
 * A {@code Lint} which verifies that all fields and methods annotated with {@code Bind}
 * are public and final, and public respectively.
 */
public class BindingAccessibilityLint extends TypeLint {

    private final Assertion<Set<Modifier>> variable = contains(PUBLIC, FINAL);
    private final Assertion<Set<Modifier>> executable = contains(PUBLIC);

    /**
     * Creates a {@code PublicFinalBindingLint} with the given logger and types.
     * 
     * @param logger the logger used to report errors
     * @param types the types used in the assertion
     */
    public BindingAccessibilityLint(Logger logger, Types types) {
        super(logger, types);
    }
    
    @Override
    public void lint(Environment environment, Command command) {
        for (var binding : command.bindings().values()) {
            var site = binding.site();
            if (site instanceof VariableElement && !variable.test(types, site.getModifiers())) {
                logger.error(site, "Field should be public and final");
                
            } else if (site instanceof ExecutableElement && !executable.test(types, site.getModifiers())) {
                logger.error(site, "Method should be public");
            }
        }
    }
}

