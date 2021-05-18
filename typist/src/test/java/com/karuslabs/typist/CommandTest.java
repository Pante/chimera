/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
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
package com.karuslabs.typist;

import com.karuslabs.typist.Binding.Field;

import org.junit.jupiter.api.*;

import static com.karuslabs.typist.Binding.Pattern.EXECUTION;
import static com.karuslabs.typist.Identity.Type.*;
import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    Command tell = new Command(null, new Identity(LITERAL, "tell"), null);
    Command players = new Command(tell, new Identity(ARGUMENT, "players"), null);
    Command message = new Command(players, new Identity(ARGUMENT, "message"), null);
    Field field = new Field(null, EXECUTION);
    
    @Test
    void binding() {
        players.bind(field);
        
        assertEquals(field, players.binding(EXECUTION));
        assertEquals(field, players.groups.get(EXECUTION.group));
        
        assertNull(message.binding(EXECUTION));
    }
    
    @Test
    void path() {
        assertEquals("tell <players>", players.path());
        assertEquals("tell <players> <message>", message.path());
    }
    
}
