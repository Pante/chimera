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
open module com.karuslabs.commons {
    exports com.karuslabs.commons.codec.decoder;
    exports com.karuslabs.commons.codec.decoders;
    exports com.karuslabs.commons.codec.encoder;
    exports com.karuslabs.commons.codec.encoder.encoded;
    exports com.karuslabs.commons.codec.encoders;
    exports com.karuslabs.commons.codec.nodes;
    exports com.karuslabs.commons.item;
    exports com.karuslabs.commons.item.builders;
    exports com.karuslabs.commons.util;
    exports com.karuslabs.commons.util.collections;
    exports com.karuslabs.commons.util.concurrent;
    exports com.karuslabs.commons.util.concurrent.bukkit;
    exports com.karuslabs.commons.util.concurrent.locks;
    
    requires com.karuslabs.annotations;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.javaprop;
    requires com.fasterxml.jackson.dataformat.yaml;
 
    requires brigadier;
    requires org.bukkit;
    requires guava;
    requires bungeecord.chat;
    requires org.checkerframework.checker.qual;
}
