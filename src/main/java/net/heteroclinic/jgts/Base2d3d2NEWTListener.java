package net.heteroclinic.jgts;
import java.awt.Font;
import java.nio.FloatBuffer;
import com.jogamp.newt.Window;
import com.jogamp.newt.event.InputEvent;
import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.event.awt.AWTKeyAdapter;
import com.jogamp.newt.event.awt.AWTMouseAdapter;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.JoglVersion;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.TileRendererBase;
import com.jogamp.opengl.util.awt.TextRenderer;
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
 * JGTS not necessarily a clone of GTS
 * <p>
 * The class {@code Base2d3d2NEWTListener} extends a Listener from JOGL unit tests,
 * so we can customize or override given behaviors. 
 * <p>
 * Modified from:
 * <p> 
 * Gears.java : Brian Paul (converted to Java by Ron Cemer and Sven Gothel)
 * <p>
 * jogl tag 2.3.2
 * <p> 
 * also com.jogamp.opengl.test.junit.jogl.demos.gl2.Gears.java
 * <p> 
 * also com.jogamp.opengl.test.junit.jogl.demos.gl2.newt.TestGearsNEWT.java
 */
//TODO resolve the TODOs
public class Base2d3d2NEWTListener implements GLEventListener, TileRendererBase.TileRendererListener {
	protected float view_rotx = 20.0f, view_roty = 30.0f;
	protected final float view_rotz = 0.0f;
	// protected int gear1 = 0, gear2 = 0, gear3 = 0;
	protected float angle = 0.0f;
	protected final int swapInterval;

	protected int prevMouseX, prevMouseY;

	public Base2d3d2NEWTListener(int swapInterval) {
		this.swapInterval = swapInterval;
	}

	public Base2d3d2NEWTListener() {
		this.swapInterval = 1;
	}

	protected boolean doRotate = true;
	protected TileRendererBase tileRendererInUse = null;
	protected boolean doRotateBeforePrinting;
	protected boolean verbose = false;
	protected boolean flipVerticalInGLOrientation = false;
	protected volatile boolean isInit = false;
	protected Object syncObjects;// TODO sync what, the gears are shared by
									// different threads?

	// TileRendererBase.TileRendererListener
	@Override
	public void addTileRendererNotify(TileRendererBase tr) {
		tileRendererInUse = tr;
		doRotateBeforePrinting = doRotate;
		setDoRotation(false);
	}

	// TileRendererBase.TileRendererListener
	@Override
	public void removeTileRendererNotify(TileRendererBase tr) {
		tileRendererInUse = null;
		setDoRotation(doRotateBeforePrinting);
	}

	// TileRendererBase.TileRendererListener
	@Override
	public void startTileRendering(TileRendererBase tr) {
		System.err.println("XXX.startTileRendering: " + tr);
	}

	// TileRendererBase.TileRendererListener
	@Override
	public void endTileRendering(TileRendererBase tr) {
		System.err.println("XXX.endTileRendering: " + tr);
	}

	// TileRendererBase.TileRendererListener
	public void setDoRotation(final boolean rotate) {
		doRotate = rotate;
	}

	public void setVerbose(final boolean v) {
		verbose = v;
	}

	public void setFlipVerticalInGLOrientation(final boolean v) {
		flipVerticalInGLOrientation = v;
	}

	@Override
	public void init(final GLAutoDrawable drawable) {
		System.err.println("CubeDemo: Init: " + drawable);

		final GL2 gl = drawable.getGL().getGL2();

		if (init(gl)) {
			final Object upstreamWidget = drawable.getUpstreamWidget();
			if (upstreamWidget instanceof Window) {
				final Window window = (Window) upstreamWidget;
				window.addMouseListener(gearsMouse);
				window.addKeyListener(gearsKeys);
			} else if (GLProfile.isAWTAvailable() && upstreamWidget instanceof java.awt.Component) {
				final java.awt.Component comp = (java.awt.Component) upstreamWidget;
				new AWTMouseAdapter(gearsMouse, drawable).addTo(comp);
				new AWTKeyAdapter(gearsKeys, drawable).addTo(comp);
			}
		} else {
			drawable.setGLEventListenerInitState(this, false);
		}
	}

