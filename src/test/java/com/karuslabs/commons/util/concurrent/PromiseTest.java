/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.util.concurrent;

import java.util.concurrent.*;

import junitparams.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class PromiseTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    
    private Promise<String> promise;
    
    
    public PromiseTest() {
        promise = mock(Promise.class);
        when(promise.obtain()).thenCallRealMethod();
        when(promise.obtain(anyLong(), any())).thenCallRealMethod();
    }
    
    
    @Test
    @Parameters
    public void obtain(Class<? extends Exception> type, Exception thrown) throws InterruptedException, ExecutionException {
        exception.expect(type);
        exception.expectCause(equalTo(thrown));
        
        when(promise.get()).thenThrow(thrown);
        
        promise.obtain();
    }
    
    protected Object[] parametersForObtain() {
        return new Object[] {
            new Object[] {UncheckedExecutionException.class, new ExecutionException(null)},
            new Object[] {UncheckedInterruptedException.class, new InterruptedException()}
        };
    }
    
    
    @Test
    @Parameters
    public void obtain_Time(Class<? extends Exception> type, Exception thrown) throws InterruptedException, ExecutionException, TimeoutException {
        exception.expect(type);
        exception.expectCause(equalTo(thrown));
        
        when(promise.get(anyLong(), any())).thenThrow(thrown);
        
        promise.obtain(0, TimeUnit.DAYS);
    }
    
    protected Object[] parametersForObtain_Time() {
        return new Object[] {
            new Object[] {UncheckedExecutionException.class, new ExecutionException(null)},
            new Object[] {UncheckedInterruptedException.class, new InterruptedException()},
            new Object[] {UncheckedTimeoutException.class, new TimeoutException()}
        };
    }
    
}
