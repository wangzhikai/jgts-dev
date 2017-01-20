package net.heteroclinic.jgts;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.JoglVersion;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.TileRendererBase;
import net.heteroclinic.computergraphics.derivedfromopengl.UnlightedAxis;
import net.heteroclinic.jgts.model.Model;
import net.heteroclinic.jgts.model.joglext.DrawableModel;
/**
 * Copyright 2010 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY JogAmp Community ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JogAmp Community OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of JogAmp Community.
 */
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
 * The class {@code GTSModelViewerListener} is edited from a JOGL listner in the unit test 
 * suite. It full-fills the overriding behavior to render a GTS model.
 * <p>
 * Since the model is properly sized/scaled, we can use fixed setting of a camera for 
 * any model regardless the geometry of the model.
 * <blockquote><pre>
 *     gl.glOrtho(-10.0d, 10.0d, -10.0d, 10.0d, 10.0d, 300.0d);
 *     glu.gluLookAt(5.0f, 10f, 20f, 0f, 0f, 0f, 0f, 1f, 0f);
 * </pre></blockquote><p>
 * @author      Zhikai Wang &lt;www.heteroclinic.net&gt;
 * @version     1.0
 * @since       1.0
 * @see			net.heteroclinic.jgts.model.Model
 */
//TODO answer the questions with TODO
public class GTSModelViewerListener extends Base2d3d2NEWTListener {
	protected DrawableModel model;
	protected GLU glu = new GLU();

