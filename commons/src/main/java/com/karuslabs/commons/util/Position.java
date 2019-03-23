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


/**
 * A 3D position in a world and relativity of which.
 */
public class Position extends Location {
    
    private static final double EPSILON = 0.000001;
    
    
    /**
     * The X axis.
     */
    public static final int X = 0;
    /**
     * The Y axis.
     */
    public static final int Y = 1;
    /**
     * The Z axis.
     */
    public static final int Z = 2;
    
    
    private boolean[] relative;
    private boolean rotate;
    
    
    /**
     * Creates a {@code Position} with no world at {@code 0, 0, 0}.
     */
    public Position() {
        this(0, 0, 0);
    }
    
    /**
     * Creates a {@code Position} with no world at the given coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public Position(double x, double y, double z) {
        this(null, x, y, z);
    }
    
    /**
     * Creates a {@code Position} with the given world and coordinates.
     * 
     * @param world the world
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public Position(World world, double x, double y, double z) {
        this(world, x, y, z, 0, 0);
    }
    
    /**
     * Creates a {@code Position} with the given world, coordinates and direction
     * 
     * @param world the world
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param yaw the yaw
     * @param pitch the pitch
     */
    public Position(World world, double x, double y, double z, float yaw, float pitch) {
        super(world, x, y, z, yaw, pitch);
        relative = new boolean[]{ false, false, false };
        rotate = false;
    }
    
    
    /**
     * Sets the coordinates of the given location as the absolute coordinates of
     * this position relative to the {@code origin}.
     * 
     * @param to the location this position is to be applied
     * @param origin the origin
     */
    public void apply(Location to, Location origin) {
        to.setX(relative[X] ? getX() + origin.getX() : getX());
        to.setY(relative[Y] ? getY() + origin.getY() : getY());
        to.setZ(relative[Z] ? getZ() + origin.getZ() : getZ());
        
        if (rotate) Vectors.rotate(to, origin);
    }
    
    /**
     * Sets the coordinates of the given vector as the absolute coordinates of
     * this position relative to the {@code origin}.
     * 
     * @param to the vector this position is to be applied
     * @param origin the origin
     */
    public void apply(Vector to, Location origin) {
        to.setX(relative[X] ? getX() + origin.getX() : getX())
          .setY(relative[Y] ? getY() + origin.getY() : getY())
          .setZ(relative[Z] ? getZ() + origin.getZ() : getZ());
        
        if (rotate) Vectors.rotate(to, origin);
    }
    
    /**
     * 
     * Relativizes this position about the given origin.
     * 
     * @param origin the origin
     */
    public void relativize(Location origin) {
        if (relative[X]) setX(getX() + origin.getX());
        if (relative[Y]) setY(getY() + origin.getY());
        if (relative[Z]) setZ(getZ() + origin.getZ());
        if (rotate) Vectors.rotate(this, origin);
    }
    
    
    /**
     * Sets the coordinate for the given axis.
     * 
     * @param axis the axis
     * @param value the coordinate for the axis
     * @return this
     * @throws IllegalArgumentException if an invalid argument was specified
     */
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
    
    
    /**
     * Returns whether the coordinate for the given axis is absolute or relative.
     * 
     * @param axis the axis
     * @return true if the coordinate for the given axis is relative
     */
    public boolean relative(int axis) {
        return relative[axis];
    }
    
    /**
     * Sets the relativity of the coordinate for the given axis.
     * 
     * @param axis the axis
     * @param relative true if the coordinate for the axis is relative
     * @return this
     */
    public Position relative(int axis, boolean relative) {
        this.relative[axis] = relative;
        return this;
    }
    
    
    /**
     * Returns whether this position is to be rotated when applied.
     * 
     * @return true if this position is to be rotated
     */
    public boolean rotate() {
        return rotate;
    }
    
    /**
     * Sets whether this position is to be rotated when applied.
     * 
     * @param rotate true if this position is to be rotated
     * @return this
     */
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
