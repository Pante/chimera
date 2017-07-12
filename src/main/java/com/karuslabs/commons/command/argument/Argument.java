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
package com.karuslabs.commons.command.argument;

import com.google.common.primitives.*;

import java.util.*;

import org.bukkit.*;
import org.bukkit.entity.Player;


public class Argument {
    
    public static final Argument EMPTY = new Argument("");
    
    
    private String argument;
    
    
    public Argument(String argument) {
        this.argument = argument;
    }
    
    
    public String asString() {
        return argument;
    }
    
    public String asColouredString() {
        return ChatColor.translateAlternateColorCodes('&', argument);
    }
    
    
    public boolean asBoolean() {
        return Boolean.parseBoolean(argument);
    }
    
    public boolean asBooleanOrDefault(boolean value) {
        if (argument.toLowerCase().matches("(true|false)")) {
            return Boolean.parseBoolean(argument);
                    
        } else {
            return value;
        }
    }
    
    
    public int asInteger() {
        return Integer.parseInt(argument);
    }
    
    public int asIntegerOrDefault(int value) {
        Integer number;
        if ((number = Ints.tryParse(argument)) != null) {
            return number;
            
        } else {
            return value;
        }
    }
    
    
    public double asDouble() {
        return Double.parseDouble(argument);
    }
    
    public double asDoubleOrDefault(double value) {
        Double number;
        if ((number = Doubles.tryParse(argument)) != null) {
            return number;
            
        } else {
            return value;
        }
    }
    
    
    public float asFloat() {
        return Float.parseFloat(argument);
    }
    
    public float asFloatOrDefault(float value) {
        Float number;
        if ((number = Floats.tryParse(argument)) != null) {
            return number;
            
        } else {
            return value;
        }
    }
    
    
    public Player asPlayer() {
        return Bukkit.getPlayer(argument);
    }
    
    public Player asPlayerOrDefault(Player value) {
        Player player;
        if ((player = Bukkit.getPlayer(argument)) != null) {
            return player;
            
        } else {
            return value;
        }
    }
    
    
    public World asWorld() {
        return Bukkit.getWorld(argument);
    }
    
    public World asWorldOrDefault(World value) {
        World world;
        if ((world = Bukkit.getWorld(argument)) != null) {
            return world;
            
        } else {
            return value;
        }
    }
    
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Optional)) {
            return false;
        }

        return Objects.equals(argument, ((Argument) object).argument);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(argument);
    }
    
}
