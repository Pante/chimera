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
package com.karuslabs.commons.command;

import com.karuslabs.commons.command.completion.Completion;
import com.karuslabs.commons.locale.MessageTranslation;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


@TestInstance(PER_CLASS)
class ReferencesTest {
    
    References references = new References();

    
    @Test
    void command() {
        assertSame(Command.NONE, references.command("a", Command.NONE).getCommand("a"));
    }
    
    
    @Test
    void completion() {
        assertSame(Completion.NONE, references.completion("b", Completion.NONE).getCompletion("b"));
    }
    
    
    @Test
    void executor() {
        assertSame(CommandExecutor.NONE, references.executor("b", CommandExecutor.NONE).getExecutor("b"));
    }
    
    
    @Test
    void translation() {
        assertSame(MessageTranslation.NONE, references.translation("c", MessageTranslation.NONE).getTranslation("c"));
    }
    
}
