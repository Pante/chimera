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
package com.karuslabs.commons.command.aot.old;

import com.karuslabs.smoke.Typing;
import com.karuslabs.commons.command.aot.old.Mirrors.Pointer;

import java.util.Map;

public class PointerBlock implements Block<LambdaContext, Map.Entry<Integer, Pointer>> {

    Typing typing;
    
    public PointerBlock(Typing typing) {
        this.typing = typing;
    }
    
    @Override
    public void emit(LambdaContext lambda, Map.Entry<Integer, Pointer> entry) {
        var pointer = entry.getValue();
        var variable = "pointer" + lambda.count++;
        var name = pointer.value.identifier.name;
        var type = typing.element(typing.box(pointer.site.asType())).getQualifiedName();
        
        lambda.line("var ", variable, " =  context.getArgument(", name, ", ", type, ".class);");
        lambda.arguments.put(entry.getKey(), variable);
    }

}
