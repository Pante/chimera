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

import com.karuslabs.commons.annotation.Static;
import com.karuslabs.commons.effect.Effect;
import com.karuslabs.commons.effect.particles.Particles;
import com.karuslabs.commons.world.BoundLocation;

import java.util.*;
import java.lang.ref.WeakReference;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.collection.Sets.weakSet;


@Static
public class Tasks {
    
    public static Task global(Effect effect, BoundLocation origin, BoundLocation target, boolean orientate, long iterations) {
        return new GlobalTask(effect, origin, target, orientate, iterations);
    }
    
    public static Task players(Effect effect, Collection<Player> players, BoundLocation origin, BoundLocation target, boolean orientate, long iterations) {
        return new PlayersTask(effect, players, origin, target, orientate, iterations);
    }
    
    public static Task player(Effect effect, Player player, BoundLocation origin, BoundLocation target, boolean orientate, long iterations) {
        return new PlayerTask(effect, player, origin, target, orientate, iterations);
    }
    
    
    static class GlobalTask extends Task {

        GlobalTask(Effect effect, BoundLocation origin, BoundLocation target, boolean orientate, long iterations) {
            super(effect, origin, target, orientate, iterations);
        }

        @Override
        public void render(Particles particles, Location location) {
            particles.render(location);
        }

        @Override
        public void render(Particles particles, Location location, Vector offset) {
            particles.render(location, offset);
        }
        
    }

    
    static class PlayersTask extends Task {
        
        Set<Player> players;
        
        
        PlayersTask(Effect effect, Collection<Player> players, BoundLocation origin, BoundLocation target, boolean orientate, long iterations) {
            super(effect, origin, target, orientate, iterations);
            this.players = weakSet(players);
        }

        @Override
        public void render(Particles particles, Location location) {
            particles.render(players, location);
        }

        @Override
        public void render(Particles particles, Location location, Vector offset) {
            particles.render(players, location, offset);
        }
        
    }
    
    
    static class PlayerTask extends Task {
        
        private WeakReference<Player> reference;
        
        
        public PlayerTask(Effect effect, Player player, BoundLocation origin, BoundLocation target, boolean orientate, long iterations) {
            super(effect, origin, target, orientate, iterations);
            this.reference = new WeakReference<>(player);
        }

        @Override
        public void render(Particles particles, Location location) {
            Player player = reference.get();
            if (player != null) {
                particles.render(player, location);
            }
        }

        @Override
        public void render(Particles particles, Location location, Vector offset) {
            Player player = reference.get();
            if (player != null) {
                particles.render(player, location, offset);
            }
        }
        
    }
    
    
}
