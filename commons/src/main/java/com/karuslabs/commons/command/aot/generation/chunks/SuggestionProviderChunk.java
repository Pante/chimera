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
package com.karuslabs.commons.command.aot.generation.chunks;

import com.karuslabs.commons.command.aot.Binding.Method;
import com.karuslabs.commons.command.aot.*;

import com.karuslabs.puff.generation.Source;

import java.util.ArrayList;
import java.util.List;

public class SuggestionProviderChunk {
    
    private final Types types;
    
    public SuggestionProviderChunk(Types types) {
        this.types = types;
    }
    
    public void emit(Source source, Command command, Method value) {
        var arguments = desugar(source, value, command);
        
        source.line(Source.arguments("context", "builder"), " -> {")
              .indent()

                  .line("return ", "Type", ".", "method", Source.arguments(arguments), ";")
              .unindent()
              .line("};");
    }
    
    List<String> desugar(Source source, Method method, Command command) {
        var arguments = new ArrayList<String>();
        var refs = method.parameters(command);
        var params = method.site.getParameters();
        
        for (int i = 0; i < params.size(); i++) {
            var ref = refs.get(i);
            if (ref == null) {
                // Need to map param's type mirror to param inside lambda, then put into arguments
            } else {
                // Need counter to bump
                var name = ref.site.getSimpleName().toString() + "counter";
                source.assign(name, "context.getArgument" + Source.arguments(ref.value.identity.name, types.element(ref.site.asType()).getQualifiedName() + ".class"));
            }
        }
        
        return arguments;
    }

}
