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
    
    public static final Vector rotateAroundXAxis(Vector vector, double angle) {
        double y, z, cos, sin;
        cos = cos(angle);
        sin = sin(angle);
        y = vector.getY() * cos - vector.getZ() * sin;
        z = vector.getY() * sin + vector.getZ() * cos;
        return vector.setY(y).setZ(z);
    }

    public static final Vector rotateAroundYAxis(Vector vector, double angle) {
        double x, z, cos, sin;
        cos = cos(angle);
        sin = sin(angle);
        x = vector.getX() * cos + vector.getZ() * sin;
        z = vector.getX() * -sin + vector.getZ() * cos;
        return vector.setX(x).setZ(z);
    }

    public static final Vector rotateAroundZAxis(Vector vector, double angle) {
        double x, y, cos, sin;
        cos = cos(angle);
        sin = sin(angle);
        x = vector.getX() * cos - vector.getY() * sin;
        y = vector.getX() * sin + vector.getY() * cos;
        return vector.setX(x).setY(y);
    }

    
    public static final Vector rotate(Vector vector, double angleX, double angleY, double angleZ) {
        rotateAroundXAxis(vector, angleX);
        rotateAroundYAxis(vector, angleY);
        rotateAroundZAxis(vector, angleZ);
        
        return vector;
    }

    public static final Vector rotate(Vector vector, Location location) {
        return rotate(vector, location.getYaw(), location.getPitch());
    }

    public static final Vector rotate(Vector vector, float yawDegrees, float pitchDegrees) {
        double initialX, initialY, initialZ;
        double x, y, z;
        
        double yaw = toRadians(-(yawDegrees + 90));
        double pitch = toRadians(-pitchDegrees);


        // Y axis rotation (Yaw)
        double cosPitch = cos(pitch);
        double sinPitch = sin(pitch);
        
        initialX = vector.getX();
        initialY = vector.getY();
        x = initialX * cosPitch - initialY * sinPitch;
        y = initialX * sinPitch + initialY * cosPitch;

        // Z axis rotation (Pitch) 
        double cosYaw = cos(yaw);
        double sinYaw = sin(yaw);
        
        initialX = x;
        initialZ = vector.getZ();
        x = initialZ * sinYaw + initialX * cosYaw;
        z = initialZ * cosYaw - initialX * sinYaw;

        return vector.setX(x).setY(y).setZ(z);
    }

    
    public static final double angleToXAxis(Vector vector) {
        return atan2(vector.getX(), vector.getY());
    }
    
}
