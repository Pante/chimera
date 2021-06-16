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
package com.karuslabs.typist.lints;

import com.karuslabs.typist.*;
import com.karuslabs.utilitary.Logger;

/**
 * A {@code Lint} analyzes the abstract syntax tree (AST) of commands in an
 * environment.
 */
public abstract class Lint {
    
    /**
     * Analyzes the given environment using the given lints.
     * 
     * @param environment the environment to be analyzed
     * @param lints the lints used to analyze the given environment
     */
    public static void lint(Environment environment, Lint... lints) {
        for (var lint : lints) {
            for (var namespace : environment.namespaces.values()) {
                for (var command : namespace.values()) {
                    Lint.lint(environment, lint, command);
                }
            }
        }
    }
    
    /**
     * Analyzes the given command using the given lint.
     * 
     * @param environment the environment
     * @param lint the lint used to analyze the given environment
     * @param command the command to be analyzed
     */
    static void lint(Environment environment, Lint lint, Command command) {
        lint.lint(environment, command);
        for (var child : command.children().values()) {
            Lint.lint(environment, lint, child);
        }
    }
    
    /**
     * The logger used to report errors.
     */
    protected final Logger logger;
    
    /**
     * Creates a {@code Lint} with the given logger.
     * 
     * @param logger the logger
     */
    public Lint(Logger logger) {
        this.logger = logger;
    }
    
    /**
     * Lints the given command.
     * 
     * @param environment the environment
     * @param command the command to be linted
     */
    public abstract void lint(Environment environment, Command command);
    
}
