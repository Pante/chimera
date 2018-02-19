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
package com.karuslabs.commons.locale.builders;

import com.karuslabs.commons.locale.*;
import com.karuslabs.commons.locale.providers.Provider;
import com.karuslabs.commons.locale.resources.*;

import java.io.File;
import java.util.*;


public class ExternalBuilder<GenericTranslation extends Translation> extends Builder<ExternalBuilder, GenericTranslation> {

    private static final Resource[] EMPTY = new Resource[] {};
    
    
    private List<Resource> resources;

    
    public ExternalBuilder(TranslationSupplier supplier, String bundle, Provider provider) {
        super(supplier, bundle, provider);
        resources = new ArrayList<>();
    }

    
    public ExternalBuilder embedded(String folder) {
        resources.add(new EmbeddedResource(folder));
        return this;
    }

    public ExternalBuilder external(String folder) {
        resources.add(new ExternalResource(new File(folder)));
        return this;
    }

    
    @Override
    public GenericTranslation build() {
        return supplier.get(bundle, new ExternalControl(resources.toArray(EMPTY)), provider);
    }

    @Override
    protected ExternalBuilder getThis() {
        return this;
    }

}
