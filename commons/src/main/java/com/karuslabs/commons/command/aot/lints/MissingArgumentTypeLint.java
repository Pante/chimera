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
package com.karuslabs.commons.command.aot.lints;

import com.karuslabs.smoke.Logger;
import com.karuslabs.commons.command.aot.Identifier;
import com.karuslabs.commons.command.aot.Identifier.Type;
import com.karuslabs.commons.command.aot.Mirrors.*;

public class MissingArgumentTypeLint implements Lint {

    @Override
    public void lint(Logger logger, Identifier identifier, Command command) {
        if (identifier.type != Type.ARGUMENT) {
            return;
        }
        
        for (var member : command.members.values()) {
            if (member.type == Member.Type.ARGUMENT_TYPE) {
                return;
            }
        }
        
        logger.zone(command.site).error(identifier.name, "is missing an argument type", "an ArgumentType<?> should be bound to it");
    }

}
