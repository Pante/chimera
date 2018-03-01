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
package com.karuslabs.commons.command.parser;

import com.karuslabs.commons.annotation.Static;
import com.karuslabs.commons.command.References;
import com.karuslabs.commons.locale.providers.Provider;

import java.io.File;

import org.bukkit.plugin.Plugin;


/**
 * This class consists exclusively of methods which creates {@code Parser}s.
 */
@Static
public class Parsers {
    
    /**
     * Creates a {@code Parser} with the specified plugin, folder, references, {@code NullHandle} and {@code Provider}.
     * 
     * @param plugin the plugin
     * @param folder the folder
     * @param references the references
     * @param handle the NullHandle
     * @param provider the Provider
     * @return a Parser
     */
    public static Parser newParser(Plugin plugin, File folder, References references, NullHandle handle, Provider provider) {
        TranslationToken translation = new TranslationToken(references, handle, folder, provider);
        CompletionsToken completions = new CompletionsToken(new CompletionToken(references, handle));
        CommandToken command = new CommandToken(references, handle, plugin, null, translation, completions);
        CommandsToken commands = new CommandsToken(command);
        command.setCommandsToken(commands);
        
        return new Parser(command);
    }
    
}
