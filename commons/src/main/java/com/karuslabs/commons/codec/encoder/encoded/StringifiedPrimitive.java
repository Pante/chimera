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
package com.karuslabs.commons.codec.encoder.encoded;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

import com.karuslabs.commons.codec.encoder.Encoded;
import com.karuslabs.commons.codec.nodes.SparseArrayNode;
import java.util.Arrays;

import java.util.regex.Pattern;


public abstract class StringifiedPrimitive<T, R extends JsonNode> implements Encoded<T, R> {
    
    private static final LenientStringifiedPrimitive LENIENT = new LenientStringifiedPrimitive();
    private static final StrictStringifiedPrimitive STRICT = new StrictStringifiedPrimitive();
    
    
    public static LenientStringifiedPrimitive lenient() {
        return LENIENT;
    }
    
    public static StrictStringifiedPrimitive unstringify() {
        return STRICT;
    }
    
    
    private static final Pattern LONG = Pattern.compile("(([+-]?)[\\d]+)");
    // Copied from the JavaDoc for Double#parseDouble(double)
    private static final String DIGITS = "(\\p{Digit}+)";
    private static final String HEX = "(\\p{XDigit}+)";
    private static final String EXP = "[eE][+-]?" + DIGITS;
    private final Pattern DOUBLE
            = Pattern.compile("[\\x00-\\x20]*"
            + // Optional leading "whitespace"
            "[+-]?("
            + // Optional sign character
            "NaN|"
            + // "NaN" string
            "Infinity|"
            + // "Infinity" string
            // A decimal floating-point string representing a finite positive
            // number without a leading sign has at most five basic pieces:
            // Digits . Digits ExponentPart FloatTypeSuffix
            //
            // Since this method allows integer-only strings as input
            // in addition to strings of floating-point literals, the
            // two sub-patterns below are simplifications of the grammar
            // productions from section 3.10.2 of
            // The Java Language Specification.
            // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
            "(((" + DIGITS + "(\\.)?(" + DIGITS + "?)(" + EXP + ")?)|"
            + // . Digits ExponentPart_opt FloatTypeSuffix_opt
            "(\\.(" + DIGITS + ")(" + EXP + ")?)|"
            + // Hexadecimal strings
            "(("
            + // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
            "(0[xX]" + HEX + "(\\.)?)|"
            + // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
            "(0[xX]" + HEX + "?(\\.)" + HEX + ")"
            + ")[pP][+-]?" + DIGITS + "))"
            + "[fFdD]?))"
            + "[\\x00-\\x20]*");// Optional trailing "whitespace"
    
    
    protected ValueNode unstringify(JsonNodeFactory factory, String value) {
        if (value.equals("true") || value.equals("false")) {
            return factory.booleanNode(Boolean.parseBoolean(value));

        } else if (LONG.matcher(value).matches()) {
            return factory.numberNode(Long.parseLong(value));
            
        } else if (DOUBLE.matcher(value).matches()) {
            return factory.numberNode(Double.parseDouble(value));

        } else {
            return factory.textNode(value);
        }
    }
    
    
    public static class StrictStringifiedPrimitive extends StringifiedPrimitive<String, ValueNode> {

        @Override
        public ValueNode encode(JsonNodeFactory factory, String value) {
            return unstringify(factory, value);
        }
        
    }
    
    public static class LenientStringifiedPrimitive extends StringifiedPrimitive<Object, JsonNode> {

        @Override
        public JsonNode encode(JsonNodeFactory factory, Object value) {
            if (value instanceof String[]) {
                var array = new SparseArrayNode(factory);
                for (var string : (String[]) value) {
                    array.add(unstringify(factory, string));
                }
                
                return array;
                
            } else {
                return unstringify(factory, (String) value);
            }
        }
        
    }
    
}
