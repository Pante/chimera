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
package com.karuslabs.commons.animation;

import com.karuslabs.commons.annotation.*;

import java.util.*;

import org.bukkit.*;
import org.bukkit.entity.Player;

import static java.lang.Float.floatToIntBits;
import static org.bukkit.SoundCategory.MASTER;

/**
 * Represents a {@code Audio} which consist of a {@code Sound}, {@code SoundCategory}, volume and pitch.
 */
@Immutable
@ValueBased
public class Audio {
    
    private Sound sound;
    private SoundCategory category;
    private float volume;
    private float pitch;
    
    
    /**
     * Constructs an {@code Audio} with the specified {@code Sound}, volume and pitch and {@link SoundCategory#MASTER}.
     * 
     * @param sound the sound
     * @param volume the volume
     * @param pitch the pitch
     */
    public Audio(Sound sound, float volume, float pitch) {
        this(sound, MASTER, volume, pitch);
    }
    
    /**
     * Constructs an {@code Audio} with the specified {@code Sound}, {@code SoundCategory}, volume and pitch.
     * 
     * @param sound the sound
     * @param category the category
     * @param volume the volume
     * @param pitch the pitch
     */
    public Audio(Sound sound, SoundCategory category, float volume, float pitch) {
        this.sound = sound;
        this.category = category;
        this.volume = volume;
        this.pitch = pitch;
    }

    
    /**
     * Plays this {@code Audio} at the specified location.
     * 
     * @param location the location
     */
    public void play(Location location) {
        location.getWorld().playSound(location, sound, category, volume, pitch);
    }
    
    
    /**
     * Plays this {@code Audio} at the location of the specified {@code Player} for the specified {@code Player} only.
     * 
     * @param player the player
     */
    public void play(Player player) {
        play(player, player.getLocation());
    }
    
    /**
     * Plays this {@code Audio} at the specified location for the specified {@code Player}s only.
     * 
     * @param players the players
     * @param location the location
     */
    public void play(Collection<Player> players, Location location) {
        players.forEach(player -> play(player, location));
    }
    
    /**
     * Plays this {@code Audio} at the specified location for the specified {@code Player} only.
     * 
     * @param player the player
     * @param location the location
     */
    public void play(Player player, Location location) {
        player.playSound(location, sound, category, volume, pitch);
    }
    
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
            
        } else if (other instanceof Audio) {
            return equals((Audio) other);
            
        } else {
            return false;
        }
    }
    
    public boolean equals(Audio other) {
        return sound == other.sound && category == other.category && volume == other.volume && pitch == other.pitch;
    } 

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(sound);
        hash = 53 * hash + Objects.hashCode(category);
        hash = 53 * hash + floatToIntBits(volume);
        hash = 53 * hash + floatToIntBits(pitch);
        return hash;
    }
    
    @Override
    public String toString() {
        return "Audio[sound: " + sound + " category: " + category + " volume: " + volume + " pitch: " + pitch + "]";
    }
    
}
