package net.heteroclinic.jgts;

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
 * {@code TestBasicRenderAxonometricProjectionCubeNEWT} tests the basic rendering UI derived from JOGL unit test.
 * <p>
 * It demonstrates the desired properties related to a model.
 * A model should have information of the following three, geometry (vertices),
 * topology (edges), completeness and orientation (triangles).
 * In a shaded model, we can not demonstrate all the above information.
 * The model cube.gts is rendered in axonometric projection. The vertices are
 * labeled to show the properties of the model.
 * @author      Zhikai Wang &lt;www.heteroclinic.net&gt;
 * @version     1.0
 * @since       1.0
 */
// TODO extends UITestCase, this util is not available from Maven repos, mostly
// it deals with X11 stuff, we can
// include joal, jogl source code projects/jars explicitly in class path, but it
// will affect Maven's 1 file project dependency-manage-ability.
// TO!DO tweak without this, refer to TestTessellationShader01GL4NEWT's approach
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBasicRenderAxonometricProjectionCubeNEWT {
	static int width, height;
	static long duration = 500; // ms

	static GLCapabilities getCaps(final String profile) {
		if (!GLProfile.isAvailable(profile)) {
			System.err.println("Profile " + profile + " n/a");
			return null;
		}
		return new GLCapabilities(GLProfile.get(profile));
	}

	@BeforeClass
	public static void initClass() {
		width = 640;
		height = 480;
	}

	@AfterClass
	public static void releaseClass() {
	}

	@Test
	public void test01_01a() throws InterruptedException {
		final GLCapabilities caps = getCaps(GLProfile.GL2);
		if (null == caps) {
			return;
		}
		AxonometricProjectionCubeDemo axonometricProjectionCubeDemo = new AxonometricProjectionCubeDemo();
		testImpl(caps, axonometricProjectionCubeDemo);
	}

	private void testImpl(final GLCapabilities caps, final GLEventListener glel) throws InterruptedException {
		final GLWindow glWindow = GLWindow.create(caps);
		Assert.assertNotNull(glWindow);
		glWindow.setSize(800, 600);
		glWindow.setVisible(true);
		glWindow.setTitle("Basic Render/TEST Axonometric Projection Cube NEWTExample");
		Assert.assertTrue(glWindow.isNativeValid());

		final QuitAdapter quitAdapter = new QuitAdapter();
		glWindow.addKeyListener(quitAdapter);
		glWindow.addWindowListener(quitAdapter);
		glWindow.addGLEventListener(glel);

		// final SnapshotGLEventListener snapshotGLEventListener = new
		// SnapshotGLEventListener();
//		AxonometricProjectionCubeDemo axonometricProjectionCubeDemo = new AxonometricProjectionCubeDemo();
//		glWindow.addGLEventListener(axonometricProjectionCubeDemo);

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
		org.junit.runner.JUnitCore.main(TestBasicRenderAxonometricProjectionCubeNEWT.class.getName());
	}
}
