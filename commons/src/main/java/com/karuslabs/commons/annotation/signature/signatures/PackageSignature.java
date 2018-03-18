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
package com.karuslabs.commons.annotation.signature.signatures;

import com.karuslabs.commons.annotation.signature.Signature;

import java.util.regex.Pattern;
import javax.lang.model.element.PackageElement;


public abstract class PackageSignature implements Signature<PackageElement> {
    
    public static PackageSignature exact(String name) {
        return new ExactPackageSignature(name);
    }
    
    public static PackageSignature expression(String expression) {
        return new ExpressionPackageSignature(expression);
    }
    
    
    static class ExactPackageSignature extends PackageSignature {
        
        private String name;
        
        
        public ExactPackageSignature(String name) {
            this.name = name;
        }
        
        
        @Override
        public boolean match(PackageElement element) {
            return name.equals(element.getQualifiedName());
        }
        
    }
    
    static class ExpressionPackageSignature extends PackageSignature {
        
        private Pattern expression;
        
        
        public ExpressionPackageSignature(String expression) {
            this.expression = Pattern.compile(expression);
        }
        
        
        @Override
        public boolean match(PackageElement element) {
            return expression.matcher(element.getQualifiedName()).matches();
        }
        
    }
    
}
