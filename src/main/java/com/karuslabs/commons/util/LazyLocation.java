/*
 * Copyright (C) 2017 Karus Labs
 * All rights reserved.
 */
package com.karuslabs.commons.util;

import com.karuslabs.commons.annotations.Proxied;

import java.util.Map;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;


public class LazyLocation extends Location {
    
    private String name;
    private Server server;
    private Location location;

    
    public LazyLocation(String name, Location location) {
        this(name, Bukkit.getServer(), location);
    }
    
    public LazyLocation(String name, Server server, Location location) {
        super(null, 0, 0, 0);
        this.name = name;
        this.server = server;
        this.location = location;
    }
    


    @Proxied
    @Override
    public void setWorld(World world) {
        location.setWorld(world);
    }
    
    @Override
    public World getWorld() {
        if (location.getWorld() == null) {
            location.setWorld(server.getWorld(name));
        }
        
        return location.getWorld();
    }
    
    @Proxied
    @Override
    public Chunk getChunk() {
        return location.getChunk();
    }
    
    @Proxied
    @Override
    public Block getBlock() {
        return location.getBlock();
    }
    
    @Proxied
    @Override
    public void setX(double x) {
        location.setX(x);
    }

    
    @Proxied
    @Override
    public double getX() {
        return location.getX();
    }
    
    @Proxied
    @Override
    public int getBlockX() {
        return location.getBlockX();
    }
    
    @Proxied
    @Override
    public void setY(double y) {
        location.setY(y);
    }

    @Proxied
    @Override
    public double getY() {
        return location.getY();
    }

    @Proxied
    @Override
    public int getBlockY() {
        return location.getBlockY();
    }

    @Proxied
    @Override
    public void setZ(double z) {
        location.setZ(z);
    }

    @Proxied
    @Override
    public double getZ() {
        return location.getZ();
    }

    @Proxied
    @Override
    public int getBlockZ() {
        return location.getBlockZ();
    }

    @Proxied
    @Override
    public void setYaw(float yaw) {
        location.setYaw(yaw);
    }

    @Proxied
    @Override
    public float getYaw() {
        return location.getYaw();
    }

    @Proxied
    @Override
    public void setPitch(float pitch) {
        location.setPitch(pitch);
    }

    @Proxied
    @Override
    public float getPitch() {
        return location.getPitch();
    }

    @Proxied
    @Override
    public Vector getDirection() {
        return location.getDirection();
    }

    @Proxied
    @Override
    public Location setDirection(Vector vector) {
        return location.setDirection(vector);
    }

    @Proxied
    @Override
    public Location add(Location vec) {
        return location.add(vec);
    }

    @Proxied
    @Override
    public Location add(Vector vec) {
        return location.add(vec);
    }

    @Proxied
    @Override
    public Location add(double x, double y, double z) {
        return location.add(x, y, z);
    }

    @Proxied
    @Override
    public Location subtract(Location vec) {
        return location.subtract(vec);
    }

    @Proxied
    @Override
    public Location subtract(Vector vec) {
        return location.subtract(vec);
    }

    @Proxied
    @Override
    public Location subtract(double x, double y, double z) {
        return location.subtract(x, y, z);
    }

    @Proxied
    @Override
    public double length() {
        return location.length();
    }

    @Proxied
    @Override
    public double lengthSquared() {
        return location.lengthSquared();
    }

    @Proxied
    @Override
    public double distance(Location o) {
        return location.distance(o);
    }

    @Proxied
    @Override
    public double distanceSquared(Location o) {
        return location.distanceSquared(o);
    }

    @Proxied
    @Override
    public Location multiply(double m) {
        return location.multiply(m);
    }

    @Proxied
    @Override
    public Location zero() {
        return location.zero();
    }

    public String toString() {
        return location.toString();
    }

    @Proxied
    @Override
    public Vector toVector() {
        return location.toVector();
    }
    
    @Proxied
    @Override
    public Location clone() {
        return location.clone();
    }


    @Proxied
    @Override
    public Map<String, Object> serialize() {
        return location.serialize();
    }
    
    @Override
    public boolean equals(Object o) {
        Object target = o;
        if (o instanceof LazyLocation) {
            target = ((LazyLocation) o).location;
        }
        return this.location.equals(target);
    }

    @Override
    public int hashCode() {
        return this.location.hashCode();
    }
    
}
