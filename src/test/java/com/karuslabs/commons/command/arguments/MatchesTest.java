/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.command.arguments;

import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import static com.karuslabs.commons.command.arguments.Matches.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.params.provider.Arguments.of;


@TestInstance(PER_CLASS)
class MatchesTest {
    
    @ParameterizedTest
    @MethodSource("test_parameters")
    void test(Predicate<String> match, String text, boolean expected) {
        assertEquals(expected, match.test(text));
    }
    
    static Stream<Arguments> test_parameters() {
        return Stream.of(
            of(EMPTY, "", true),
            of(EMPTY, "full", false),
            of(BOOLEAN, "FALSE", true),
            of(BOOLEAN, "yes", false),
            of(INT, "-100", true),
            of(INT, "meh", false),
            of(DOUBLE, "-3.142", true),
            of(DOUBLE, "type", false),
            of(FLOAT, "-3.142", true),
            of(FLOAT, "type", false)
        );
    }
    
}