	protected MouseListener gearsMouse;
	protected KeyListener gearsKeys;
	boolean enableCullFace = false;

	protected void enableStates(final GL gl, final boolean enable) {
		final boolean msaa = gl.getContext().getGLDrawable().getChosenGLCapabilities().getSampleBuffers();
		((GLLightingFunc) gl).glShadeModel(GLLightingFunc.GL_SMOOTH);
		if (enable) {
			if (enableCullFace) {
				gl.glEnable(GL.GL_CULL_FACE);
			}
			gl.glEnable(GLLightingFunc.GL_LIGHTING);
			gl.glEnable(GLLightingFunc.GL_LIGHT0);
			gl.glEnable(GL.GL_DEPTH_TEST);
			gl.glDepthFunc(GL.GL_LESS); // default
			gl.glEnable(GLLightingFunc.GL_NORMALIZE);
			((GL2) gl).glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL.GL_TRUE);
			if (msaa) {
				gl.glEnable(GL.GL_MULTISAMPLE);
			}
		} else {
			if (enableCullFace) {
				gl.glDisable(GL.GL_CULL_FACE);
			}
			gl.glDisable(GLLightingFunc.GL_LIGHTING);
			gl.glDisable(GLLightingFunc.GL_LIGHT0);
			gl.glDisable(GL.GL_DEPTH_TEST);
			gl.glDisable(GLLightingFunc.GL_NORMALIZE);
			((GL2) gl).glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL.GL_FALSE);
			if (msaa) {
				gl.glDisable(GL.GL_MULTISAMPLE);
			}
		}
	}

	public boolean init(final GL2 gl) {

		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
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

		hints_renderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 16));

		isInit = true;
		return true;
	}

	public final boolean isInit() {
		return isInit;
	}

	protected TextRenderer hints_renderer;// = new TextRenderer(new
											// Font("SansSerif", Font.PLAIN,
											// 16));

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

		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();
		if (flipVerticalInGLOrientation && gl.getContext().getGLDrawable().isGLOriented()) {
			gl.glScalef(1f, -1f, 1f);
		}
		gl.glFrustum(l, r, b, t, 5.0f, 60.0f);

		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -40.0f);
	}

	// @Override
	// public void reshape(final GLAutoDrawable drawable, final int x, final int
	// y, final int width, final int height) {
	// if (!isInit) {
	// return;
	// }
	//
	// System.err.println("CubeDemo: Reshape " + x + "/" + y + " " + width + "x"
	// + height);
	// GL2 gl = drawable.getGL().getGL2();
	//
	// gl.setSwapInterval(swapInterval);
	//
	// float h = (float) height / (float) width;
	//
	// gl.glMatrixMode(GL2.GL_PROJECTION);
	//
	// gl.glLoadIdentity();
	//
	// // TO-DO
	// // gl.glFrustum(-1.0f, 1.0f, -h, h, 5.0f, 60.0f);
	//
	// gl.glMatrixMode(GL2.GL_MODELVIEW);
	// gl.glLoadIdentity();
	//
	// gl.glTranslatef(0.0f, 0.0f, -40.0f);
	// }

	@Override
	public void dispose(final GLAutoDrawable drawable) {
		if (!isInit) {
			return;
		}
		isInit = false;
		System.err.println(Thread.currentThread() + " Gears.dispose: tileRendererInUse " + tileRendererInUse);
		try {
			final Object upstreamWidget = drawable.getUpstreamWidget();
			if (upstreamWidget instanceof Window) {
				final Window window = (Window) upstreamWidget;
				window.removeMouseListener(gearsMouse);
				window.removeKeyListener(gearsKeys);
			}
		} catch (final Exception e) {
			System.err.println("Caught: ");
			e.printStackTrace();
		}
		// gear1 = 0;
		// gear2 = 0;
		// gear3 = 0;
		// sharedGears = null;
		syncObjects = null;

		this.hints_renderer = null;

	}

	public static final int MATRIX_SIZE = 16;
	// if you put this in display(), the program will crash!
	protected FloatBuffer storage = FloatBuffer.allocate(MATRIX_SIZE);

	protected float[] data = new float[MATRIX_SIZE];

	{
		for (int i = 0; i < MATRIX_SIZE; i++) {
			data[i] = 0.0f;
		}
		data[0] = 1.0f;
		data[5] = 1.0f;
		data[10] = 1.0f;
		data[15] = 1.0f;

		// data[4] = -(float) Math.tan(Math.toRadians(45f));
		// TO-DO 135 or 120 degrees?
		data[9] = -(float) Math.tan(Math.toRadians(60f));
		data[8] = -(float) Math.tan(Math.toRadians(60f));
		/*
		 * The column is the row of OpenGL 4x4 matrix 0 1 2 3 4 5 6 7 8 9 10 11
		 * 12 13 14 15
		 */

		storage.clear();
		storage.put(data);
		storage.clear();
	}

	// // draw the cube
	// protected float[][] vs = new float[][] { new float[] { 0f, -1.63299f,
	// -1.1547f }, new float[] { -1.63299f, 0f, -1.1547f },
	// new float[] { 0f, 1.63299f, -1.1547f }, new float[] { 1.63299f, 0f,
	// -1.1547f },
	// new float[] { 0f, -1.63299f, 1.1547f }, new float[] { -1.63299f, 0f,
	// 1.1547f },
	// new float[] { 0f, 1.63299f, 1.1547f }, new float[] { 1.63299f, 0f,
	// 1.1547f } };

	// TO-DO orthogonal cube mesh
	// draw the cube
	protected float[][] vs = new float[][] { new float[] { -1.0f, -1.0f, -1.0f }, new float[] { -1.0f, 1.0f, -1.0f },
			new float[] { 1.0f, 1.0f, -1.0f }, new float[] { 1.0f, -1.0f, -1.0f }, new float[] { -1.0f, -1.0f, 1.0f },
			new float[] { -1.0f, 1.0f, 1.0f }, new float[] { 1.0f, 1.0f, 1.0f }, new float[] { 1.0f, -1.0f, 1.0f } };

	@Override
	public void display(final GLAutoDrawable drawable) {
		if (!isInit) {
			return;
		}

		// Get the GL corresponding to the drawable we are animating
		final GL2 gl = drawable.getGL().getGL2();

		enableStates(gl, true);

		if (null == tileRendererInUse) {
			// gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		} else {
			gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		}

		// Special handling for the case where the GLJPanel is translucent
		// and wants to be composited with other Java 2D content
		if (GLProfile.isAWTAvailable() && (drawable instanceof com.jogamp.opengl.awt.GLJPanel)
				&& !((com.jogamp.opengl.awt.GLJPanel) drawable).isOpaque()
				&& ((com.jogamp.opengl.awt.GLJPanel) drawable).shouldPreserveColorBufferIfTranslucent()) {
			gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		} else {
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		}
		displayImpl(gl);

		enableStates(gl, false);
	}

	public void display(final GL2 gl) {
		if (!isInit) {
			return;
		}
		enableStates(gl, true);

		if (null == tileRendererInUse) {
			// gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		} else {
			gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		}
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		displayImpl(gl);

		enableStates(gl, false);
	}

	protected void displayImpl(final GL2 gl) {
		if (doRotate) {
			// Turn the gears' teeth
			angle += 0.5f;
		}
		// Rotate the entire assembly of gears based on how the user
		// dragged the mouse around
		gl.glPushMatrix();
		gl.glRotatef(view_rotx, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(view_roty, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(view_rotz, 0.0f, 0.0f, 1.0f);

		{
			final float axisHalfLengh = 3.0f;

			gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
			gl.glLoadIdentity();
			float o_size1 = -3.5f;
			float o_size2 = 3.5f;
			float o_size3 = -3.5f;
			float o_size4 = 3.5f;
			float near_z = 1.0f;
			float far_z = 300.0f;
			gl.glOrtho(o_size1, o_size2, o_size3, o_size4, near_z, far_z);
			gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
			gl.glLoadIdentity();
			GLU glu = new GLU();
			// up vec should be Y, not Z, glu.gluLookAt(5.0f, 8.0f, 10.0f, 0f,
			// 0f, 0f, 0.f, 0.f, 1.f);
			glu.gluLookAt(0.0f, 0.0f, 10.0f, 0f, 0f, 0f, 0.f, 1.f, 0.f);
			// cannot see ? glu.gluLookAt(0f, 0f, 100.0f, 0f, 0f, 0f, 0.f, 0.f,
			// 1.f);

			// TO-DO create orthogonal cube mesh

			gl.glMultMatrixf(storage);

			// TO-DO half z
			gl.glScalef(1.0f, 1.0f, 0.5f);

			gl.glDisable(GLLightingFunc.GL_LIGHTING);
			((GL2) gl).glColor3f(1.0f, 0.0f, 0.0f);
			((GL2) gl).glBegin(GL2.GL_LINES);
			((GL2) gl).glVertex3f(-axisHalfLengh / 2.0f, 0.0f, 0.0f);
			((GL2) gl).glVertex3f(axisHalfLengh, 0.0f, 0.0f);
			((GL2) gl).glEnd();

			((GL2) gl).glColor3f(0.0f, 1.0f, 0.0f);
			((GL2) gl).glBegin(GL2.GL_LINES);
			((GL2) gl).glVertex3f(0.0f, -axisHalfLengh / 2.0f, 0.0f);
			((GL2) gl).glVertex3f(.0f, axisHalfLengh, 0.0f);
			((GL2) gl).glEnd();

			((GL2) gl).glColor3f(0.0f, 0.0f, 1.0f);
			((GL2) gl).glBegin(GL2.GL_LINES);
			((GL2) gl).glVertex3f(0.0f, 0.0f, -axisHalfLengh / 2.0f);
			((GL2) gl).glVertex3f(.0f, .0f, axisHalfLengh);
			((GL2) gl).glEnd();

			// gl.glVertex3fv(float [], int);
			// 3 1 draw edges from vertex 3 to vertex 1
			{
				gl.glPushAttrib(GL2.GL_ENABLE_BIT);
				gl.glLineStipple(4, (short) 0xAAAA);
				gl.glEnable(GL2.GL_LINE_STIPPLE);
				int beginIndex = 3;
				int endIndex = 1;
				((GL2) gl).glColor3f(0.0f, 0.0f, 0.0f);
				((GL2) gl).glBegin(GL2.GL_LINES);
				((GL2) gl).glVertex3fv(vs[beginIndex - 1], 0);
				((GL2) gl).glVertex3fv(vs[endIndex - 1], 0);
				((GL2) gl).glEnd();
				gl.glPopAttrib();
			}

			// 2 1
			{
				gl.glPushAttrib(GL2.GL_ENABLE_BIT);
				gl.glLineStipple(4, (short) 0xAAAA);
				gl.glEnable(GL2.GL_LINE_STIPPLE);
				int beginIndex = 2;
				int endIndex = 1;
				((GL2) gl).glColor3f(0.0f, 0.0f, 0.0f);
				((GL2) gl).glBegin(GL2.GL_LINES);
				((GL2) gl).glVertex3fv(vs[beginIndex - 1], 0);
				((GL2) gl).glVertex3fv(vs[endIndex - 1], 0);
				((GL2) gl).glEnd();
				gl.glPopAttrib();
			}

			// 6 1
			{
				gl.glPushAttrib(GL2.GL_ENABLE_BIT);
				gl.glLineStipple(4, (short) 0xAAAA);
				gl.glEnable(GL2.GL_LINE_STIPPLE);
				int beginIndex = 6;
				int endIndex = 1;
				((GL2) gl).glColor3f(0.0f, 0.0f, 0.0f);
				((GL2) gl).glBegin(GL2.GL_LINES);
				((GL2) gl).glVertex3fv(vs[beginIndex - 1], 0);
				((GL2) gl).glVertex3fv(vs[endIndex - 1], 0);
				((GL2) gl).glEnd();
				gl.glPopAttrib();
			}
			// 2 6
			{
				int beginIndex = 2;
				int endIndex = 6;
				((GL2) gl).glColor3f(0.0f, 0.0f, 0.0f);
				((GL2) gl).glBegin(GL2.GL_LINES);
				((GL2) gl).glVertex3fv(vs[beginIndex - 1], 0);
				((GL2) gl).glVertex3fv(vs[endIndex - 1], 0);
				((GL2) gl).glEnd();
			}
			// 2 7
			{
				int beginIndex = 2;
				int endIndex = 7;
				((GL2) gl).glColor3f(0.0f, 0.0f, 0.0f);
				((GL2) gl).glBegin(GL2.GL_LINES);
				((GL2) gl).glVertex3fv(vs[beginIndex - 1], 0);
				((GL2) gl).glVertex3fv(vs[endIndex - 1], 0);
				((GL2) gl).glEnd();
			}
			// 3 2
			{
				int beginIndex = 3;
				int endIndex = 2;
				((GL2) gl).glColor3f(0.0f, 0.0f, 0.0f);
				((GL2) gl).glBegin(GL2.GL_LINES);
				((GL2) gl).glVertex3fv(vs[beginIndex - 1], 0);
				((GL2) gl).glVertex3fv(vs[endIndex - 1], 0);
				((GL2) gl).glEnd();
			}
			// 3 7
			{
				int beginIndex = 3;
				int endIndex = 7;
				((GL2) gl).glColor3f(0.0f, 0.0f, 0.0f);
				((GL2) gl).glBegin(GL2.GL_LINES);
				((GL2) gl).glVertex3fv(vs[beginIndex - 1], 0);
				((GL2) gl).glVertex3fv(vs[endIndex - 1], 0);
				((GL2) gl).glEnd();
			}
			// 3 8
			{
				int beginIndex = 3;
				int endIndex = 8;
				((GL2) gl).glColor3f(0.0f, 0.0f, 0.0f);
				((GL2) gl).glBegin(GL2.GL_LINES);
				((GL2) gl).glVertex3fv(vs[beginIndex - 1], 0);
				((GL2) gl).glVertex3fv(vs[endIndex - 1], 0);
				((GL2) gl).glEnd();
			}
			// 4 3
			{
				int beginIndex = 4;
				int endIndex = 3;
				((GL2) gl).glColor3f(0.0f, 0.0f, 0.0f);
				((GL2) gl).glBegin(GL2.GL_LINES);
				((GL2) gl).glVertex3fv(vs[beginIndex - 1], 0);
				((GL2) gl).glVertex3fv(vs[endIndex - 1], 0);
				((GL2) gl).glEnd();
			}
			// 4 8
			{
				int beginIndex = 4;
				int endIndex = 8;
				((GL2) gl).glColor3f(0.0f, 0.0f, 0.0f);
				((GL2) gl).glBegin(GL2.GL_LINES);
				((GL2) gl).glVertex3fv(vs[beginIndex - 1], 0);
				((GL2) gl).glVertex3fv(vs[endIndex - 1], 0);
				((GL2) gl).glEnd();
			}
			// 5 1
			{
				gl.glPushAttrib(GL2.GL_ENABLE_BIT);
				gl.glLineStipple(4, (short) 0xAAAA);
				gl.glEnable(GL2.GL_LINE_STIPPLE);
				int beginIndex = 5;
				int endIndex = 1;
				((GL2) gl).glColor3f(0.0f, 0.0f, 0.0f);
				((GL2) gl).glBegin(GL2.GL_LINES);
				((GL2) gl).glVertex3fv(vs[beginIndex - 1], 0);
				((GL2) gl).glVertex3fv(vs[endIndex - 1], 0);
				((GL2) gl).glEnd();
				gl.glPopAttrib();
			}
			// 4 5
			{
				gl.glPushAttrib(GL2.GL_ENABLE_BIT);
				gl.glLineStipple(4, (short) 0xAAAA);
				gl.glEnable(GL2.GL_LINE_STIPPLE);
				int beginIndex = 4;
				int endIndex = 5;
				((GL2) gl).glColor3f(0.0f, 0.0f, 0.0f);
				((GL2) gl).glBegin(GL2.GL_LINES);
				((GL2) gl).glVertex3fv(vs[beginIndex - 1], 0);
				((GL2) gl).glVertex3fv(vs[endIndex - 1], 0);
				((GL2) gl).glEnd();
				gl.glPopAttrib();
			}
			// 1 4
			{
				gl.glPushAttrib(GL2.GL_ENABLE_BIT);
				gl.glLineStipple(4, (short) 0xAAAA);
				gl.glEnable(GL2.GL_LINE_STIPPLE);
				int beginIndex = 1;
				int endIndex = 4;
				((GL2) gl).glColor3f(0.0f, 0.0f, 0.0f);
				((GL2) gl).glBegin(GL2.GL_LINES);
				((GL2) gl).glVertex3fv(vs[beginIndex - 1], 0);
				((GL2) gl).glVertex3fv(vs[endIndex - 1], 0);
				((GL2) gl).glEnd();
				gl.glPopAttrib();
			}
			// 6 5
			{
				int beginIndex = 6;
				int endIndex = 5;
				((GL2) gl).glColor3f(0.0f, 0.0f, 0.0f);
				((GL2) gl).glBegin(GL2.GL_LINES);
				((GL2) gl).glVertex3fv(vs[beginIndex - 1], 0);
				((GL2) gl).glVertex3fv(vs[endIndex - 1], 0);
				((GL2) gl).glEnd();
			}
			// 7 6
			{
				int beginIndex = 7;
				int endIndex = 6;
				((GL2) gl).glColor3f(0.0f, 0.0f, 0.0f);
				((GL2) gl).glBegin(GL2.GL_LINES);
				((GL2) gl).glVertex3fv(vs[beginIndex - 1], 0);
				((GL2) gl).glVertex3fv(vs[endIndex - 1], 0);
				((GL2) gl).glEnd();
			}
			// 5 8
			{
				int beginIndex = 5;
				int endIndex = 8;
				((GL2) gl).glColor3f(0.0f, 0.0f, 0.0f);
				((GL2) gl).glBegin(GL2.GL_LINES);
				((GL2) gl).glVertex3fv(vs[beginIndex - 1], 0);
				((GL2) gl).glVertex3fv(vs[endIndex - 1], 0);
				((GL2) gl).glEnd();
			}
			// 7 5
			{
				int beginIndex = 7;
				int endIndex = 5;
				((GL2) gl).glColor3f(0.0f, 0.0f, 0.0f);
				((GL2) gl).glBegin(GL2.GL_LINES);
				((GL2) gl).glVertex3fv(vs[beginIndex - 1], 0);
				((GL2) gl).glVertex3fv(vs[endIndex - 1], 0);
				((GL2) gl).glEnd();
			}
			// 8 7
			{
				int beginIndex = 8;
				int endIndex = 7;
				((GL2) gl).glColor3f(0.0f, 0.0f, 0.0f);
				((GL2) gl).glBegin(GL2.GL_LINES);
				((GL2) gl).glVertex3fv(vs[beginIndex - 1], 0);
				((GL2) gl).glVertex3fv(vs[endIndex - 1], 0);
				((GL2) gl).glEnd();
			}
			gl.glEnable(GLLightingFunc.GL_LIGHTING);

			//// TO-DO render the text crash the program?
			// TO-DO rotate the text properly

			// May be the reason, new operator in display() create too many
			// objects for the gc to collect.
			// then memory used up. The jvm or system ulimit halts the program.
			{
				gl.glPushMatrix();
				int vertexIndex = 1;
				gl.glTranslatef(vs[vertexIndex - 1][0], vs[vertexIndex - 1][1], vs[vertexIndex - 1][2]);
				// gl.glRotatef(150f, 0.0f, 0.0f, 1.0f);
				// TextRenderer hints_renderer = new TextRenderer(new
				// Font("SansSerif", Font.PLAIN, 16));
				hints_renderer.setColor(0f, 0f, 0f, 1.0f);
				hints_renderer.begin3DRendering();
				float textScaleFactor = 0.012f;
				hints_renderer.draw3D("V" + vertexIndex, 0f, 0f, 0f, textScaleFactor);
				hints_renderer.end3DRendering();
				gl.glPopMatrix();

			}
			{
				gl.glPushMatrix();
				int vertexIndex = 2;
				gl.glTranslatef(vs[vertexIndex - 1][0], vs[vertexIndex - 1][1], vs[vertexIndex - 1][2]);
				// gl.glRotatef(150f, 0.0f, 0.0f, 1.0f);
				// TextRenderer hints_renderer = new TextRenderer(new
				// Font("SansSerif", Font.PLAIN, 16));
				hints_renderer.setColor(0f, 0f, 0f, 1.0f);
				hints_renderer.begin3DRendering();
				float textScaleFactor = 0.012f;
				hints_renderer.draw3D("V" + vertexIndex, 0f, 0f, 0f, textScaleFactor);
				hints_renderer.end3DRendering();
				gl.glPopMatrix();

			}
			{
				gl.glPushMatrix();
				int vertexIndex = 3;
				gl.glTranslatef(vs[vertexIndex - 1][0], vs[vertexIndex - 1][1], vs[vertexIndex - 1][2]);
				// gl.glRotatef(150f, 0.0f, 0.0f, 1.0f);
				// TextRenderer hints_renderer = new TextRenderer(new
				// Font("SansSerif", Font.PLAIN, 16));
				hints_renderer.setColor(0f, 0f, 0f, 1.0f);
				hints_renderer.begin3DRendering();
				float textScaleFactor = 0.012f;
				hints_renderer.draw3D("V" + vertexIndex, 0f, 0f, 0f, textScaleFactor);
				hints_renderer.end3DRendering();
				gl.glPopMatrix();

			}
			{
				gl.glPushMatrix();
				int vertexIndex = 4;
				gl.glTranslatef(vs[vertexIndex - 1][0], vs[vertexIndex - 1][1], vs[vertexIndex - 1][2]);
				// gl.glRotatef(150f, 0.0f, 0.0f, 1.0f);
				// TextRenderer hints_renderer = new TextRenderer(new
				// Font("SansSerif", Font.PLAIN, 16));
				hints_renderer.setColor(0f, 0f, 0f, 1.0f);
				hints_renderer.begin3DRendering();
				float textScaleFactor = 0.012f;
				hints_renderer.draw3D("V" + vertexIndex, 0f, 0f, 0f, textScaleFactor);
				hints_renderer.end3DRendering();
				gl.glPopMatrix();

			}
			{
				gl.glPushMatrix();
				int vertexIndex = 5;
				gl.glTranslatef(vs[vertexIndex - 1][0], vs[vertexIndex - 1][1], vs[vertexIndex - 1][2]);
				// gl.glRotatef(150f, 0.0f, 0.0f, 1.0f);
				// TextRenderer hints_renderer = new TextRenderer(new
				// Font("SansSerif", Font.PLAIN, 16));
				hints_renderer.setColor(0f, 0f, 0f, 1.0f);
				hints_renderer.begin3DRendering();
				float textScaleFactor = 0.012f;
				hints_renderer.draw3D("V" + vertexIndex, 0f, 0f, 0f, textScaleFactor);
				hints_renderer.end3DRendering();
				gl.glPopMatrix();

			}
			{
				gl.glPushMatrix();
				int vertexIndex = 6;
				gl.glTranslatef(vs[vertexIndex - 1][0], vs[vertexIndex - 1][1], vs[vertexIndex - 1][2]);
				// gl.glRotatef(150f, 0.0f, 0.0f, 1.0f);
				// TextRenderer hints_renderer = new TextRenderer(new
				// Font("SansSerif", Font.PLAIN, 16));
				hints_renderer.setColor(0f, 0f, 0f, 1.0f);
				hints_renderer.begin3DRendering();
				float textScaleFactor = 0.012f;
				hints_renderer.draw3D("V" + vertexIndex, 0f, 0f, 0f, textScaleFactor);
				hints_renderer.end3DRendering();
				gl.glPopMatrix();

			}
			{
				gl.glPushMatrix();
				int vertexIndex = 7;
				gl.glTranslatef(vs[vertexIndex - 1][0], vs[vertexIndex - 1][1], vs[vertexIndex - 1][2]);
				// gl.glRotatef(150f, 0.0f, 0.0f, 1.0f);
				// TextRenderer hints_renderer = new TextRenderer(new
				// Font("SansSerif", Font.PLAIN, 16));
				hints_renderer.setColor(0f, 0f, 0f, 1.0f);
				hints_renderer.begin3DRendering();
				float textScaleFactor = 0.012f;
				hints_renderer.draw3D("V" + vertexIndex, 0f, 0f, 0f, textScaleFactor);
				hints_renderer.end3DRendering();
				gl.glPopMatrix();

			}
			{
				gl.glPushMatrix();
				int vertexIndex = 8;
				gl.glTranslatef(vs[vertexIndex - 1][0], vs[vertexIndex - 1][1], vs[vertexIndex - 1][2]);
				// gl.glRotatef(150f, 0.0f, 0.0f, 1.0f);
				// TextRenderer hints_renderer = new TextRenderer(new
				// Font("SansSerif", Font.PLAIN, 16));
				hints_renderer.setColor(0f, 0f, 0f, 1.0f);
				hints_renderer.begin3DRendering();
				float textScaleFactor = 0.012f;
				hints_renderer.draw3D("V" + vertexIndex, 0f, 0f, 0f, textScaleFactor);
				hints_renderer.end3DRendering();
				gl.glPopMatrix();
			}
		}

		// Remember that every push needs a pop; this one is paired with
		// rotating the entire gear assembly
		gl.glPopMatrix();
	}

	class CubeDemoKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			int kc = e.getKeyCode();
			if (KeyEvent.VK_LEFT == kc) {
				view_roty -= 1;
			} else if (KeyEvent.VK_RIGHT == kc) {
				view_roty += 1;
			} else if (KeyEvent.VK_UP == kc) {
				view_rotx -= 1;
			} else if (KeyEvent.VK_DOWN == kc) {
				view_rotx += 1;
			}
		}
	}

	class CubeDemoMouseAdapter extends MouseAdapter {
		public void mousePressed(final MouseEvent e) {
			prevMouseX = e.getX();
			prevMouseY = e.getY();
			if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
				// mouseRButtonDown = true;
			}
		}

		public void mouseReleased(final MouseEvent e) {
			if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
				// mouseRButtonDown = false;
			}
		}

		public void mouseDragged(final MouseEvent e) {
			final int x = e.getX();
			final int y = e.getY();
			int width = 0, height = 0;
			final Object source = e.getSource();
			if (source instanceof Window) {
				final Window window = (Window) source;
				width = window.getSurfaceWidth();
				height = window.getSurfaceHeight();
			} else if (source instanceof GLAutoDrawable) {
				final GLAutoDrawable glad = (GLAutoDrawable) source;
				width = glad.getSurfaceWidth();
				height = glad.getSurfaceHeight();
			} else if (GLProfile.isAWTAvailable() && source instanceof java.awt.Component) {
				final java.awt.Component comp = (java.awt.Component) source;
				width = comp.getWidth(); // FIXME HiDPI: May need to convert
											// window units -> pixel units!
				height = comp.getHeight();
			} else {
				throw new RuntimeException("Event source neither Window nor Component: " + source);
			}
			final float thetaY = 360.0f * ((float) (x - prevMouseX) / (float) width);
			final float thetaX = 360.0f * ((float) (prevMouseY - y) / (float) height);

			prevMouseX = x;
			prevMouseY = y;

			view_rotx += thetaX;
			view_roty += thetaY;
		}
	}

}
