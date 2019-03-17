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

import com.karuslabs.annotations.Static;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static java.lang.Math.*;


public @Static class Vectors {
    
    @FunctionalInterface
    public static interface Reducer<T> {
        
        public T reduce(T container, double x, double y, double z);
        
    }
    
    public static Reducer<Vector> VECTOR = (vector, x, y, z) -> vector.setX(x).setY(y).setZ(z);
        
    public static Reducer<Location> LOCATION = (location, x, y, z) -> {
        location.setX(x);
        location.setY(y);
        location.setZ(z);
        return location;
    };

    
    public static Vector rotate(Vector vector, double angleX, double angleY, double angleZ) {
        rotateAroundXAxis(vector, angleX);
        rotateAroundYAxis(vector, angleY);
        rotateAroundZAxis(vector, angleZ);
        
        return vector;
    }
    
    /**
     * Returns the specified {@code Vector} rotated about the X axis using the specified radian angle.
     * 
     * @param vector the vector
     * @param angle the angle
     * @return the vector
     */
    public static Vector rotateAroundXAxis(Vector vector, double angle) {
        double y, z, cos, sin;
        cos = cos(angle);
        sin = sin(angle);
        y = vector.getY() * cos - vector.getZ() * sin;
        z = vector.getY() * sin + vector.getZ() * cos;
        return vector.setY(y).setZ(z);
    }

    public static Vector rotateAroundYAxis(Vector vector, double angle) {
        double x, z, cos, sin;
        cos = cos(angle);
        sin = sin(angle);
        x = vector.getX() * cos + vector.getZ() * sin;
        z = vector.getX() * -sin + vector.getZ() * cos;
        return vector.setX(x).setZ(z);
    }

    public static Vector rotateAroundZAxis(Vector vector, double angle) {
        double x, y, cos, sin;
        cos = cos(angle);
        sin = sin(angle);
        x = vector.getX() * cos - vector.getY() * sin;
        y = vector.getX() * sin + vector.getY() * cos;
        return vector.setX(x).setY(y);
    }

    public static Vector rotate(Vector vector, Location pivot) {
        return rotate(vector, pivot.getYaw(), pivot.getPitch());
    }

    public static Vector rotate(Vector vector, float yawDegrees, float pitchDegrees) {
        return rotate(VECTOR, vector, vector.getX(), vector.getY(), vector.getZ(), yawDegrees, pitchDegrees);
    }
    
    
    public static Location rotate(Location location, Location pivot) {
        return rotate(location, pivot.getYaw(), pivot.getPitch());
    }

    public static Location rotate(Location location, float yawDegrees, float pitchDegrees) {
        return rotate(LOCATION, location, location.getX(), location.getY(), location.getZ(), yawDegrees, pitchDegrees);
    }
    
    
    public static <T> T rotate(Reducer<T> reducer, T container, double initialX, double initialY, double initialZ, float yawDegrees, float pitchDegrees) {
        double x, y, z;
        
        double yaw = toRadians(-(yawDegrees + 90));
        double pitch = toRadians(-pitchDegrees);


        // Y axis rotation (Yaw)
        double cosPitch = cos(pitch);
        double sinPitch = sin(pitch);
        
        x = initialX * cosPitch - initialY * sinPitch;
        y = initialX * sinPitch + initialY * cosPitch;

        // Z axis rotation (Pitch) 
        double cosYaw = cos(yaw);
        double sinYaw = sin(yaw);
        
        initialX = x;
        x = initialZ * sinYaw + initialX * cosYaw;
        z = initialZ * cosYaw - initialX * sinYaw;

        return reducer.reduce(container, x, y, z);
    }

    
    public static double angleToXAxis(Vector vector) {
        return atan2(vector.getX(), vector.getY());
    }
    
}
