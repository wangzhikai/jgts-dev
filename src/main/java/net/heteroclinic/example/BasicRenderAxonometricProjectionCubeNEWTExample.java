package net.heteroclinic.example;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.jgtsx.ShutdownQuitAdapter;
import com.jogamp.opengl.util.Animator;
import net.heteroclinic.jgts.AxonometricProjectionCubeDemo;
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
 * The {@code BasicRenderAxonometricProjectionCubeNEWTExample} demonstates the geometry of model cube.gts.
 * <p>
 * A model should have information of the following three, geometry (vertices),
 * topology (edges), completeness and orientation (triangles).
 * In a shaded model, we can not demonstrate all the above information as we want.
 * @author      Zhikai Wang &lt;www.heteroclinic.net&gt;
 * @version     1.0
 * @since       1.0
 */
public class BasicRenderAxonometricProjectionCubeNEWTExample {

	public static void main(String[] args) {
		final String profile = GLProfile.GL2;
		if (!GLProfile.isAvailable(GLProfile.GL2)) {
			System.err.println("Profile " + GLProfile.GL2 + " n/a");
			return;
		}
		GLCapabilities caps = new GLCapabilities(GLProfile.get(profile));
		// if (null == caps) {
		// System.err.println("Profile " + profile + " n/a");
		// return;
		// }

		final GLWindow glWindow = GLWindow.create(caps);
		// Assert.assertNotNull(glWindow);
		glWindow.setSize(800, 600);
		glWindow.setVisible(true);
		glWindow.setTitle("Basic Render Axonometric Projection Cube NEWTExample");
		// Assert.assertTrue(glWindow.isNativeValid());
		AxonometricProjectionCubeDemo axonometricProjectionCubeDemo = new AxonometricProjectionCubeDemo();

		final ShutdownQuitAdapter quitAdapter = new ShutdownQuitAdapter();
		glWindow.addKeyListener(quitAdapter);
		glWindow.addWindowListener(quitAdapter);
		glWindow.addGLEventListener(axonometricProjectionCubeDemo);

		final Animator animator = new Animator(glWindow);
		quitAdapter.setAnimator(animator);
		animator.start();

		animator.setUpdateFPSFrames(60, System.err);
		// snapshotGLEventListener.setMakeSnapshot();

		// while (!quitAdapter.shouldQuit() && animator.isAnimating() &&
		// animator.getTotalFPSDuration() < duration) {
		// Thread.sleep(100);
		// }

		// animator.stop();
		// glWindow.destroy();
	}

}
