package com.karuslabs.commons.animation.particles.tasks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import com.karuslabs.commons.animation.particles.Particles;
import com.karuslabs.commons.animation.particles.effect.Context;
import com.karuslabs.commons.animation.particles.effect.MemoisableTask;
import com.karuslabs.commons.world.BoundLocation;

public class Trace implements MemoisableTask<BoundLocation, BoundLocation> {

	private Particles particles;
	private int refresh;
	private int maxWayPoints;
	protected final List<Vector> wayPoints;
	protected World world;
	
	public Trace(Particles particles) {
		this(particles, 5, 30);
	}
	
	public Trace(Particles particles, int refresh, int maxWayPoints) {
		this.particles = particles;
		this.refresh = refresh;
		this.maxWayPoints = maxWayPoints;
		this.wayPoints = new ArrayList<Vector>();
	}
	
	@Override
    public void render(Context<BoundLocation, BoundLocation> context) {
		Location location = context.getOrigin().getLocation();
		
        if (world == null) {
            world = location.getWorld();
        } else if (!location.getWorld().equals(world)) {
            return;
        }

        synchronized(wayPoints) {
            if (wayPoints.size() >= maxWayPoints) {
                wayPoints.remove(0);
            }
        }

        wayPoints.add(location.toVector());

        if (context.getCurrent() % refresh != 0) {
            return;
        }

        synchronized(wayPoints)
        {
            for (Vector position : wayPoints) {
                Location particleLocation = new Location(world, position.getX(), position.getY(), position.getZ());
                context.render(particles, particleLocation);
            }
        }
		
	}
}
