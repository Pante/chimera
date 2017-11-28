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

import com.karuslabs.commons.locale.resources.Resource;
import java.io.*;
import javax.annotation.*;


/**
 * A {@code Resource} implementation which references files external of this JAR and are
 * on the same machine, from which {@code ResourceBundle}s may be loaded.
 */
public class FileResource implements Resource {
    
    private File folder;
    
    
    /**
     * Constructs a {@code FileResource} with the specified folder which contains the files from which
     * {@code ResourceBundle}s may be loaded.
     *  
     * @param folder the folder
     */
    public FileResource(File folder) {
        this.folder = folder;
    }
    
    /**
     * Returns an {@code InputStream} for the file with the specified name, or {@code null} if no
     * such file exists.
     * 
     * @param name the file name
     * @return an InputStream if the file exists; else null 
     */
    @Override
    public @Nullable InputStream load(@Nonnull String name) {
        File file = new File(folder, name);
        if (!exists(file)) {
            return null;
        }
            
        try {
            return new BufferedInputStream(new FileInputStream(file));

        } catch (FileNotFoundException e) {
            return null;
        }
    }
    
    /**
     * Checks if this {@code FiledResource} holds a reference to a file with the specified name.
     * 
     * @param name the file name
     * @return true if the file exists; else false
     */
    @Override
    public boolean exists(@Nonnull String name) {
        return exists(new File(folder, name));
    }
    
    private boolean exists(File file) {
        return file.isFile() && file.canRead();
    }
    
    
    /**
     * Returns the path to the folder which contains the files which {@code ResourceBundle}s may be
     * loaded from, specified by {@link File#getPath()}.
     * 
     * @return the path to the folder
     */
    public String getPath() {
        return folder.getPath();
    }
    
}
