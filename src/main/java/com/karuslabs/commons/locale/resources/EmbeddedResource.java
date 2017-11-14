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
package com.karuslabs.commons.locale.resources;

import java.io.InputStream;
import javax.annotation.*;


/**
 * A {@code Resource} implementation which references embedded JAR files, from which {@code ResourceBundle}s may be loaded. 
 * The files are relative to the {@code ClassLoader} of this class.
 */
public class EmbeddedResource implements Resource {

    public String path;
    
    
    /**
     * Constructs an {@code EmbeddedResource} with the specified path to the folder which contains the embedded files
     * from which {@code ResourceBundle}s may be loaded, relative to the {@code ClassLoader} of this class.
     * 
     * @param path the path to this EmbeddedResource, relative to the ClassLoader of this class
     */
    public EmbeddedResource(String path) {
        if (!path.isEmpty() && !path.endsWith("/")) {
            path += "/";
        }
        this.path = path;
    }
    
    
    /**
     * Returns an {@code InputStream} for the embedded file with the specified name, or {@code null} if this {@code EmbeddedResource}
     * contains no such file with the specified name.
     * 
     * @param name the file name
     * @return an InputStream if the file exists; else null
     */
    @Override
    public @Nullable InputStream load(@Nonnull String name) {
        return getClass().getClassLoader().getResourceAsStream(path + name);
    }
    
    
    /**
     * Checks if this {@code EmbeddedResource} holds a reference to an embedded file with the specified name.
     * 
     * @param name the file name
     * @return true if the file exists; else false
     */
    @Override
    public boolean exists(@Nonnull String name) {
        return getClass().getClassLoader().getResource(path + name) != null;
    }
    
    
    /**
     * Returns the path to this {@code EmbeddedResource}, relative to the {@code ClassLoader} of this class.
     * 
     * @return the path
     */
    public String getPath() {
        return path;
    }
    
}
