/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.display;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;


/**
 * Represents a immutable wrapper for <code>Sound</code> which encapsulates additional dependent fields.
 * <p>
 * Generally, using <code>Sound</code> requires other variables such as volume and pitch which makes
 * storing <code>Sound</code> and its related variables either extremely messy or hard-coded. Neither of
 * which are desirable. This solves it by storing the related variables internally.
 */
public class Music {
    
    private Sound sound;
    private SoundCategory category;
    private float volume;
    private float pitch;
    
    
    /**
     * Constructs a <code>Music</code> with the specified <code>ConfigurationSection</code>.
     * <p>
     * Valid keys and their default values are as follows: <pre>sound: BLOCK_GLASS_BREAK, category: PLAYERS, volume: 1, pitch: 1</pre>. 
     * Note that the values for <pre>sound</pre> and <pre>category</pre> must be in all capital letters.
     * 
     * @param config the ConfigurationSection
     */
    public Music(ConfigurationSection config) {
        this(
            Sound.valueOf(config.getString("sound", "BLOCK_GLASS_BREAK")),
            SoundCategory.valueOf(config.getString("category", "PLAYERS")),
            (float) config.getDouble("volume", 1),
            (float) config.getDouble("pitch", 1)
        );
    }
    
    /**
     * Constructs a <code>Music</code> with the specified sound, category, volume and pitch,
     * 
     * @param sound the sound
     * @param category the category
     * @param volume the volume
     * @param pitch  the pitch
     */
    public Music(Sound sound, SoundCategory category, float volume, float pitch) {
        this.sound = sound;
        this.category = category;
        this.volume = volume;
        this.pitch = pitch;
    }
    
    
    
    /**
     * Plays the sound to the specified <code>Player</code> only.
     * 
     * @param player the player
     */
    public void play(Player player) {
        player.playSound(player.getLocation(), sound, category, volume, pitch);
    }
    
    /**
     * Plays the sound at the location to all players within range.
     * 
     * @param location the location
     */
    public void play(Location location) {
        location.getWorld().playSound(location, sound, category, volume, pitch);
    }
    
    
    /**
     * @return the sound
     */
    public Sound getSound() {
        return sound;
    }
    
    /**
     * @return the category
     */
    public SoundCategory getCategory() {
        return category;
    }
    
    /**
     * @return the volume
     */
    public float getVolume() {
        return volume;
    }
    
    /**
     * @return the pitch
     */
    public float getPitch() {
        return pitch;
    }
    
}
