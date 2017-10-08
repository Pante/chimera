package com.karuslabs.commons.animation.particles.tasks;

import com.karuslabs.commons.animation.particles.Particles;
import com.karuslabs.commons.animation.particles.effect.*;
import com.karuslabs.commons.world.BoundLocation;
import com.karuslabs.commons.world.Vectors;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Vortex implements MemoisableTask<BoundLocation, BoundLocation> {

	private Particles particles;
	private float radius;
	private float grow;
	private double radials;
	private int circles;
	private int helixes;
	
	public Vortex(Particles particles) {
		this(particles, 2, 0.5f, Math.PI / 16, 3, 4);
	}
	
	public Vortex(Particles particles, float radius, float grow, double radials, int circles, int helixes) {
		this.particles = particles;
		this.radius = radius;
		this.grow = grow;
		this.radials = radials;
		this.circles = circles;
		this.helixes = helixes;
	}
	
	@Override
	public void render(Context<BoundLocation, BoundLocation> context) {
		Location location = context.getOrigin().getLocation();
		
        for (int x = 0; x < circles; x++) {
            for (int i = 0; i < helixes; i++) {
                double angle = context.getCurrent() * radials + (2 * Math.PI * i / helixes);
                Vector v = new Vector(Math.cos(angle) * radius, context.getCurrent() * grow, Math.sin(angle) * radius);
                
                Vectors.rotateAroundXAxis(v, (location.getPitch() + 90) * (Math.PI / 180));
                Vectors.rotateAroundYAxis(v, -location.getYaw() * (Math.PI / 180));

                location.add(v);
                context.render(particles, location);
                location.subtract(v);
            }
        }
	}
}
