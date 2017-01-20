package net.heteroclinic.computergraphics.derivedfromopengl;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
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
 * The {@code GLMaterialf} is the implementation of OpenGL material with Java float values.
 * <p> 
 * Complete material for rendering  should include shineness, specular, ambient, diffuse and 
 * emission properties. Each property is a four-tuple (r,g,b,a). The r, g, b, a range in [0,255].
 * @author      Zhikai Wang &lt;www.heteroclinic.net&gt;
 * @version     1.0
 * @since       1.0
 */

public class GLMaterialf {

	/**
	 * Apply front material for given rendering
	 * 
	 * @param drawable no longer used in JOGL 2.3.2
	 */
	@Deprecated
	public void applyFront(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glMaterialf(GL.GL_FRONT, GLLightingFunc.GL_SHININESS, this.shineness);
		gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_SPECULAR, this.specular, 0);
		gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_AMBIENT, this.ambient, 0);
		gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_DIFFUSE, this.diffuse, 0);
		gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_EMISSION, this.emission, 0);
	}

	/**
	 * Apply back material for given rendering
	 * 
	 * @param drawable no longer used in JOGL 2.3.2
	 */
	@Deprecated
	public void applyBack(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glMaterialf(GL.GL_BACK, GLLightingFunc.GL_SHININESS, this.shineness);
		gl.glMaterialfv(GL.GL_BACK, GLLightingFunc.GL_SPECULAR, this.specular, 0);
		gl.glMaterialfv(GL.GL_BACK, GLLightingFunc.GL_AMBIENT, this.ambient, 0);
		gl.glMaterialfv(GL.GL_BACK, GLLightingFunc.GL_DIFFUSE, this.diffuse, 0);
		gl.glMaterialfv(GL.GL_BACK, GLLightingFunc.GL_EMISSION, this.emission, 0);

	}

	/**
	 * Apply back and front with same material for given rendering
	 * @param drawable no longer used in JOGL 2.3.2
	 */
	@Deprecated
	public void applyFrontBack(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glMaterialf(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_SHININESS, this.shineness);
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_SPECULAR, this.specular, 0);
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_AMBIENT, this.ambient, 0);
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_DIFFUSE, this.diffuse, 0);
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_EMISSION, this.emission, 0);

	}

	public static final GLMaterialf BRASS = new GLMaterialf("BRASS", 27.8f, new float[] { 0.99f, 0.91f, 0.81f, 0.8f },
			new float[] { 0.7f, 0.57f, 0.11f, 0.8f }, new float[] { 0.33f, 0.22f, 0.33f, 0.8f },
			new float[] { 0.0f, 0.0f, 0.0f, 0.8f });
	public static final GLMaterialf BLUE_SHINY = new GLMaterialf("BLUE_SHINY", 100.0f,
			new float[] { 1.0f, 1.0f, 1.0f, 1.0f }, new float[] { 0.0f, 0.0f, 0.7f, 1.0f },
			new float[] { 0.0f, 0.0f, 0.0f, 1.0f }, new float[] { 0.0f, 0.0f, 0.0f, 1.0f });
	public static final GLMaterialf WHITE_SHINY = new GLMaterialf("WHITE_SHINY", 100.0f,
			new float[] { 1.0f, 1.0f, 1.0f, 1.0f }, new float[] { 1.0f, 1.0f, 1.0f, 1.0f },
			new float[] { 1.0f, 1.0f, 1.0f, 1.0f }, new float[] { 0.0f, 0.0f, 0.0f, 1.0f });
	public static final GLMaterialf RED_PLASTIC = new GLMaterialf("RED_PLASTIC", 32.0f,
			new float[] { 0.0f, 0.6f, 0.6f, 0.3f }, new float[] { 0.6f, 0.0f, 0.0f, 0.3f },
			new float[] { 0.3f, 0.0f, 0.0f, 0.3f }, new float[] { 0.0f, 0.0f, 0.0f, 0.3f });
	public static final GLMaterialf GREY = new GLMaterialf("GREY", 85.0f, new float[] { 0.85f, 0.85f, 0.85f, 1.0f },
			new float[] { 0.85f, 0.85f, 0.85f, 1.0f }, new float[] { 0.85f, 0.85f, 0.85f, 1.0f },
			new float[] { 0.0f, 0.0f, 0.0f, 1.0f });
	public static final GLMaterialf DEFAULT_BACK = GREY;

	public static void main(String[] args) {
		GLMaterialf glmf = new GLMaterialf("Test_material", 27.8f, new float[] { 0.99f, 0.91f, 0.81f, 0.8f },
				new float[] { 0.7f, 0.57f, 0.11f, 0.8f }, new float[] { 0.33f, 0.22f, 0.33f, 0.8f },
				new float[] { 0.0f, 0.0f, 0.0f, 0.8f });
		System.out.println(glmf);

		System.out.println(GLMaterialf.BLUE_SHINY);

	}

	public GLMaterialf(String material_name, float shineness, float[] specular, float[] diffuse, float[] ambient,
			float[] emission) {
		this.material_name = material_name;
		this.shineness = shineness;
		this.specular = specular;
		this.diffuse = diffuse;
		this.ambient = ambient;
		this.emission = emission;
	}

	protected String material_name = "Not set";
	protected float[] specular = { 0.f, 0.f, 0.f, 0.f };
	protected float[] diffuse = { 0.f, 0.f, 0.f, 0.f };
	protected float[] ambient = { 0.f, 0.f, 0.f, 0.f };
	protected float[] emission = { 0.f, 0.f, 0.f, 0.f };

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{Material_name,[" + material_name + "]};");
		sb.append("{shineness,[" + shineness + "]};");
		sb.append("{specular,[" + specular[0] + "," + specular[1] + "," + specular[2] + "," + specular[3] + "]};");

		sb.append("{diffuse,[" + diffuse[0] + "," + diffuse[1] + "," + diffuse[2] + "," + diffuse[3] + "]};");
		sb.append("{ambient,[" + ambient[0] + "," + ambient[1] + "," + ambient[2] + "," + ambient[3] + "]};");
		sb.append("{emission,[" + emission[0] + "," + emission[1] + "," + emission[2] + "," + emission[3] + "]};");

		return sb.toString();
	}

	protected float shineness = 0;

	public float getShineness() {
		return shineness;
	}

	public void setShineness(float shineness) {
		this.shineness = shineness;
	}

	public float[] getSpecular() {
		return specular;
	}

	public void setSpecular(float[] specular) {
		this.specular = specular;
	}

	public float[] getDiffuse() {
		return diffuse;
	}

	public void setDiffuse(float[] diffuse) {
		this.diffuse = diffuse;
	}

	public float[] getAmbient() {
		return ambient;
	}

	public void setAmbient(float[] ambient) {
		this.ambient = ambient;
	}

	public float[] getEmission() {
		return emission;
	}

	public void setEmission(float[] emission) {
		this.emission = emission;
	}

}