/*
 * Copyright (C) 2017 Karus Labs
 * All rights reserved.
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
        sound = Sound.valueOf(config.getString("sound", "BLOCK_GLASS_BREAK"));
        category = SoundCategory.valueOf(config.getString("category", "PLAYERS"));
        volume = (float) config.getDouble("volume", 1);
        pitch = (float) config.getDouble("pitch", 1);
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
