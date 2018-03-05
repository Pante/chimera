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
package com.karuslabs.command.validator;

import com.karuslabs.command.validator.Validation;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.command.validator.Validation.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;


class ValidationTest {
    
    @ParameterizedTest
    @MethodSource({"description_parameters"})
    void getDescription(Validation validation, String expected) {
        assertEquals(expected, validation.getDescription());
    }
    
    static Stream<Arguments> description_parameters() {
        return Stream.of(
            of(LENIENT, "unresolvable references will be ignored"),
            of(WARNING, "warnings will be issued for unresolvable references"),
            of(STRICT, "exceptions will be thrown for unresolvable references")
        );
    }
    
}
