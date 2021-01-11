package lwjgl3;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class Input {
	private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
	private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
	private static double mouseX, mouseY;
	private static double scrollX, scrollY;
	private static double lastMouseX, lastMouseY;
	private static double lastscrollX, lastscrollY;
	
	private static GLFWKeyCallback keyboard;
	private static GLFWCursorPosCallback mouseMove;
	private static GLFWMouseButtonCallback mouseButtons;
	private static GLFWScrollCallback mouseScroll;
	
	public static void Initalize()
	{
		keyboard = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action, int mods) {
				keys[key] = (action != GLFW.GLFW_RELEASE);
			}
		};
		
		mouseMove = new GLFWCursorPosCallback() {
			public void invoke(long window, double xpos, double ypos) {
				lastMouseX = mouseX;
				lastMouseY = mouseY;
				mouseX = xpos;
				mouseY = ypos;
			}
		};
		
		mouseButtons = new GLFWMouseButtonCallback() {
			public void invoke(long window, int button, int action, int mods) { 
				buttons[button] = (action != GLFW.GLFW_RELEASE);
			}
		};

		mouseScroll = new GLFWScrollCallback() {
			public void invoke(long window, double offsetx, double offsety) {
				lastscrollX = scrollX; lastscrollY = scrollY;
				scrollX += offsetx;
				scrollY += offsety;
			}
		};
	}

	public static boolean isKeyDown(int key) {
		return keys[key];
	}
	
	public static boolean isButtonDown(int button) {
		return buttons[button];
	}
	
	public void destroy() {
		keyboard.free();
		mouseMove.free();
		mouseButtons.free();
		mouseScroll.free();
	}

	public static double getMouseX() {
		return mouseX;
	}

	public static double getMouseY() {
		return mouseY;
	}
	
	public static double getMouseDX() {
		return mouseX-lastMouseX ;
	}

	public static double getMouseDY() {
		return mouseY-lastMouseY ;
	}
	
	public static double getScrollX() {
		return scrollX;
	}

	public static double getScrollY() {
		return scrollY;
	}

	public static double getScrollDX() {
		return lastscrollX - scrollX;
	}

	public static double getScrollDY() {
		return lastscrollY - scrollY;
	}
	
	public static GLFWKeyCallback getKeyboardCallback() {
		return keyboard;
	}

	public static GLFWCursorPosCallback getMouseMoveCallback() {
		return mouseMove;
	}

	public static GLFWMouseButtonCallback getMouseButtonsCallback() {
		return mouseButtons;
	}
	
	public static GLFWScrollCallback getMouseScrollCallback() {
		return mouseScroll;
	}
}
