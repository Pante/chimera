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
package com.karuslabs.commons.command.types;

import com.karuslabs.commons.command.Readers;
import com.mojang.brigadier.arguments.*;

/**
 * A word type that is mapped to an unquoted {@code StringArgumentType}.
 * 
 * @param <T> the type of the argument
 */
@FunctionalInterface
public interface WordType<T> extends Type<T> {
    
    /**
     * Returns a alternate implementation of {@link StringArgumentType#word()}
     * that supports non-ASCII characters.
     * <br>
     * <br>
     * <b>This should be preferred over {@link StringArgumentType#word()}.</b>
     * 
     * @return an alternate implementation of {@link StringArgumentType#word()} that
     *         supports non-ASCII characters
     */
    static WordType<String> word() {
        return Readers::unquoted;
    }
    
    /**
     * Returns an unquoted {@code StringArgumentType}.
     * 
     * @return an unquoted {@code StringArgumentType}
     */
    @Override
    default StringArgumentType mapped() {
        class Constants {
            static final StringArgumentType WORD = StringArgumentType.word(); 
        }
        
        return Constants.WORD;
    }
    
}
