package net.heteroclinic.computergraphics.derivedfromopengl;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;

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
 * The class {@code UnlightedAxis} is a place-holder for drawing the
 * unlighted or not shaded RGB-XYZ axis. Normally in JGTS the camera 
 * scope is at about 10.f, the axis' length can be 6.0f to better
 * reference the model.
 * @author      Zhikai Wang &lt;www.heteroclinic.net&gt;
 * @version     1.0
 * @since       1.0
 */

//TODO Need know how JOGL 2.3.2 Use glPushAttrib()
public class UnlightedAxis {
	public static void draw(GL gl) {
		draw(gl, 6.0f);
	}

	/**
	 * @param gl GL2 or above
	 * @param length of the axis
	 */
	public static void draw(GL gl, float length) {
		// gl.glPushAttrib();
		// GLbitfield mask;
		((GL2) gl).glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);

		((GLMatrixFunc) gl).glPushMatrix();
		// gl.glDisable(GL2.GL_LIGHTING);
		gl.glDisable(GLLightingFunc.GL_LIGHTING);
		((GL2) gl).glColor3f(1.0f, 0.0f, 0.0f);
		((GL2) gl).glBegin(GL2.GL_LINES);
		((GL2) gl).glVertex3f(0.0f, 0.0f, 0.0f);
		((GL2) gl).glVertex3f(length, 0.0f, 0.0f);
		((GL2) gl).glEnd();

		((GL2) gl).glColor3f(0.0f, 1.0f, 0.0f);
		((GL2) gl).glBegin(GL2.GL_LINES);
		((GL2) gl).glVertex3f(0.0f, 0.0f, 0.0f);
		((GL2) gl).glVertex3f(.0f, length, 0.0f);
		((GL2) gl).glEnd();

		((GL2) gl).glColor3f(0.0f, 0.0f, 1.0f);
		((GL2) gl).glBegin(GL2.GL_LINES);
		((GL2) gl).glVertex3f(0.0f, 0.0f, 0.0f);
		((GL2) gl).glVertex3f(.0f, .0f, length);
		((GL2) gl).glEnd();
		gl.glEnable(GLLightingFunc.GL_LIGHTING);
		// glPopMatrix();
		// glEnable(GL_LIGHTING);

		// glPopAttrib();
		((GL2) gl).glPopAttrib();
		((GLMatrixFunc) gl).glPopMatrix();
	}

}
