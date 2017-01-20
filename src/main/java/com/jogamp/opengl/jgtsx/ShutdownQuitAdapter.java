package com.jogamp.opengl.jgtsx;

import com.jogamp.newt.event.WindowEvent;
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
 * The {@code ShutdownQuitAdapter} class is used for closing a UI window by user interaction.
 * E.g. click the [X].
 * <p>
 * Tech/implementation discussion Zhikai TBD TODO
 * <p>
 * Not sure how comes this QuitAdapter. According to the approach in Thinking in Java 4th 
 * edition and my own experience programming with Java synchronization, the threads may be 
 * good to attach to a Future each, run from Executor, use a list to store all the futures.
 * While shutting down will be handled by a Shutdown-hook, similar to VC++ CloseHandle, 
 * probably an equivalence of _exit() of a process. In the Shutdown-hook, cancel all the 
 * futures, better to have a 'halted' boolean signaling the end of life a thread, wait for 
 * the end of life booleans. Shutdown the Executor finally.
 * @author      Zhikai Wang &lt;www.heteroclinic.net&gt;
 */

public class ShutdownQuitAdapter extends QuitAdapter {
	static final long duration = 500;
	private Animator animator;

	public void setAnimator(Animator animator) {
		this.animator = animator;
	}

	public void windowDestroyNotify(final WindowEvent e) {
		if (enabled) {
			System.err.println("QUIT Window " + Thread.currentThread());
			shouldQuit = true;
		}
		if (null != animator) {
			while (!shouldQuit() && animator.isAnimating() && animator.getTotalFPSDuration() < duration) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

			while (!animator.stop()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					// animator.stop();
				}
			}
		}
		// animator.stop();
	}
}