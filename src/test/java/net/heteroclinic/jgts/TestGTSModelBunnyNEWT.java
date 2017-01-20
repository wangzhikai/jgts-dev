package net.heteroclinic.jgts;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.jgtsx.QuitAdapter;
import com.jogamp.opengl.util.Animator;

import net.heteroclinic.jgts.model.joglext.DrawableModel;

/**
 * Copyright 2010 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY JogAmp Community ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL JogAmp Community OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of JogAmp Community.
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
 * {@code TestGTSModelBunnyNEWT} tests the basic UI rendering derived from JOGL unit test.
 * <p>
 * It shows how a GTS model is rendered by JOGL. We shade the GTS mesh bunny.gts in smooth mode.
 * @author      Zhikai Wang &lt;www.heteroclinic.net&gt;
 * @version     1.0
 * @since       1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGTSModelBunnyNEWT {
	static int width, height;
	static long duration = 500; // ms

	static GLCapabilities getCaps(final String profile) {
		if (!GLProfile.isAvailable(profile)) {
			System.err.println("Profile " + profile + " n/a");
			return null;
		}
		return new GLCapabilities(GLProfile.get(profile));
	}

	static protected final String fn = "bunny.gts";
	static protected DrawableModel model;

	@BeforeClass
	public static void initClass() {
		width = 640;
		height = 640;
	}
	
	@Test
	public void test01InitializeModel() throws InterruptedException {
		
		model = new DrawableModel();

		model.initFromFile(TestGTSModelBunnyNEWT.class.getClassLoader(), fn);
		
//		this.vertices = new Vertex[this.verticesCount] ;
//		this.edges = new GTSEdge[this.edgesCount] ;
//		this.triangles = new GTSTriangle[this.trianglesCount];
		
		//34834 104288 69451
		assertTrue("Bunny model.getVerticesCount()!=34834",model.getVerticesCount()==34834);
		assertTrue("Bunny model.getEdgesCount()!=104288",model.getEdgesCount()==104288);
		assertTrue("Bunny model.getTrianglesCount()!=69451",model.getTrianglesCount()==69451);
		
		model.OBJ_SIZE = 10f;

		// model.print();
		// model.printBounding();

		model.calculateScale();

		model.offSet();

		model.calculateTrianglesNormals();

		assertTrue("Bunny model.trianlgesNomals.length!=69451",model.trianlgesNomals.length==69451);
		assertTrue("Bunny model.verticesNomals.length!=34834",model.verticesNomals.length==34834);
	
	}


	@Test
	public void test02_01a() throws InterruptedException {
		final GLCapabilities caps = getCaps(GLProfile.GL2);
		if (null == caps) {
			return;
		}

		GTSModelViewerListener bunnyGTSModelViewerListener = new GTSModelViewerListener(model);

		// bunnyGTSModelViewerListener.setModel(model);

		testImpl(caps, bunnyGTSModelViewerListener);
	}

	private void testImpl(final GLCapabilities caps, final GLEventListener renderingListener)
			throws InterruptedException {
		final GLWindow glWindow = GLWindow.create(caps);
		Assert.assertNotNull(glWindow);
		glWindow.setSize(800, 600);
		glWindow.setVisible(true);
		glWindow.setTitle("Basic Render/TEST Axonometric Projection Cube NEWTExample");
		Assert.assertTrue(glWindow.isNativeValid());

		final QuitAdapter quitAdapter = new QuitAdapter();
		glWindow.addKeyListener(quitAdapter);
		glWindow.addWindowListener(quitAdapter);
		glWindow.addGLEventListener(renderingListener);

		// final SnapshotGLEventListener snapshotGLEventListener = new
		// SnapshotGLEventListener();
		// AxonometricProjectionCubeDemo axonometricProjectionCubeDemo = new
		// AxonometricProjectionCubeDemo();
		// glWindow.addGLEventListener(axonometricProjectionCubeDemo);

		final Animator animator = new Animator(glWindow);
		animator.start();

		animator.setUpdateFPSFrames(60, System.err);
		// snapshotGLEventListener.setMakeSnapshot();

		while (!quitAdapter.shouldQuit() && animator.isAnimating() && animator.getTotalFPSDuration() < duration) {
			Thread.sleep(100);
		}

		animator.stop();
		glWindow.destroy();
	}

	@AfterClass
	public static void releaseClass() {
	}

	public static void main(final String args[]) throws IOException {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-time")) {
				i++;
				// duration = MiscUtils.atol(args[i], duration);
				try {
					duration = Integer.parseInt(args[i]);
				} catch (final Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		org.junit.runner.JUnitCore.main(TestGTSModelBunnyNEWT.class.getName());
	}
}
