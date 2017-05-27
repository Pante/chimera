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
package com.karuslabs.commons.animation;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;


public class Music {
    
    private Sound sound;
    private SoundCategory category;
    private float volume;
    private float pitch;
    
    
    public Music(ConfigurationSection config) {
        this(
            Sound.valueOf(config.getString("sound", "BLOCK_GLASS_BREAK")),
            SoundCategory.valueOf(config.getString("category", "PLAYERS")),
            (float) config.getDouble("volume", 1),
            (float) config.getDouble("pitch", 1)
        );
    }
    
    public Music(Sound sound, SoundCategory category, float volume, float pitch) {
        this.sound = sound;
        this.category = category;
        this.volume = volume;
        this.pitch = pitch;
    }
    
    
    
    public void play(Player player) {
        player.playSound(player.getLocation(), sound, category, volume, pitch);
    }
    
    public void play(Location location) {
        location.getWorld().playSound(location, sound, category, volume, pitch);
    }
    
    
    public Sound getSound() {
        return sound;
    }

    public SoundCategory getCategory() {
        return category;
    }
    
    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
    
}
