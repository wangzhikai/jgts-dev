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
 * A {@code Triangle} is the tuple of three indices of its edges.
 * <p>
 * The three edges are in a sequence of counter-clock-wise shown in the geometry. 
 * So the order decides the orientation or normal of the triangle.
 * @author      Zhikai Wang &lt;www.heteroclinic.net&gt;
 * @version     1.0
 * @since       1.0
 */
public class Triangle {
	public int e1;
	public int e2;
	public int e3;
	public Triangle(int e1, int e2, int e3) {
		super();
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
	}	
}
