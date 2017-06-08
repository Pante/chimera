/*
 * Copyright (C) 2017 Karus Labs
 * All rights reserved.
 */
package com.karuslabs.commons.display;

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
