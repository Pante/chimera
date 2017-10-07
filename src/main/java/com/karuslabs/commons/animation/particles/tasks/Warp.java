package com.karuslabs.commons.animation.particles.tasks;

import com.karuslabs.commons.animation.particles.Particles;
import com.karuslabs.commons.animation.particles.effect.*;
import com.karuslabs.commons.world.BoundLocation;

import org.bukkit.Location;

public class Warp implements MemoisableTask<BoundLocation, BoundLocation> {

	private Particles particles;
	private float radius;
	private int total;
	private float grow;
	private int rings;
	
	public Warp(Particles particles) {
		this(particles, 1, 20, 0.2f, 12);
	}
	
	public Warp(Particles particles, float radius, int total, float grow, int rings) {
		this.particles = particles;
		this.radius = radius;
		this.total = total;
		this.grow = grow;
		this.rings = rings;
	}
	
	@Override
	public void render(Context<BoundLocation, BoundLocation> context) {
		Location location = context.getOrigin().getLocation();
        double x, y, z;
        
        y = context.getCurrent() * grow;
        location.add(0, y, 0);
        
        for (int i = 0; i < total; i++) {
            double angle = (double) 2 * Math.PI * i / total;
            
            x = Math.cos(angle) * radius;
            z = Math.sin(angle) * radius;
            location.add(x, 0, z);
            context.render(particles, location);
            location.subtract(x, 0, z);
        }
        
        location.subtract(0, y, 0);
	}
}
