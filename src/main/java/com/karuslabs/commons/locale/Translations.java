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

import com.karuslabs.commons.annotation.Static;
import com.karuslabs.commons.locale.providers.Provider;
import com.karuslabs.commons.locale.resources.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

import static com.karuslabs.commons.locale.providers.Provider.DETECTED;


@Static
public class Translations {
    
    private static final Resource[] EMPTY = new Resource[] {};
    
    
    public static CachedBuilder<Translation> cachedTranslationBuilder() {
        return new CachedBuilder<>(Translation::new, "", DETECTED);
    }
    
    public static CachedBuilder<MessageTranslation> cachedMessageTranslationBuilder() {
        return new CachedBuilder<>(MessageTranslation::new, "", DETECTED);
    }
    
    public static ExternalBuilder<Translation> externalTranslationBuilder() {
        return new ExternalBuilder<>(Translation::new, "", DETECTED);
    }
    
    public static ExternalBuilder<MessageTranslation> externalMessageTranslationBuilder() {
        return new ExternalBuilder<>(MessageTranslation::new, "", DETECTED);
    }
    
    
    public static abstract class Builder<GenericBuilder extends Builder, GenericTranslation extends Translation> {
        
        protected TranslationSupplier<GenericTranslation> supplier;
        protected String bundle;
        protected Provider provider;
        
        
        public Builder(TranslationSupplier supplier, String bundle, Provider provider) {
            this.supplier = supplier;
            this.bundle = bundle;
            this.provider = provider;
        }
        
        
        public GenericBuilder bundle(String bundle) {
            this.bundle = bundle;
            return getThis();
        }
        
        public GenericBuilder provider(Provider provider) {
            this.provider = provider;
            return getThis();
        }
        
        
        public abstract GenericTranslation build();
        
        protected abstract GenericBuilder getThis();
        
    }
    
    public static class CachedBuilder<GenericTranslation extends Translation> extends Builder<CachedBuilder, GenericTranslation> {
        
        private ConcurrentMap<Locale, ResourceBundle> messages;
        
        
        public CachedBuilder(TranslationSupplier supplier, String bundle, Provider provider) {
            super(supplier, bundle, provider);
            messages = new ConcurrentHashMap<>();
        }
        
        
        public CachedBuilder locale(Locale locale, ResourceBundle bundle) {
            messages.put(locale, bundle);
            return this;
        }

        
        @Override
        public GenericTranslation build() {
            return supplier.get(bundle, new CachedControl(messages), provider);
        }

        @Override
        protected CachedBuilder getThis() {
            return this;
        }
        
    }
    
    public static class ExternalBuilder<GenericTranslation extends Translation> extends Builder<ExternalBuilder, GenericTranslation> {
        
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
    
}
