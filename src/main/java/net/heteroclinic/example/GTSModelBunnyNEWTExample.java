package net.heteroclinic.example;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.jgtsx.ShutdownQuitAdapter;
import com.jogamp.opengl.util.Animator;
import net.heteroclinic.jgts.AxonometricProjectionCubeDemo;
import net.heteroclinic.jgts.GTSModelViewerListener;
import net.heteroclinic.jgts.TestGTSModelBunnyNEWT;
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
 * JGTS not necessarily a clone of GTS
 * <p>
 * The {@code GTSModelBunnyNEWTExample} demonstrates the basic UI rendering derived from JOGL unit test.
 * <p>
 * It shows how a GTS model is rendered by JOGL. We shade the GTS mesh bunny.gts in smooth mode.
 * <p>
 * Beyond the UI technique, a model should have information of the following three, geometry (vertices),
 * topology (edges), completeness and orientation (triangles).
 * @author      Zhikai Wang &lt;www.heteroclinic.net&gt;
 * @version     1.0
 * @since       1.0
 */
public class GTSModelBunnyNEWTExample {
	/**
	 * @param args
	 * @throws MalformedURLException
	 * 		can not find file, we don't handle this, abort.
	 */

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws MalformedURLException {
		DrawableModel model = new DrawableModel();
		
		//Specify file to load
		final String fileNameInResourceFolder = "bunny.gts";
		String absolutePath = "/somepath/workspace/jgts-dev/src/main/resources/";
		
		//OR
		//final String fileNameInResourceFolder = "head.gts";
		//String absolutePath = "/home/zhikai/Downloads/";
				
		model.initFromFile(new File(absolutePath  +fileNameInResourceFolder ).toURL());
		
		model.OBJ_SIZE = 10f;

		// model.print();
		// model.printBounding();

		model.calculateScale();

		model.offSet();

		model.calculateTrianglesNormals();

		
		
		final String profile = GLProfile.GL2;
		if (!GLProfile.isAvailable(GLProfile.GL2)) {
			System.err.println("Profile " + GLProfile.GL2 + " n/a");
			return;
		}
		GLCapabilities caps = new GLCapabilities(GLProfile.get(profile));
		

		final GLWindow glWindow = GLWindow.create(caps);
		// Assert.assertNotNull(glWindow);
		glWindow.setSize(800, 600);
		glWindow.setVisible(true);
		glWindow.setTitle("GTS Model Bunny NEWT Example");
		// Assert.assertTrue(glWindow.isNativeValid());
		//AxonometricProjectionCubeDemo axonometricProjectionCubeDemo = new AxonometricProjectionCubeDemo();
		GTSModelViewerListener bunnyGTSModelViewerListener = new GTSModelViewerListener(model);

		final ShutdownQuitAdapter quitAdapter = new ShutdownQuitAdapter();
		glWindow.addKeyListener(quitAdapter);
		glWindow.addWindowListener(quitAdapter);
		glWindow.addGLEventListener(bunnyGTSModelViewerListener);

		final Animator animator = new Animator(glWindow);
		quitAdapter.setAnimator(animator);
		animator.start();

		animator.setUpdateFPSFrames(60, System.err);
		
		// Wait for you to close [X]
		// snapshotGLEventListener.setMakeSnapshot();

		// while (!quitAdapter.shouldQuit() && animator.isAnimating() &&
		// animator.getTotalFPSDuration() < duration) {
		// Thread.sleep(100);
		// }

		// animator.stop();
		// glWindow.destroy();
	}

}
