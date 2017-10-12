/*
 * The MIT License
 *
 * Copyright 2014 Slikey.
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
package com.karuslabs.commons.world;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static java.lang.Math.*;


public final class Vectors {
    
    public static Vector copy(Vector source, Vector destination) {
        destination.setX(source.getX()).setY(source.getY()).setZ(source.getZ());
        return destination;
    }
    
    public static Vector copy(Location source, Vector destination) {
        destination.setX(source.getX()).setY(source.getY()).setZ(source.getZ());
        return destination;
    }
    
    
    public static Vector random(Vector vector) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        vector.setX(random.nextDouble(-1, 1));
        vector.setY(random.nextDouble(-1, 1));
        vector.setZ(random.nextDouble(-1, 1));
        vector.normalize();
        return vector;
    }

    
    public static Vector randomCircle(Vector vector) {
        double random = ThreadLocalRandom.current().nextDouble(0, 2 * PI);
        vector.setX(cos(random));
        vector.setY(0);
        vector.setZ(sin(random));
        return vector;
    }
    
    
    public static double randomAngle() {
        return ThreadLocalRandom.current().nextDouble() * 2 * PI;
    }
    
    
    public static Vector rotate(Vector vector, Vector rotation) {
        return rotate(vector, rotation.getX(), rotation.getY(), rotation.getZ());
    }
    
    public static Vector rotate(Vector vector, double angleX, double angleY, double angleZ) {
        rotateAroundXAxis(vector, angleX);
        rotateAroundYAxis(vector, angleY);
        rotateAroundZAxis(vector, angleZ);
        return vector;
    }

    
    public static Vector rotate(Vector vector, Location location) {
        return rotate(vector, location.getYaw(), location.getPitch());
    }

    
    public static Vector rotate(Vector vector, float yawDegrees, float pitchDegrees) {
        double yaw = toRadians(-1 * (yawDegrees + 90));
        double pitch = toRadians(-pitchDegrees);

        double cosYaw = cos(yaw);
        double cosPitch = cos(pitch);
        double sinYaw = sin(yaw);
        double sinPitch = sin(pitch);

        double initialX, initialY, initialZ;
        double x, y, z;

        // Z Axis rotation (Pitch)
        initialX = vector.getX();
        initialY = vector.getY();
        x = initialX * cosPitch - initialY * sinPitch;
        y = initialX * sinPitch + initialY * cosPitch;

        // Y Axis rotation (Yaw)
        initialZ = vector.getZ();
        initialX = x;
        z = initialZ * cosYaw - initialX * sinYaw;
        x = initialZ * sinYaw + initialX * cosYaw;

        return new Vector(x, y, z);
    }

    
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

    
    public static final double angleToXAxis(Vector vector) {
        return atan2(vector.getX(), vector.getY());
    }
    
}
