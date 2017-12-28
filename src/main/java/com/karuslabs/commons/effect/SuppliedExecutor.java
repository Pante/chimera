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
package com.karuslabs.commons.effect;

import com.karuslabs.commons.util.concurrent.Result;
import com.karuslabs.commons.world.BoundLocation;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


/**
 * A concrete subclass of {@code EffectExecutor} which executes the supplied {@code Effect}s.
 */
public class SuppliedExecutor extends EffectExecutor {
    
    /**
     * Constructs an {@code SuppliedExecutor} with the specified plugin, orientation, number of iterations, delay, period and whether the effect
     * should be executed asynchronously.
     * 
     * @param plugin the plugin
     * @param orientate true if the direction of the origin and target will be updated; else false
     * @param iterations the number of iterations
     * @param delay the number of ticks to wait before executing the effect
     * @param period the number of ticks to wait between runs
     * @param async true if the effect is to be executed asynchronously; else false
     */
    public SuppliedExecutor(Plugin plugin, boolean orientate, long iterations, long delay, long period, boolean async) {
        super(plugin, orientate, iterations, delay, period, async);
    }
    
    
    /**
     * Renders the {@code Effect} with the specified origin and target globally.
     * 
     * @param effect the effect
     * @param origin the origin
     * @param target the target
     * @return the Result for the execution of the effect
     */
    public Result<Void> render(Effect effect, BoundLocation origin, BoundLocation target) {
        return schedule(Tasks.global(effect, origin, target, orientate, iterations));
    }
    
    /**
     * Renders the {@code Effect} with the specified origin and target to the {@code Player}s.
     * 
     * @param effect the effect
     * @param players the players
     * @param origin the origin
     * @param target the target
     * @return the Result for the execution of the effect
     */
    public Result<Void> render(Effect effect, Collection<Player> players, BoundLocation origin, BoundLocation target) {
        return schedule(Tasks.players(effect, players, origin, target, orientate, iterations));
    }
    
    /**
     * Renders the {@code Effect} with the specified origin and target to the {@code Player}.
     * 
     * @param effect the effect
     * @param player the player
     * @param origin the origin
     * @param target the target
     * @return the Result for the execution of the effect
     */
    public Result<Void> render(Effect effect, Player player, BoundLocation origin, BoundLocation target) {
        return schedule(Tasks.player(effect, player, origin, target, orientate, iterations));
    }
    
    
    /**
     * Creates a {@code SuppliedExecutor} builder with the specified plugin.
     * 
     * @param plugin the plugin
     * @return the builder
     */
    public static SuppliedBuilder builder(Plugin plugin) {
        return new SuppliedBuilder(new SuppliedExecutor(plugin, false, 0, 0, 0, true));
    }
    
    
    /**
     * Represents a builder for {@code SuppliedExecutor}s.
     */
    public static class SuppliedBuilder extends Builder<SuppliedExecutor, SuppliedBuilder> {

        private SuppliedBuilder(SuppliedExecutor executor) {
            super(executor);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected SuppliedBuilder getThis() {
            return this;
        }
        
    }
    
}
