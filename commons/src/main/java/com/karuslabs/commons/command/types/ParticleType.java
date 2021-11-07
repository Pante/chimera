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

import org.bukkit.Particle;

import static com.karuslabs.commons.command.Readers.unquoted;

/**
 * A {@code Particle} type.
 */
public class ParticleType implements WordType<Particle> {

    private static final Trie<Particle> PARTICLES = new Trie<>();
    private static final DynamicCommandExceptionType EXCEPTION = new DynamicCommandExceptionType(particle -> new LiteralMessage("Unknown particle: " + particle));
    private static final List<String> EXAMPLES = List.of("barrier", "bubble_column_up");
    static {
        for (var particle : Particle.values()) {
            PARTICLES.put(particle.toString().toLowerCase(), particle);
        }
    }
    
    /**
     * Returns a particle which name matches the string returned by the given {@code StringReader}.
     * 
     * @param reader the reader
     * @return a particle with the given name
     * @throws CommandSyntaxException if a particle with the given name does not exist
     */
    @Override
    public Particle parse(StringReader reader) throws CommandSyntaxException {
        var name = unquoted(reader).toLowerCase();
        var particles = PARTICLES.get(name);
        
        if (particles == null) {
            throw EXCEPTION.createWithContext(reader, name);
        }
        
        return particles;
    }

    /**
     * Returns the particles that start with the remaining input of the given {@code SuggesitonBuilder}.
     * 
     * @param <S> the type of the source
     * @param context the context
     * @param builder the builder
     * @return the particle names that start with the remaining input
     */
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (var particles : PARTICLES.prefixedKeys(builder.getRemaining())) {
            builder.suggest(particles);
        }
        
        return builder.buildFuture();
    }

    @Override
    public List<String> getExamples() {
        return EXAMPLES;
    }
    
}
