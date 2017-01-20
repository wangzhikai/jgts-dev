package net.heteroclinic.computergraphics.derivedfromopengl;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import net.heteroclinic.jgts.model.Point;

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
 * The {@code GLBasket} class is for convenience by saving typing time. 
 * <p>
 * When you type GLBasket, you will get the proper API hints, it also adapt our 
 * own ADTs, advanced date types, to OpenGL API.
 * @author      Zhikai Wang &lt;www.heteroclinic.net&gt;
 * @version     1.0
 * @since       1.0
 */
public class GLBasket {

	/**
	 * A hinted function to adapt to OpenGL glVertex3fv. 
	 * <p>
	 * @param gl GL2 or higher
	 * @param pt a Point has (x,y,z) 
	 * @see   GLColor4ub
	 */
	static public void glVertex3fv(GL gl, Point  pt) {
		((GL2) gl).glVertex3f(pt.x, pt.y, pt.z);
	}
	
	/**
	 * A hinted function to adapt to OpenGL glColor4ub.
	 * @param gl GL2 or higher
	 * @param color a color has (r,g,b,a)
	 * @see   Point
	 */
	static public void glColor4ub(GL gl, GLColor4ub  color) {
		((GL2) gl).glColor4ub(color.getR(), color.getG(), color.getB(),color.getAlpha());
	}	
}
