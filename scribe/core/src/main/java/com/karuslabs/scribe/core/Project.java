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
package com.karuslabs.scribe.core;

import com.karuslabs.annotations.*;

import java.util.*;


/**
 * A {@code Project} contains values inferred from a source.
 */
public @ValueType class Project {
    
    /**
     * An empty project.
     */
    public static Project EMPTY = new Project("", "", "", List.of(), "", "");
    /**
     * A map that associates the {@code artifactId} of a Spigot fork to its {@code groupId}.
     */
    public static Map<String, String> DEPENDENCIES = Map.of(
        "bukkit", "org.bukkit",
        "craftbukkit", "org.bukkit",
        "spigot-api", "org.spigotmc",
        "spigot", "org.spigotmc",
        "paper-api", "com.destroystokyo.paper",
        "paper", "com.destroystokyo.paper"
    );
    
    
    public final String name;
    public final String version;
    public final String api;
    public final @Immutable List<String> authors;
    public final String description;
    public final String url;
    
    
    /**
     * Creates a {@code Project} with the given parameters.
     * 
     * @param name the name
     * @param version the version
     * @param api the API version
     * @param authors the authors
     * @param description the description
     * @param url the URL
     */
    public Project(String name, String version, String api, List<String> authors, String description, String url) {
        this.name = name;
        this.version = version;
        this.api = api;
        this.authors = authors;
        this.description = description;
        this.url = url;
    }
    
}
