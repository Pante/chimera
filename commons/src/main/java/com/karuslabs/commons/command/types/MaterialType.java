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
package com.karuslabs.commons.command.types;

import com.karuslabs.commons.util.collection.Trie;

import com.mojang.brigadier.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.*;
import com.mojang.brigadier.suggestion.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;

import org.bukkit.Material;

import static com.karuslabs.commons.command.Readers.unquoted;

/**
 * A {@code Material} type. <b>Legacy materials are not supported</b>.
 */
public class MaterialType implements WordType<Material> {
    
    private static final Trie<Material> MATERIALS = new Trie<>();
    private static final DynamicCommandExceptionType EXCEPTION = new DynamicCommandExceptionType(material -> new LiteralMessage("Unknown material: " + material));
    private static final List<String> EXAMPLES = List.of("flint_and_steel", "tnt");
    static {
        var warn = true;
        for (var material : Material.values()) {
            if (!material.isLegacy()) {
                MATERIALS.put(material.getKey().getKey(), material);
                
            } else if (warn) {
                Bukkit.getLogger().warning("Leagcy Material enumerations are not supported. Please add 'api-version: 1.13' to your plugin.yml");
                warn = false;
            }
        }
    }
    
    /**
     * Returns a material which key matches the string returned by the given {@code StringReader}.
     * 
     * @param reader the reader
     * @return a material with the given key
     * @throws CommandSyntaxException if a material with the given key does not exist
     */
    @Override
    public Material parse(StringReader reader) throws CommandSyntaxException {
        var name = unquoted(reader).toLowerCase();
        var material = MATERIALS.get(name);
        
        if (material == null) {
            throw EXCEPTION.createWithContext(reader, name);
        }
        
        return material;
    }
    
    /**
     * Returns the materials that start with the remaining input of the given {@code SuggesitonBuilder}.
     * 
     * @param <S> the type of the source
     * @param context the context
     * @param builder the builder
     * @return the material keys that start with the remaining input
     */
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (var material : MATERIALS.prefixedKeys(builder.getRemaining())) {
            builder.suggest(material);
        }
        
        return builder.buildFuture();
    }

    @Override
    public List<String> getExamples() {
        return EXAMPLES;
    }
    
}
