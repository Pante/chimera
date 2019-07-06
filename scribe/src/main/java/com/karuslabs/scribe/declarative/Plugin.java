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
package com.karuslabs.scribe.declarative;

import com.karuslabs.scribe.Version;

import org.checkerframework.checker.nullness.qual.Nullable;


@Configuration
public abstract class Plugin {
    
    private @Nullable String name;
    private @Nullable String version;
    private @Nullable String main;
    private @Nullable Version api;
    private Information information;
    private Load load;
    private Command[] commands;
    private Permission[] permissions;
    
    
    public Plugin() {
        name = null;
        version = null;
        main = null;
        api = null;
        information = new Information();
        load = new Load();
        commands = new Command[] {};
        permissions = new Permission[] {};
    }
    
    
    public abstract void build();
    
    
    public Plugin name(String name) {
        this.name = name;
        return this;
    }
    
    public Plugin version(String version) {
        this.version = version;
        return this;
    }
    
    public Plugin main(Class<?> main) {
        return main(main.getCanonicalName());
    }
    
    public Plugin main(String main) {
        this.main = main;
        return this;
    }
    
    public Plugin api(Version version) {
        api = version;
        return this;
    }
    
    public Plugin information(Information information) {
        this.information = information;
        return this;
    }
    
    public Plugin load(Load load) {
        this.load = load;
        return this;
    }
    
    public Plugin commands(Command... commands) {
        this.commands = commands;
        return this;
    }
    
    public Plugin permissions(Permission... permissions) {
        this.permissions = permissions;
        return this;
    }


    public @Nullable String name() {
        return name;
    }

    public @Nullable String version() {
        return version;
    }
    
    public @Nullable String main() {
        return main;
    }

    public @Nullable Version api() {
        return api;
    }
    
    public Information information() {
        return information;
    }
    
    public Load load() {
        return load;
    }

    public Command[] commands() {
        return commands;
    }

    public Permission[] permissions() {
        return permissions;
    }
    
}
