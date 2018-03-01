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
 * A concrete subclass of {@code EffectExecutor} which contains a {@code Effect} to execute.
 */
public class CachedExecutor extends EffectExecutor {
    
    private Effect effect;
    
    
    /**
     * Constructs a {@code CachedExecutor} with the specified plugin, {@code Effect}, orientation, number of iterations, delay, period
     * and whether the effect is to be executed asynchronously.
     * 
     * @param plugin the plugin
     * @param effect the effect
     * @param orientate true if the direction of the origin and target will be updated; else false
     * @param iterations the number of iterations
     * @param delay the number of ticks to wait before executing the effect
     * @param period the number of ticks to wait between runs
     * @param async true if the effect is to be executed asynchronously; else false
     */
    public CachedExecutor(Plugin plugin, Effect effect, boolean orientate, long iterations, long delay, long period, boolean async) {
        super(plugin, orientate, iterations, delay, period, async);
        this.effect = effect;
    }
    
    
    /**
     * Renders a copy of the {@code Effect} with the specified origin and target globally.
     * 
     * @param origin the origin
     * @param target the target
     * @return the Result for the execution of the effect
     */
    public Result<Void> render(BoundLocation origin, BoundLocation target) {
        return schedule(Tasks.global(effect.get(), origin, target, orientate, iterations));
    }
    
    /**
     * Renders a copy of the {@code Effect} with the specified origin and target to the {@code Player}s.
     * 
     * @param players the players
     * @param origin the origin
     * @param target the target
     * @return the Result for the execution of the effect
     */
    public Result<Void> render(Collection<Player> players, BoundLocation origin, BoundLocation target) {
        return schedule(Tasks.players(effect.get(), players, origin, target, orientate, iterations));
    }
    
    /**
     * Renders a copy of the {@code Effect} with the specified origin and target to the {@code Player}.
     * 
     * @param player the player
     * @param origin the origin
     * @param target the target
     * @return the Result for the execution of the effect
     */
    public Result<Void> render(Player player, BoundLocation origin, BoundLocation target) {
        return schedule(Tasks.player(effect.get(), player, origin, target, orientate, iterations));
    }
    
    
    /**
     * Creates a {@code CachedExecutor} builder with the specified plugin.
     * 
     * @param plugin the plugin
     * @return the builder
     */
    public static CachedBuilder builder(Plugin plugin) {
        return new CachedBuilder(new CachedExecutor(plugin, null, false, 0, 0, 0, true));
    }
    
    
    /**
     * Represents a builder for {@code CachedExecutor}s.
     */
    public static class CachedBuilder extends Builder<CachedExecutor, CachedBuilder> {

        private CachedBuilder(CachedExecutor executor) {
            super(executor);
        }
        
        
        /**
         * Sets the {@code Effect}.
         * 
         * @param effect the effect
         * @return this
         */
        public CachedBuilder effect(Effect effect) {
            executor.effect = effect;
            return this;
        }
        
        /**
         * {@inheritDoc} 
         */
        @Override
        protected CachedBuilder getThis() {
            return this;
        }
        
    }
    
}
