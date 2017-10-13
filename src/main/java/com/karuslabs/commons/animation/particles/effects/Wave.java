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
package com.karuslabs.commons.animation.particles.effects;

import com.karuslabs.commons.animation.particles.Particles;
import com.karuslabs.commons.animation.particles.effect.*;
import com.karuslabs.commons.world.BoundLocation;
import com.karuslabs.commons.world.Vectors;

import java.util.*;

import org.bukkit.*;
import org.bukkit.util.Vector;

public class Wave implements Task<Wave, BoundLocation, BoundLocation> {

	private Particles particles;
	private Particles cloudParticle;
	private int countFront;
	private int countBack;
	private int rows;
	private float lengthFront;
	private float lengthBack;
	private float depth;
	private float height;
	private float heightBack;
	private float width;
	protected final Collection<Vector> waterCache, cloudCache;
	protected boolean firstStep;

	public Wave(Particles particles, Particles cloudParticle) {
		this(particles, cloudParticle, 10, 10, 20, 1.5f, 3, 1, 0.5f, 2, 5);
	}

	public Wave(Particles particles, Particles cloudParticle, int countFront, int countBack, int rows, float lengthFront, float lengthBack, float depth, float height, float heightBack, float width) {
		this.particles = particles;
		this.cloudParticle = cloudParticle;
		this.countFront = countFront;
		this.countBack = countBack;
		this.rows = rows;
		this.lengthFront = lengthFront;
		this.lengthBack = lengthBack;
		this.depth = depth;
		this.height = height;
		this.heightBack = heightBack;
		this.width = width;
		this.waterCache = new HashSet<Vector>();
		this.cloudCache = new HashSet<Vector>();
		this.firstStep = true;
	}

	public void invalidate(Location location) {
		firstStep = false;
		waterCache.clear();
		cloudCache.clear();

		Vector front = new Vector(-lengthFront, 0, 0);
		Vector back = new Vector(lengthBack, 0, 0);
		Vector rise = new Vector(-0.5 * lengthFront, height, 0);

		Vector n1, n2, n_s1ToH, n_s2ToH, c1, c2, s1ToH, s2ToH;
		float len_s1ToH, len_s2ToH, yaw;

		s1ToH = rise.clone().subtract(front);
		c1 = front.clone().add(s1ToH.clone().multiply(0.5));
		len_s1ToH = (float) s1ToH.length();
		n_s1ToH = s1ToH.clone().multiply(1f / len_s1ToH);
		n1 = new Vector(s1ToH.getY(), -s1ToH.getX(), 0).normalize();
		
		if (n1.getX() < 0) {
			n1.multiply(-1);
		}

		s2ToH = rise.clone().subtract(back);
		c2 = back.clone().add(s2ToH.clone().multiply(0.5));
		len_s2ToH = (float) s2ToH.length();
		n_s2ToH = s2ToH.clone().multiply(1f / len_s2ToH);
		n2 = new Vector(s2ToH.getY(), -s2ToH.getX(), 0).normalize();
		
		if (n2.getX() < 0) {
			n2.multiply(-1);
		}

		yaw = (float) ((-location.getYaw() + 90) * (Math.PI / 180));

		for (int i = 0; i < countFront; i++) {
			float ratio = (float) i / countFront;
			float x = (ratio - .5f) * len_s1ToH;
			float y = (float) (-depth / Math.pow((len_s1ToH / 2), 2) * Math.pow(x, 2) + depth);
			Vector v = c1.clone();
			
			v.add(n_s1ToH.clone().multiply(x));
			v.add(n1.clone().multiply(y));
			
			for (int j = 0; j < rows; j++) {
				float z = ((float) j / rows - .5f) * width;
				Vector vec = v.clone().setZ(v.getZ() + z);
				Vectors.rotateAroundYAxis(vec, yaw);
				
				if (i == 0 || i == countFront - 1) {
					cloudCache.add(vec);
				} else {
					waterCache.add(vec);
				}
			}
		}

		for (int i = 0; i < countBack; i++) {
			float ratio = (float) i / countBack;
			float x = (ratio - .5f) * len_s2ToH;
			float y = (float) (-heightBack / Math.pow((len_s2ToH / 2), 2) * Math.pow(x, 2) + heightBack);	
			Vector v = c2.clone();
			
			v.add(n_s2ToH.clone().multiply(x));
			v.add(n2.clone().multiply(y));
			
			for (int j = 0; j < rows; j++) {
				float z = ((float) j / rows - .5f) * width;
				Vector vec = v.clone().setZ(v.getZ() + z);
				Vectors.rotateAroundYAxis(vec, yaw);
				
				if (i == countFront - 1) {
					cloudCache.add(vec);
				} else {
					waterCache.add(vec);
				}
			}
		}
	}

	@Override
	public void render(Context<BoundLocation, BoundLocation> context) {
		Location location = context.getOrigin().getLocation();
		Vector velocity = context.getVector();
		
		if (firstStep) {
			velocity.copy(location.getDirection().setY(0).normalize().multiply(0.2));
			invalidate(location);
		}
		
		location.add(velocity);

		for (Vector v : cloudCache) {
			location.add(v);
			context.render(cloudParticle, location);
			location.subtract(v);
		}
		
		for (Vector v : waterCache) {
			location.add(v);
			context.render(particles, location);
			location.subtract(v);
		}
	}

	@Override
	public Wave get() {
		return this;
	}

}
