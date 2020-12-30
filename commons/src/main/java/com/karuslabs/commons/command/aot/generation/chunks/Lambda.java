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

import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.Binding.*;
import com.karuslabs.puff.generation.Source;

import java.util.*;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static javax.lang.model.element.Modifier.STATIC;

public class Lambda {
    
    public static Lambda command(Types types, int[] counter) {
        return new Lambda(types, counter, Map.of("context", types.context), true);
    }
    
    public static Lambda execution(Types types, int[] counter) {
        var parameters = new LinkedHashMap<String, TypeMirror>();
        parameters.put("sender", types.sender);
        parameters.put("context", types.optionalContext);
        
        return new Lambda(types, counter, parameters, false);
    }
    
    public static Lambda requirement(Types types, int[] counter) {
        return new Lambda(types, counter, Map.of("sender", types.sender), true);
    }
    
    public static Lambda suggestionProvider(Types types, int[] counter) {
        var parameters = new LinkedHashMap<String, TypeMirror>();
        parameters.put("context", types.context);
        parameters.put("builder", types.suggestionsBuilder);
        
        return new Lambda(types, counter, parameters, true);
    }
    
    
    private final Types types;
    private final int[] counter;
    private final Map<String, TypeMirror> parameters;
    private final boolean returns;
    
    Lambda(Types types, int[] counter, Map<String, TypeMirror> parameters, boolean returns) {
        this.types = types;
        this.counter = counter;
        this.parameters = parameters;
        this.returns = returns;
    }
    
    public void emit(Source source, Command command, Method method) {
        var reciever = method.site.getModifiers().contains(STATIC) ? command.site.getQualifiedName() : Constants.SOURCE;
        var references = method.parameters(command);
        
        if (references.isEmpty()) {
            source.line(reciever, "::", method.site.getSimpleName());
            
        } else {
            source.line(Source.arguments(parameters.keySet()), " -> {")
              .indent()
                .line(returns ? "return " : "", reciever, ".", method.site.getSimpleName(), Source.arguments(desugar(source, command, references, method.site.getParameters())), ";")
              .unindent()
              .line("};");
        }
    }
    
    List<String> desugar(Source source, Command command, Map<Integer, Reference> references, List<? extends VariableElement> siteParameters) {
        var variables = new ArrayList<String>();
        
        for (int i = 0; i < siteParameters.size(); i++) {
            var reference = references.get(i);
            if (reference == null) {
                variables.add(match(siteParameters.get(i)));
                        
            } else {
                var name = reference.value.identity.name + counter[0]++;
                
                variables.add(name);
                source.assign(name, "context.getArgument" + Source.arguments(reference.value.identity.name, reference.site.asType() + ".class"));
            }
        }
        
        return variables;
    }
    
    String match(VariableElement parameter) {
        for (var entry : parameters.entrySet()) {
            if (types.isSubtype(entry.getValue(), parameter.asType())) {
                return entry.getKey();
            }
        }
        
        throw new IllegalStateException("Unable to resolve parameter: " + parameter.asType() + " " + parameter.getSimpleName());
    }
}
