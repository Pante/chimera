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

import com.karuslabs.annotations.ValueType;

import java.util.*;

import org.bukkit.*;
import org.bukkit.util.Vector;

import org.checkerframework.checker.nullness.qual.Nullable;


public final @ValueType class Position extends Location {
    
    private static final double EPSILON = 0.000001;
    
    
    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;
    
    
    private boolean[] relative;
    private boolean rotate;
    
    
    public Position() {
        this(0, 0, 0);
    }
    
    public Position(double x, double y, double z) {
        this(null, x, y, z);
    }
    
    public Position(@Nullable World world, double x, double y, double z) {
        this(world, x, y, z, 0, 0);
    }

    public Position(@Nullable World world, double x, double y, double z, float yaw, float pitch) {
        super(world, x, y, z, yaw, pitch);
        relative = new boolean[]{ false, false, false };
        rotate = false;
    }
    
    
    public void apply(Location to, Location origin) {
        to.setX(relative[X] ? getX() + origin.getX() : getX());
        to.setY(relative[Y] ? getY() + origin.getY() : getY());
        to.setZ(relative[Z] ? getZ() + origin.getZ() : getZ());
        
        if (rotate) Vectors.rotate(to, origin);
    }
    
    public void apply(Vector to, Location origin) {
        to.setX(relative[X] ? getX() + origin.getX() : getX())
          .setY(relative[Y] ? getY() + origin.getY() : getY())
          .setZ(relative[Z] ? getZ() + origin.getZ() : getZ());
        
        if (rotate) Vectors.rotate(to, origin);
    }
    
    public void relativize(Location origin) {
        if (relative[X]) setX(getX() + origin.getX());
        if (relative[Y]) setY(getY() + origin.getY());
        if (relative[Z]) setZ(getZ() + origin.getZ());
        if (rotate) Vectors.rotate(this, origin);
    }
    
    
    public Position set(int axis, double value) {
        switch (axis) {
            case X:
               setX(value);
               return this;
               
            case Y:
                setY(value);
                return this;
                
            case Z:
                setZ(value);
                return this;
                
            default:
                throw new IllegalArgumentException("Invalid axis: " + axis);
        }
    }
    
    
    public boolean relative(int axis) {
        return relative[axis];
    }
    
    public Position relative(int axis, boolean relative) {
        this.relative[axis] = relative;
        return this;
    }
    
    
    public boolean rotate() {
        return rotate;
    }
    
    public Position rotate(boolean rotate) {
        this.rotate = rotate;
        return this;
    }
    
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        
        if (!(object instanceof Position)) {
            return false;
        }
        
        var other = (Position) object;
        return Objects.equals(getWorld(), other.getWorld())
            && Math.abs(getX() - other.getX()) < EPSILON
            && Math.abs(getY() - other.getY()) < EPSILON
            && Math.abs(getZ() - other.getZ()) < EPSILON
            && Math.abs(getYaw() - other.getYaw()) < EPSILON
            && Math.abs(getPitch() - other.getPitch()) < EPSILON
            && rotate == other.rotate 
            && Arrays.equals(relative, other.relative);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        
        hash = 19 * hash + Arrays.hashCode(this.relative);
        hash = 19 * hash + (this.rotate ? 1 : 0);
        
        return hash;
    }
    
    
    @Override
    public String toString() {
        return "rotate[" + rotate + "], " + getWorld() + "[world]" + point(getX(), X) + point(getY(), Y) + point(getZ(), Z) + ", " + getYaw() + "[yaw], " + getPitch() + "[pitch]";
    }
    
    private String point(double point, int axis) {
        var relativity = relative[axis] ? "relative" : "absolute";
        return ", " + point + "[" + relativity + "]";
    }
    
}
