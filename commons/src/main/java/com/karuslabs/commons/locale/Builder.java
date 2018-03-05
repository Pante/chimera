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
package com.karuslabs.commons.locale;

import com.karuslabs.commons.locale.providers.Provider;
import com.karuslabs.commons.locale.resources.*;

import java.io.File;
import java.util.*;


/**
 * Represents a builder for {@code Translation}s.
 * 
 * @param <GenericTranslation> the type of Translations to build
 */
public class Builder<GenericTranslation extends Translation> {
    
    private static final Resource[] EMPTY = new Resource[] {};
    
    private TranslationSupplier<GenericTranslation> supplier;
    private String bundle;
    private Provider provider;
    private List<Resource> resources;
    
    
    /**
     * Constructs a {@code Builder} with the specified supplier, bundle name and provider.
     * 
     * @param supplier the supplier which supplies the Translations
     * @param bundle the base bundle name
     * @param provider the provider which provides the locales of the players
     */
    public Builder(TranslationSupplier<GenericTranslation> supplier, String bundle, Provider provider) {
        this.supplier = supplier;
        this.bundle = bundle;
        this.provider = provider;
        this.resources = new ArrayList<>();
    }
    
    
    /**
     * Adds the specified embedded folder.
     * 
     * @param folder the folder which contains the additional Resources.
     * @return this
     */
    public Builder embedded(String folder) {
        resources.add(new EmbeddedResource(folder));
        return this;
    }
    
    /**
     * Adds the specified external folder.
     * 
     * @param folder the folder which contains the additional Resources.
     * @return this
     */
    public Builder external(String folder) {
        resources.add(new ExternalResource(new File(folder)));
        return this;
    }
    
    /**
     * Sets the base bundle name.
     * 
     * @param bundle the base bundle name
     * @return this
     */
    public Builder bundle(String bundle) {
        this.bundle = bundle;
        return this;
    }
    
    /**
     * Sets the {@code Provider}.
     * 
     * @param provider the provider which provides the locales of the players
     * @return this
     */
    public Builder provider(Provider provider) {
        this.provider = provider;
        return this;
    }

    
    /**
     * Builds the {@code Translation}.
     * 
     * @return the Translation
     */
    public GenericTranslation build() {
        return supplier.get(bundle, new ExternalControl(resources.toArray(EMPTY)), provider);
    }
    
}
