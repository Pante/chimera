/*
 * Copyright (C) 2017 Karus Labs
 * All rights reserved.
 */
package com.karuslabs.commons.util;

import java.util.Map;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;


public class LazyLocation extends Location {
    
    private String name;
    protected Server server;
    protected Location location;

    
    public LazyLocation(String name, double x, double y, double z) {
        this(name, x, y, z, 0, 0);
    }
    
    public LazyLocation(String name, double x, double y, double z, float yaw, float pitch) {
        this(name, new Location(null, x, y, z, yaw, pitch));
    }
    
    public LazyLocation(String name, Location location) {
        this(name, Bukkit.getServer(), location);
    }  
    
    public LazyLocation(String name, Server server, Location location) {
        super(null, 0, 0, 0);
        this.name = name;
        this.server = server;
        this.location = location;
    }
    
    
    public boolean isLoaded() {
        return location.getWorld() != null;
    }
    
    public void load() {
        location.setWorld(server.getWorld(name));
    }
    

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
