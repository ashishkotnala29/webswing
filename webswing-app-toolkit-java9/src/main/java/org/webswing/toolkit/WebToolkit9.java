package org.webswing.toolkit;

import java.applet.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.peer.*;
import java.lang.reflect.Method;

import org.webswing.applet.WebAppletContext;
import org.webswing.dispatch.*;
import org.webswing.toolkit.extra.*;
import org.webswing.toolkit.ge.*;
import org.webswing.toolkit.util.Logger;
import org.webswing.toolkit.util.Util;
import sun.awt.*;
import sun.awt.datatransfer.*;
import sun.awt.image.*;
import sun.java2d.*;

public class WebToolkit9 extends WebToolkit {
	private KeyboardFocusManagerPeer kfmp;

	@Override
	WebFramePeer createWebFramePeer(Frame frame) throws HeadlessException {
		return new WebFramePeer9(frame);
	}

	@Override
	WebDialogPeer createWebDialogPeer(Dialog paramDialog) {
		return new WebDialogPeer9(paramDialog);
	}

	@Override
	WebWindowPeer createWebWindowPeer(Window paramWindow) {
		return new WebWindowPeer9(paramWindow);
	}

	@Override
	WebPanelPeer createWebPanelPeer(Panel panel) {
		return new WebPanelPeer9(panel);
	}

	@Override
	WebFileDialogPeer createWebFileDialogPeer(FileDialog paramFileDialog) {
		return new WebFileDialogPeer9(paramFileDialog);
	}

	@Override
	public void displayChanged() {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				WebGraphicsEnvironment wge = (WebGraphicsEnvironment) ge;
				wge.displayChanged();
			}
		});
	}

	@Override
	public KeyboardFocusManagerPeer getKeyboardFocusManagerPeer() throws HeadlessException {
		if (kfmp == null) {
			kfmp = new WebKeyboardFocusManagerPeer();
		}
		return kfmp;
	}

	@Override
	protected boolean syncNativeQueue(long paramLong) {
		return false;
	}

	@Override
	public boolean webConpoenentPeerUpdateGraphicsData() {
		((WebGraphicsEnvironment) GraphicsEnvironment.getLocalGraphicsEnvironment()).displayChanged();
		return true;
	}

	@Override
	public SurfaceData webComponentPeerReplaceSurfaceData(SurfaceManager mgr) {
		return mgr.getPrimarySurfaceData();
	}

	@Override
	public int shouldNativelyFocusHeavyweight(Window heavyweight, Component descendant, boolean temporary, boolean focusedWindowChangeAllowed, long time, FocusEventCause cause) {
		try {
			Method m2 = KeyboardFocusManager.class.getDeclaredMethod("shouldNativelyFocusHeavyweight", Component.class, Component.class, Boolean.TYPE, Boolean.TYPE, Long.TYPE, FocusEvent.Cause.class);
			m2.setAccessible(true);
			Integer result2 = (Integer) m2.invoke(null, heavyweight, descendant, temporary, focusedWindowChangeAllowed, time, FocusEvent.Cause.valueOf(cause.name()));
			return result2;
		} catch (Exception e) {
			Logger.debug("Failed to invoke processSynchronousLightweightTransfer on KeyboardFocusManager. Check your java version.", e);
			return 0;
		}
	}

	@Override
	public boolean deliverFocus(Component heavyweight, Component descendant, boolean temporary, boolean focusedWindowChangeAllowed, long time, FocusEventCause cause) {
		if (heavyweight == null) {
			heavyweight = descendant;
		}

		Component c = Util.getWebToolkit().getWindowManager().getActiveWindow().getFocusOwner();
		FocusEvent focusEvent;
		if ((c != null) && !c.isDisplayable()) {
			c = null;
		}
		if (c != null) {
			focusEvent = new FocusEvent(c, FocusEvent.FOCUS_LOST, false, heavyweight, FocusEvent.Cause.valueOf(cause.name()));

			SunToolkit.postEvent(SunToolkit.targetToAppContext(c), focusEvent);
		}

		focusEvent = new FocusEvent(heavyweight, FocusEvent.FOCUS_GAINED, false, c, FocusEvent.Cause.valueOf(cause.name()));

		SunToolkit.postEvent(SunToolkit.targetToAppContext(heavyweight), focusEvent);
		return true;
	}

	@Override
	public DataTransferer getDataTransferer() {
		return WebDataTransfer.getInstanceImpl();
	}

	@Override
	public FramePeer createLightweightFrame(LightweightFrame arg0) throws HeadlessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTaskbarSupported() {
		return true;
	}

	@Override
	public Dimension getScreenSize() {
		return new Dimension(screenWidth, screenHeight);
	}

	public static boolean requestFocus(Object target, Component paramComponent, boolean temporary, boolean focusedWindowChangeAllowed, long time, FocusEvent.Cause paramCause) {
		if (target instanceof Window) {
			return Util.getWebToolkit().getWindowManager().activateWindow((Window) target, paramComponent, 0, 0, temporary, focusedWindowChangeAllowed, FocusEventCause.getValue(paramCause));
		} else if (target instanceof Applet) {
			Applet applet = (Applet) target;
			Window window = ((WebAppletContext) applet.getAppletContext()).getContainer();
			return Util.getWebToolkit().getWindowManager().activateWindow(window, paramComponent, 0, 0, temporary, focusedWindowChangeAllowed, FocusEventCause.getValue(paramCause));
		} else {
			return false;
		}
	}
}
