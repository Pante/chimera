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
package com.karuslabs.commons.command;

import com.google.common.primitives.*;

import java.util.Arrays;

import org.bukkit.*;
import org.bukkit.entity.Player;


public class Arguments {
    
    public static final String[] EMPTY = new String[] {};
    
    
    private String[] args;
    
    
    public Arguments() {
        this(EMPTY);
    }
    
    public Arguments(String[] args) {
        this.args = args;
    }
    
    
    public boolean exists(int index) {
        return  0 <= index && index < args.length;
    }
    
    
    public String get(int index) {
        return args[index];
    }
    
    public String getOrDefault(int index, String value) {
        if (index < args.length) {
            return args[index];
            
        } else {
            return value;
        }
    }
    
    
    public boolean asBoolean(int index) {
        return Boolean.parseBoolean(args[index]);
    }
    
    public boolean asBooleanOrDefault(int index, boolean value) {
        String argument;
        if (index < args.length && (argument = args[index]).toLowerCase().matches("(true|false)")) {
            return Boolean.parseBoolean(argument);
                    
        } else {
            return value;
        }
    }
    
    
    public int asInteger(int index) {
        return Integer.parseInt(args[index]);
    }
    
    public int asIntegerOrDefault(int index, int value) {
        Integer number;
        if (index < args.length && (number = Ints.tryParse(args[index])) != null) {
            return number;
            
        } else {
            return value;
        }
    }
    
    
    public double asDouble(int index) {
        return Double.parseDouble(args[index]);
    }
    
    public double asDoubleOrDefault(int index, double value) {
        Double number;
        if (index < args.length && (number = Doubles.tryParse(args[index])) != null) {
            return number;
            
        } else {
            return value;
        }
    }
    
    
    public float asFloat(int index) {
        return Float.parseFloat(args[index]);
    }
    
    public float asFloatOrDefault(int index, float value) {
        Float number;
        if (index < args.length && (number = Floats.tryParse(args[index])) != null) {
            return number;
            
        } else {
            return value;
        }
    }
    
    
    public Player asPlayer(int index) {
        return Bukkit.getPlayer(args[index]);
    }
    
    public Player asPlayerOrDefault(int index, Player value) {
        Player player;
        if (index < args.length && (player = Bukkit.getPlayer(args[index])) != null) {
            return player;
            
        } else {
            return value;
        }
    }
    
    
    public World asWorld(int index) {
        return Bukkit.getWorld(args[index]);
    }
    
    public World asWorldOrDefault(int index, World value) {
        World world;
        if (index < args.length && (world = Bukkit.getWorld(args[index])) != null) {
            return world;
            
        } else {
            return value;
        }
    }
    
    
    public void trim() {
        if (args.length <= 1) {
            args = EMPTY;
            
        } else {
            args = Arrays.copyOfRange(args, 1, args.length);
        }
    }
    
    
    public String[] getRawArguments() {
        return args;
    }
    
    public void setRawArguments(String[] args) {
        this.args = args;
    }
    
}