	public GTSModelViewerListener(DrawableModel model) {
		this.model = model;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(DrawableModel model) {
		this.model = model;
	}

	@Override
	public boolean init(final GL2 gl) {

		System.err.println("GTSModelViewerListener init(final GL2 gl) ");
		// if (null != sharedGears && !sharedGears.isInit()) {
		// if (!isInit()) {
		// System.err.println(Thread.currentThread() + "init(final GL2 gl)
		// returns false.");
		// return false;
		// }
		final float lightPos[] = { 5.0f, 5.0f, 10.0f, 0.0f };
		// final float red[] = { 0.8f, 0.1f, 0.0f, 0.7f };
		// final float green[] = { 0.0f, 0.8f, 0.2f, 0.7f };
		// final float blue[] = { 0.2f, 0.2f, 1.0f, 0.7f };

		System.err.println(Thread.currentThread() + " Gears.init: tileRendererInUse " + tileRendererInUse);
		if (verbose) {
			System.err.println("GearsES2 init on " + Thread.currentThread());
			System.err.println("Chosen GLCapabilities: " + gl.getContext().getGLDrawable().getChosenGLCapabilities());
			System.err.println("INIT GL IS: " + gl.getClass().getName());
			System.err.println(JoglVersion.getGLStrings(gl, null, false).toString());
		}

		gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_POSITION, lightPos, 0);
		float ambient[] = { 0.2f, 0.2f, 0.2f, 1 };
		float position[] = { 1.0f, 10.0f, 5.7f, 1 };
		float intensity[] = { 1, 1, 1, 1 };
		float specColor[] = { 1, 1, 1, 1 };

		gl.glEnable(GLLightingFunc.GL_LIGHTING);
		gl.glLightModelfv(GLLightingFunc.GL_AMBIENT, ambient, 0);
		gl.glEnable(GLLightingFunc.GL_LIGHT0);
		((GLLightingFunc) gl).glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_POSITION, position, 0);
		((GLLightingFunc) gl).glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_DIFFUSE, intensity, 0);
		((GLLightingFunc) gl).glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_SPECULAR, specColor, 0);

		gl.glEnable(GLLightingFunc.GL_COLOR_MATERIAL);
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		if (!(flipVerticalInGLOrientation && gl.getContext().getGLDrawable().isGLOriented())) {
			// Only possible if we do not flip the projection matrix
			enableCullFace = true;
		} else {
			enableCullFace = false;
		}
		enableStates(gl, true);

		syncObjects = new Object();

		// TODO why false again
		enableStates(gl, false);

		// hints_renderer = new TextRenderer(new Font("SansSerif", Font.PLAIN,
		// 16));

		isInit = true;
		return true;
	}

	@Override
	protected void displayImpl(final GL2 gl) {
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDisable(GL.GL_CULL_FACE);
		gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
		gl.glCullFace(GL.GL_FRONT);
		gl.glFrontFace(GL.GL_CCW);

		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();

		// this.glcamera.applyProjection(gl,glu);

		// public _StructOrthorgonal()
		// {
		// this.o_size1= -10.0f;
		// this.o_size2= 10.0f;
		// this.o_size3= -10.0f;
		// this.o_size4= 10.0f;
		// this.near_z= 1.0f;
		// this.far_z= 300.0f;
		// this.o_size_limit= 1.0f;
		// }
		// gl.glOrtho(structOrthorgonal.o_size1, structOrthorgonal.o_size2,
		// structOrthorgonal.o_size3, structOrthorgonal.o_size4,
		// structOrthorgonal.near_z, structOrthorgonal.far_z);
		gl.glOrtho(-10.0d, 10.0d, -10.0d, 10.0d, 10.0d, 300.0d);

		gl.glEnable(GLLightingFunc.GL_LIGHTING);
		UnlightedAxis.draw(gl);

		// model.drawByEdges(gl);
		model.drawSmoothShading(gl);

	}

	// TileRendererBase.TileRendererListener
	@Override
	public void reshape(final GLAutoDrawable glad, final int x, final int y, final int width, final int height) {
		if (!isInit) {
			return;
		}
		final GL2 gl = glad.getGL().getGL2();
		gl.setSwapInterval(swapInterval);
		reshape(gl, x, y, width, height, width, height);
	}

	@Override
	public void reshapeTile(final TileRendererBase tr, final int tileX, final int tileY, final int tileWidth,
			final int tileHeight, final int imageWidth, final int imageHeight) {
		if (!isInit) {
			return;
		}
		final GL2 gl = tr.getAttachedDrawable().getGL().getGL2();
		gl.setSwapInterval(0);
		reshape(gl, tileX, tileY, tileWidth, tileHeight, imageWidth, imageHeight);
	}

	@Override
	public void reshape(final GL2 gl, final int tileX, final int tileY, final int tileWidth, final int tileHeight,
			final int imageWidth, final int imageHeight) {
		System.err.println(Thread.currentThread() + " Gears.reshape " + tileX + "/" + tileY + " " + tileWidth + "x"
				+ tileHeight + " of " + imageWidth + "x" + imageHeight + ", swapInterval " + swapInterval
				+ ", drawable 0x" + Long.toHexString(gl.getContext().getGLDrawable().getHandle())
				+ ", tileRendererInUse " + tileRendererInUse);

		// compute projection parameters 'normal'
		float left, right, bottom, top;
		if (imageHeight > imageWidth) {
			final float a = (float) imageHeight / (float) imageWidth;
			left = -1.0f;
			right = 1.0f;
			bottom = -a;
			top = a;
		} else {
			final float a = (float) imageWidth / (float) imageHeight;
			left = -a;
			right = a;
			bottom = -1.0f;
			top = 1.0f;
		}
		final float w = right - left;
		final float h = top - bottom;

		// compute projection parameters 'tiled'
		final float l = left + tileX * w / imageWidth;
		final float r = l + tileWidth * w / imageWidth;

		final float b = bottom + tileY * h / imageHeight;
		final float t = b + tileHeight * h / imageHeight;

		final float _w = r - l;
		final float _h = t - b;
		if (verbose) {
			// TODO what angle
			System.err.println(">> Gears angle " + angle + ", [l " + left + ", r " + right + ", b " + bottom + ", t "
					+ top + "] " + w + "x" + h + " -> [l " + l + ", r " + r + ", b " + b + ", t " + t + "] " + _w + "x"
					+ _h + ", v-flip " + flipVerticalInGLOrientation);
		}

		// GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(tileX, tileY, imageWidth, imageHeight);
		// this.glcamera.setScreenWidthHeight(imageWidth,imageHeight);
		//
		// ((GL2ES1) gl).glOrtho(-orthoganal_clip_size, orthoganal_clip_size,
		// -orthoganal_clip_size2, orthoganal_clip_size2, NEAR_Z, FAR_Z);

		// gl.glScalef(scale, scale, scale);
		// this.glcamera.applyProjection(gl,glu);
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadIdentity();
		// glu.gluLookAt(eyex, eyey, eyez, lookatx, lookaty, lookatz, 0, 1, 0);
		// ????
		// new SimpleGLCamera(new MathPoint3(5.0f, 10f, 20f), new MathPoint3(0f,
		// 0f, 0f), 1);
		// this.glcamera.applyLookat(glu);
		glu.gluLookAt(5.0f, 10f, 20f, 0f, 0f, 0f, 0f, 1f, 0f);

		gl.glEnable(GLLightingFunc.GL_LIGHTING);

		// gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		// gl.glLoadIdentity();
		if (flipVerticalInGLOrientation && gl.getContext().getGLDrawable().isGLOriented()) {
			gl.glScalef(1f, -1f, 1f);
		}
		// gl.glFrustum(l, r, b, t, 5.0f, 60.0f);
		//
		// gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		// gl.glLoadIdentity();
		// gl.glTranslatef(0.0f, 0.0f, -40.0f);
	}

}