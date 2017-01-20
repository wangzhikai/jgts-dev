package net.heteroclinic.jgts.model;
/**
 * Copyright 2016 Zhikai Wang <www.heteroclinic.net>. All rights reserved.
 * Science and Technology Promotion License 
 * All third-party licenses are automatically cascaded.
 * Non-commercial usage of this file is not limited.
 * Commercial usage is allowed, given this file is not modified. 
 * Inheritance/overriding/re-factoring is suggested at higher level.
 * This is a good-will software. Users' liability always. 
 */
/**
 * JGTS not necessarily a clone of GTS.
 * <p>
 * A {@code Vector} 3D is a three tuple of (x,y,z).
 * <p>
 * @author      Zhikai Wang &lt;www.heteroclinic.net&gt;
 * @version     1.0
 * @since       1.0
 */
public class Vector extends Point {

	/**
	 * @param x x
	 * @param y y
	 * @param z z
	 */
	public Vector(float x, float y, float z) {
		super(x, y, z);
	}

	/**
	 * @return the length of the vector
	 */
	public float length() {
		return (float) Math.sqrt(this.x*this.x+this.y*this.y+this.z*this.z);
	}
}
