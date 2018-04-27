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
package com.karuslabs.plugin.lint.lints;


import org.junit.jupiter.api.*;

import static com.karuslabs.plugin.lint.Yaml.TEST;
import static com.karuslabs.plugin.lint.lints.Lint.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


@TestInstance(PER_CLASS)
class LintTest {
    
    @Test
    void check() {
        NONE.check(TEST, "int");
        assertEquals(message("boolean"), assertThrows(LintException.class, () -> BOOLEAN.check(TEST, "int")).getMessage());
        assertEquals(message("String"), assertThrows(LintException.class, () -> STRING.check(TEST, "int")).getMessage());
        assertEquals(message("List of Strings"), assertThrows(LintException.class, () -> STRING_LIST.check(TEST, "int")).getMessage());
    }
    
    
    static String message(String type) {
        return "Invalid type for: .int, value must be a " + type;
    }
    
}
