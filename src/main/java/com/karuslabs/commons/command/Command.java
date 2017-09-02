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
package com.karuslabs.commons.command;

import com.karuslabs.commons.command.arguments.Arguments;
import com.karuslabs.commons.command.completers.Completer;
import com.karuslabs.commons.locale.Translations;

import java.util.*;
import java.util.regex.Pattern;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;


public class Command extends org.bukkit.command.Command implements PluginIdentifiableCommand {
    
    private static final Pattern PATTERN = Pattern.compile(" (?=(([^'\"]*['\"]){2})*[^'\"]*$)");
    
    
    private Plugin plugin;
    private Map<String, Command> subcommands;
    private Map<Integer, Completer> completers;
    private Translations translations;
    
    
    public Command(Plugin plugin, String name, String description, String message, List<String> aliases, Translations translations) {
        super(name, description, message, aliases);
        this.plugin = plugin;
        this.subcommands = new HashMap<>();
        this.completers = new HashMap<>();
        this.translations = translations;
    }


    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return execute(new Context(new Source(sender), label, null, this), new Arguments(PATTERN.split(String.join(" ", args))));
    }
    
    public boolean execute(Context context, Arguments arguments) {
        return false;
    }
    
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return null;
    }
    
    public List<String> tabComplete(Context context, String args) {
        return null;
    }
    

    @Override
    public Plugin getPlugin() {
        return plugin;
    }
    
    
    public Translations getTranslations() {
        return translations;
    }
    
}
