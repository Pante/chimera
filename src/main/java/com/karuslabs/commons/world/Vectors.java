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

import com.karuslabs.commons.annotation.Static;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static java.lang.Math.*;


/**
 * This class consists exclusively of static methods which operate on and return {@code Vector}s.
 * This class is a derivative of EffectLib's VectorUtils.
 */
@Static
public final class Vectors {
    
    /**
     * Copies the x,y and z coordinates from the specified {@code Location} source to the destination {@code Vector}.
     * 
     * @param source the Location source
     * @param destination the destination
     * @return the destination vector
     */
    public static Vector copy(Location source, Vector destination) {
        destination.setX(source.getX()).setY(source.getY()).setZ(source.getZ());
        return destination;
    }
    
    
    /**
     * Randomises the X, Y and Z axises of the specified {@code Vector} between -1.0, inclusive and 1.0, exclusive,
     * and normalises th X, Y and Z axises.
     * 
     * @param vector the vector
     * @return the vector
     */
    public static Vector random(Vector vector) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        vector.setX(random.nextDouble(-1, 1));
        vector.setY(random.nextDouble(-1, 1));
        vector.setZ(random.nextDouble(-1, 1));
        vector.normalize();
        return vector;
    }

    
    /**
     * 
     * 
     * @param vector TODO
     * @return TODO
     */
    public static Vector randomCircle(Vector vector) {
        double random = ThreadLocalRandom.current().nextDouble(0, 2 * PI);
        vector.setX(cos(random));
        vector.setY(0);
        vector.setZ(sin(random));
        return vector;
    }
    
    
    /**
     * Returns an random radian angle between 0 and 2π.
     * 
     * @return the angle
     */
    public static double randomAngle() {
        return ThreadLocalRandom.current().nextDouble(0, 2 * PI); 
    }
    
    /**
     * Returns the specified {@code Vector} rotated about the X, Y and Z axises using the specified radian {@code rotation}.
     * 
     * @param vector the vector
     * @param rotation the rotation
     * @return the vector
     */
    public static Vector rotate(Vector vector, Vector rotation) {
        return rotate(vector, rotation.getX(), rotation.getY(), rotation.getZ());
    }
    
    /**
     * Returns the specified {@code Vector} rotated about the X, Y and Z axises using the
     * specified radian angles respectively.
     * 
     * @param vector the vector
     * @param angleX the angle to rotate the Vector about the X axis
     * @param angleY the angle to rotate the Vector about the Y axis
     * @param angleZ the angle to rotate the Vector about the Z axis
     * @return the rotated vector
     */
    public static Vector rotate(Vector vector, double angleX, double angleY, double angleZ) {
        rotateAroundXAxis(vector, angleX);
        rotateAroundYAxis(vector, angleY);
        rotateAroundZAxis(vector, angleZ);
        return vector;
    }

    
    /**
     * Returns the specified {@code Vector} rotated about the specified {@code Location} using the direction of the {@code Location}.
     * 
     * @param vector the vector
     * @param location the location
     * @return the vector
     */
    public static Vector rotate(Vector vector, Location location) {
        return rotate(vector, location.getYaw(), location.getPitch());
    }

    
    /**
     * Returns the specified {@code Vector} rotated using the specified radian yaw and pitch.
     * 
     * @param vector the vector
     * @param yawDegrees the yaw
     * @param pitchDegrees the pitch
     * @return the vector
     */
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

    
    /**
     * Returns the specified {@code Vector} rotated about the Y axis using the specified radian angle.
     * 
     * @param vector the vector
     * @param angle the angle
     * @return the vector
     */
    public static Vector rotateAroundYAxis(Vector vector, double angle) {
        double x, z, cos, sin;
        cos = cos(angle);
        sin = sin(angle);
        x = vector.getX() * cos + vector.getZ() * sin;
        z = vector.getX() * -sin + vector.getZ() * cos;
        return vector.setX(x).setZ(z);
    }

    
    /**
     * Returns the specified {@code Vector} rotated about the Z axis using the specified radian angle.
     * 
     * @param vector the vector
     * @param angle the angle
     * @return the vector
     */
    public static Vector rotateAroundZAxis(Vector vector, double angle) {
        double x, y, cos, sin;
        cos = cos(angle);
        sin = sin(angle);
        x = vector.getX() * cos - vector.getY() * sin;
        y = vector.getX() * sin + vector.getY() * cos;
        return vector.setX(x).setY(y);
    }

    
    /**
     * 
     * 
     * @param vector TODO
     * @return TODO
     */
    public static final double angleToXAxis(Vector vector) {
        return atan2(vector.getX(), vector.getY());
    }
    
}
