/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.scribe.annotations.processors;

import com.google.auto.service.AutoService;

import com.karuslabs.annotations.processors.AnnotationProcessor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;


@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes({
    "com.karuslabs.scribe.annotations.API", "com.karuslabs.scribe.annotations.Command",
    "com.karuslabs.scribe.annotations.Commands", "com.karuslabs.scribe.annotations.Information",
    "com.karuslabs.scribe.annotations.Load", "com.karuslabs.scribe.annotations.Permission",
    "com.karuslabs.scribe.annotations.Permissions", "com.karuslabs.scribe.annotations.Plugin"
})
public class PluginProcessor extends AnnotationProcessor {
    
}
