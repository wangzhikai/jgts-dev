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
 * {@code GTSEdge} class is a composition of {@code Edge}.
 * <p>
 * {@code index} is the sequence number in the model's edges segment.
 * {@code attrib} is reserved.
 * @author      Zhikai Wang &lt;www.heteroclinic.net&gt;
 * @version     1.0
 * @since       1.0
 */
public class GTSEdge {
	public Edge gp;
	public int index;
	public int attrib;
	public GTSEdge(Edge gp, int index, int attrib) {
		this.gp = gp;
		this.index = index;
		this.attrib = attrib;
	}
	public void print() {
		System.out.println("[gp.beg,gp.fin,index,attrib]"+"["+gp.beg+","+gp.fin+","+index+","+attrib+"]");
	}
}
