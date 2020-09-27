/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.commons.util;

import java.util.*;

import org.bukkit.*;
import org.bukkit.util.Vector;

import org.checkerframework.checker.nullness.qual.Nullable;
import com.karuslabs.annotations.ValueType;

public final @ValueType class Point extends Location {
    
    private static final double EPSILON = 0.000001;
    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;
    
    public static enum Axis {
        X(0),
        Y(1),
        Z(2);
        
        private final int index;
        
        private Axis(int index) {
            this.index = index;
        }
    } 
    
    private boolean[] relative;
    private boolean rotation;
    
    public Point() {
        this(0, 0, 0);
    }
    
    public Point(double x, double y, double z) {
        this(null, x, y, z);
    }
    
    public Point(@Nullable World world, double x, double y, double z) {
        this(world, x, y, z, 0, 0);
    }

    public Point(@Nullable World world, double x, double y, double z, float yaw, float pitch) {
        super(world, x, y, z, yaw, pitch);
        relative = new boolean[]{ false, false, false };
        rotation = false;
    }
    
    
    public void copy(Location origin, Location result) {
        result.setX(relative[X] ? getX() + origin.getX() : getX());
        result.setY(relative[Y] ? getY() + origin.getY() : getY());
        result.setZ(relative[Z] ? getZ() + origin.getZ() : getZ());
        
        if (rotation) {
            Vectors.rotate(result, origin);
        }
    }
    
    public void copy(Location origin, Vector result) {
        result.setX(relative[X] ? getX() + origin.getX() : getX())
              .setY(relative[Y] ? getY() + origin.getY() : getY())
              .setZ(relative[Z] ? getZ() + origin.getZ() : getZ());
        
        if (rotation) {
            Vectors.rotate(result, origin);
        }
    }
    
    public void align(Location origin) {
        if (relative[X]) setX(getX() + origin.getX());
        if (relative[Y]) setY(getY() + origin.getY());
        if (relative[Z]) setZ(getZ() + origin.getZ());
        if (rotation) Vectors.rotate(this, origin);
    }
    
    
    public Point set(Axis axis, double value) {
        switch (axis) {
            case X:
                setX(value);
                break;
               
            case Y:
                setY(value);
                break;
                
            case Z:
                setZ(value);
                break;
        }
        
        return this;
    }
    
    
    public boolean relative(Axis axis) {
        return relative[axis.index];
    }
    
    public Point relative(Axis axis, boolean relative) {
        this.relative[axis.index] = relative;
        return this;
    }
    
    
    public boolean rotation() {
        return rotation;
    }
    
    public Point rotation(boolean rotation) {
        this.rotation = rotation;
        return this;
    }
    
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        
        if (!(object instanceof Point)) {
            return false;
        }
        
        var other = (Point) object;
        return Objects.equals(getWorld(), other.getWorld())
            && Math.abs(getX() - other.getX()) < EPSILON
            && Math.abs(getY() - other.getY()) < EPSILON
            && Math.abs(getZ() - other.getZ()) < EPSILON
            && Math.abs(getYaw() - other.getYaw()) < EPSILON
            && Math.abs(getPitch() - other.getPitch()) < EPSILON
            && rotation == other.rotation 
            && Arrays.equals(relative, other.relative);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        
        hash = 19 * hash + Arrays.hashCode(this.relative);
        hash = 19 * hash + (this.rotation ? 1 : 0);
        
        return hash;
    }
    
    @Override
    public String toString() {
        return "Point[rotation: " + rotation + ", world: " + getWorld() 
             + ", x: " + point(getX(), X) 
             + ", y: " + point(getY(), Y) 
             + ", z: " + point(getZ(), Z)
             + ", yaw: " + getYaw()
             + ", pitch: " + getPitch()
             + "]";
    }
    
    private String point(double point, int axis) {
        return "[" + point + ", " + (relative[axis] ? "relative" : "absolute") + "]";
    }
    
}
