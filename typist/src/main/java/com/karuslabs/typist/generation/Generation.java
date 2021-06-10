/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
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
package com.karuslabs.typist.generation;

import com.karuslabs.typist.Environment;
import com.karuslabs.typist.generation.chunks.*;
import com.karuslabs.utilitary.*;

import java.io.IOException;
import javax.annotation.processing.*;
import javax.lang.model.element.Element;

public record Generation(Logger logger, Filer filer, Header header, Type type, int[] counter) {
    
    public void generate(Environment environment) {
        var out = environment.out;
        var pack = out.folder().isEmpty() ? "" : out.folder();
        var file = pack.isEmpty() ? out.file() : pack + "." + out.file();
        var elements = environment.namespaces.keySet().toArray(new Element[0]);
        
        try (var writer = filer.createSourceFile(file, elements).openWriter()) {
            var source = new Source();
            
            header.emit(source, pack);
            type.emit(source, environment);
            
            counter[0] = 0;
            writer.write(source.toString());
            
        } catch (FilerException ignored) {
            logger.error(out.element(), "\"" + file + "\" already exists");
            
        } catch (IOException ignored) {
            logger.error(out.element(), "Failed to create file: \"" + file + "\"");
        }
    }
    
}
