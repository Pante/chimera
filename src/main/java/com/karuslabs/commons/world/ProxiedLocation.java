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
package com.karuslabs.commons.world;

import com.karuslabs.commons.annotation.ValueBased;

import java.util.Map;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;


@ValueBased
public abstract class ProxiedLocation extends Location {
    
    protected Location location;

    
    public ProxiedLocation(Location location) {
        super(null, 0, 0, 0);
        this.location = location;
    }


    @Override
    public void setWorld(World world) {
        location.setWorld(world);
    }

    @Override
    public World getWorld() {
        return location.getWorld();
    }

    @Override
    public Chunk getChunk() {
        return location.getChunk();
    }

    @Override
    public Block getBlock() {
        return location.getBlock();
    }

    @Override
    public void setX(double x) {
        location.setX(x);
    }

    @Override
    public double getX() {
        return location.getX();
    }

    @Override
    public int getBlockX() {
        return location.getBlockX();
    }

    @Override
    public void setY(double y) {
        location.setY(y);
    }

    @Override
    public double getY() {
        return location.getY();
    }

    @Override
    public int getBlockY() {
        return location.getBlockY();
    }

    @Override
    public void setZ(double z) {
        location.setZ(z);
    }

    @Override
    public double getZ() {
        return location.getZ();
    }

    @Override
    public int getBlockZ() {
        return location.getBlockZ();
    }

    @Override
    public void setYaw(float yaw) {
        location.setYaw(yaw);
    }

    @Override
    public float getYaw() {
        return location.getYaw();
    }

    @Override
    public void setPitch(float pitch) {
        location.setPitch(pitch);
    }

    @Override
    public float getPitch() {
        return location.getPitch();
    }

    @Override
    public Vector getDirection() {
        return location.getDirection();
    }

    @Override
    public Location setDirection(Vector vector) {
        return location.setDirection(vector);
    }

    @Override
    public Location add(Location vec) {
        return location.add(vec);
    }

    @Override
    public Location add(Vector vec) {
        return location.add(vec);
    }

    @Override
    public Location add(double x, double y, double z) {
        return location.add(x, y, z);
    }

    @Override
    public Location subtract(Location vec) {
        return location.subtract(vec);
    }

    @Override
    public Location subtract(Vector vec) {
        return location.subtract(vec);
    }

    @Override
    public Location subtract(double x, double y, double z) {
        return location.subtract(x, y, z);
    }

    @Override
    public double length() {
        return location.length();
    }

    @Override
    public double lengthSquared() {
        return location.lengthSquared();
    }

    @Override
    public double distance(Location o) {
        return location.distance(o);
    }

    @Override
    public double distanceSquared(Location o) {
        return location.distanceSquared(o);
    }

    @Override
    public Location multiply(double m) {
        return location.multiply(m);
    }

    @Override
    public Location zero() {
        return location.zero();
    }

    @Override
    public String toString() {
        return location.toString();
    }

    @Override
    public Vector toVector() {
        return location.toVector();
    }

    @Override
    public Location clone() {
        return location.clone();
    }

    @Override
    public void checkFinite() throws IllegalArgumentException {
        location.checkFinite();
    }

    @Override
    public Map<String, Object> serialize() {
        return location.serialize();
    }

    @Override
    public boolean equals(Object o) {
        Object target = o;
        if (o instanceof ProxiedLocation) {
            target = ((ProxiedLocation) o).location;
        }
        return location.equals(target);
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }
    
}
