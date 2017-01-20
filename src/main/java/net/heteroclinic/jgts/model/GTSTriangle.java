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
 * {@code GTSTriangle} class is an composition of {@code Triangle}.
 * <p> 
 * {@code index} is the sequence number in the model's triangles segment.
 * {@code attrib} is reserved.
 * @author      Zhikai Wang &lt;www.heteroclinic.net&gt;
 * @version     1.0
 * @since       1.0
 */
public class GTSTriangle {
	
	public Triangle triangle;

	public GTSTriangle(int e1, int e2, int e3,int index,int attrib) {
		triangle = new Triangle (e1, e2, e3);
		this.index = index;
		this.attrib = attrib;
	}
	
	int index;
	int attrib;

	public GTSTriangle(Triangle triangle, int index, int attrib) {
		super();
		this.triangle = triangle;
		this.index = index;
		this.attrib = attrib;
	}
	
	public void print() {
		System.out.println("[triangle.e1,triangle.e2,triangle.e3,index,attrib]"+"["+triangle.e1+","+triangle.e2+","+triangle.e3+","+index+","+attrib+"]");
	}
}
