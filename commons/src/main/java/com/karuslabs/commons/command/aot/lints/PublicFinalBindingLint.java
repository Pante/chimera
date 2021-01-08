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
package com.karuslabs.commons.command.aot.lints;

import com.karuslabs.commons.command.aot.*;
import com.karuslabs.puff.Logger;
import com.karuslabs.puff.assertion.matches.Match;

import java.util.Set;
import javax.lang.model.element.*;

import static com.karuslabs.puff.assertion.Assertions.contains;
import static javax.lang.model.element.Modifier.*;

public class PublicFinalBindingLint extends TypeLint {

    private final Match<Set<Modifier>> match = contains(PUBLIC, FINAL);

    public PublicFinalBindingLint(Logger logger, Types types) {
        super(logger, types);
    }
    
    // incorrect
    @Override
    public void lint(Environment environment, Command command) {
        for (var binding : command.bindings.values()) {
            if (!match.test(types, binding.site)) {
                var element = binding instanceof VariableElement ? "field should be " : "method should be ";
                logger.error(binding.site, element + match.describe(binding.site));
            }
        }
    }

}
