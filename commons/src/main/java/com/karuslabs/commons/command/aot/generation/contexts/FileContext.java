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
package com.karuslabs.commons.command.aot.generation.contexts;

import com.karuslabs.annotations.Lazy;

import javax.lang.model.element.Element;

import org.checkerframework.checker.nullness.qual.Nullable;

public class FileContext extends Context<FileContext> {

    private @Lazy Element element;
    private @Lazy String folder;
    private @Lazy String file;
    
    public void location(Element element, String folder, String file) {
        this.element = element;
        this.folder = folder;
        this.file = file;
    }
    
    public @Nullable Element element() {
        return element;
    }
    
    public @Nullable String folder() {
        return folder;
    }
    
    public @Nullable String file() {
        return file;
    }
    
    @Override
    protected FileContext self() {
        return this;
    }

}
