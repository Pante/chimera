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
package com.karuslabs.spigot.plugin.processor.processors;

import org.apache.maven.plugin.logging.Log;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.karuslabs.spigot.plugin.processor.Yaml.TEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class VersionProcessorTest {
    
    Log log = mock(Log.class);
    
    
    @ParameterizedTest
    @CsvSource({" , false, expected, 1, 0", "version, true, expected, 1, 0", "version, false, version, 0, 1"})
    void execute(String specified, boolean override, String expected, int setTimes, int skipTimes) {
        VersionProcessor processor = new VersionProcessor(log, "expected", override);
        TEST.set("version", specified);
        
        processor.execute(TEST, "version");
        
        verify(log, times(setTimes)).info("Detected and setting version to: expected");
        verify(log, times(skipTimes)).info("Skipping as version already exists and overriding is disabled");
        assertEquals(expected, TEST.getString("version"));
    }
    
}
