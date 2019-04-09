package org.webswing.demo.window;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

import com.sun.swingset3.DemoProperties;

@DemoProperties(value = "Windows", category = "Webswing", description = "Demonstrates handling of various window decoration options", sourceFiles = { "org/webswing/demo/window/WindowDecorationDemo.java" })
public class WindowDecorationDemo extends JPanel {

	static int count = 0;

	Point pos = new Point();
	int id = count++;
	JCheckBox parentCB = new JCheckBox("Parent", true);
	JCheckBox undecoCB = new JCheckBox("Undecorated");
	JCheckBox ontopCB = new JCheckBox("Always on top");
	JCheckBox modalCB = new JCheckBox("Modal");
	Window thisWindow;
	private Point initialClick;
	private JButton mPopup;

	public WindowDecorationDemo() {
		this(null, null, null, false, false);
	}

	public WindowDecorationDemo(Window owner, Window parent, Point pos, boolean undecorated, boolean ontop) {
		thisWindow = owner;
		JPanel info = new JPanel(new FlowLayout());
		info.add(new JLabel("Id:" + id));
		info.add(new JLabel("Parent:" + getID(parent)));
		info.add(new JLabel("Modal:" + (owner instanceof JDialog ? ((JDialog) owner).isModal() : "false")));
		info.add(new JLabel("Always on top:" + (owner == null ? "N" : owner.isAlwaysOnTop())));
		final JTextField fld_1 = new JTextField();
		JTextField fld_2 = new JTextField();
		fld_1.setColumns(5);
		fld_2.setColumns(5);
		fld_1.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent jc) {
				return ((JTextComponent) jc).getText().startsWith("S");
			}

			@Override
			public boolean shouldYieldFocus(JComponent jc) {
				if (verify(jc))
					return true;
				boolean inputOK = showLov();
				if (inputOK) {
					return true;
				}
				javax.swing.JTextField source = (javax.swing.JTextField) jc;
				source.selectAll();
				java.awt.Toolkit.getDefaultToolkit().beep();
				return false;
			}

			private boolean showLov() {
				String ok = "set def value and go to next field";
				String stop = "stop navigation";
				List<Object> options = new ArrayList<Object>();
				Object defaultOption;
				options.add(ok);
				options.add(stop);
				defaultOption = ok;
				int answer = JOptionPane.showOptionDialog(null, "msg", "title", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options.toArray(), defaultOption);
				if (answer < 0)
					return false;
				if (options.get(answer).equals(ok)) {
					fld_1.setText("S-OK");
					return true;
				}
				return false;
			}
		});

		info.add(fld_1);
		info.add(fld_2);
		if (owner == null) {
			parentCB.setSelected(false);
		}
		if (undecorated) {
			undecoCB.setSelected(true);
		}
		ontopCB.setSelected(ontop);
		if (pos != null) {
			this.pos = new Point(pos.x + 10, pos.y + 10);
		}
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		JPanel checkboxes = new JPanel(new FlowLayout());
		checkboxes.add(parentCB);
		checkboxes.add(undecoCB);
		checkboxes.add(ontopCB);
		checkboxes.add(modalCB);
		JPanel buttonsPanel = new JPanel(new FlowLayout());
		addPopupButton(buttonsPanel);
		JButton frame = new JButton("new Frame");
		frame.setToolTipText("This is a veeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeery long tooltip text");
		frame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame(WindowDecorationDemo.this.pos, undecoCB.isSelected(), ontopCB.isSelected());
				WindowDecorationDemo.this.pos.y += 10;
			}
		});
		buttonsPanel.add(frame);
		JButton dialog = new JButton("new Dialog");
		dialog.setToolTipText("This is a veeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeery long tooltip text");
		dialog.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog(parentCB.isSelected() ? thisWindow : null, WindowDecorationDemo.this.pos, WindowDecorationDemo.this.undecoCB.isSelected(), ontopCB.isSelected(), modalCB.isSelected());
				WindowDecorationDemo.this.pos.y += 10;
			}
		});
		buttonsPanel.add(dialog);
		JButton window = new JButton("new Window");
		window.setToolTipText("This is a veeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeery long tooltip text");
		window.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				window(parentCB.isSelected() ? thisWindow : null, WindowDecorationDemo.this.pos, ontopCB.isSelected());
				WindowDecorationDemo.this.pos.y += 10;
			}
		});
		buttonsPanel.add(window);
		JPanel additional = new JPanel(new FlowLayout());
		//********************LABELS**************//
		//context menu
		JLabel context = new JLabel("Right click for context menu");
		context.setBorder(BorderFactory.createLineBorder(Color.black));
		JPopupMenu menu = new JPopupMenu("test");
		JMenuItem win00 = menu.add(new JMenuItem("create window with size (0,0)"));
		win00.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JWindow frame = new JWindow();
				frame.add(new JLabel("test"));
				frame.pack();
				frame.setVisible(true);
				frame.setBounds(0, 0, 0, 0);
			}
		});
		JMenuItem translucentWin = new JMenuItem("translucent window");
		translucentWin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TranslucentWindowDemo tw = new TranslucentWindowDemo();
				tw.setVisible(true);
			}
		});
		menu.add(translucentWin);
		JMenuItem pixelTranslucentWin = new JMenuItem("pixel translucent window");
		pixelTranslucentWin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GradientTranslucentWindowDemo gtw = new GradientTranslucentWindowDemo();
				// Display the window.
				gtw.setVisible(true);

			}
		});
		menu.add(pixelTranslucentWin);

		JMenuItem shapeTranslucentWin = new JMenuItem("shaped window");
		shapeTranslucentWin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShapedWindowDemo sw = new ShapedWindowDemo();
				// Display the window.
				sw.setVisible(true);

			}
		});
		menu.add(shapeTranslucentWin);
		menu.add(new JMenuItem("test"));
		menu.add(new JMenuItem("test"));
		menu.add(new JMenuItem("test"));
		menu.add(new JMenuItem("test"));
		JMenuItem blockItem = new JMenuItem("block EDT for 15s");
		blockItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(15000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				});
			}
		});
		menu.add(blockItem);
		context.setComponentPopupMenu(menu);

		//move window
		JLabel moveButton = new JLabel("Move window by dragging this area");
		moveButton.setBorder(BorderFactory.createLineBorder(Color.black));
		moveButton.addMouseMotionListener(new MouseAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				Window parent = thisWindow;
				// get location of Window
				int thisX = parent.getLocation().x;
				int thisY = parent.getLocation().y;
				// Determine how much the mouse moved since the initial click
				int xMoved = (thisX + e.getX()) - (thisX + initialClick.x);
				int yMoved = (thisY + e.getY()) - (thisY + initialClick.y);

				// Move window to this position
				int X = thisX + xMoved;
				int Y = thisY + yMoved;
				parent.setLocation(X, Y);
			}
		});
		moveButton.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				initialClick = e.getPoint();
				getComponentAt(initialClick);
			}
		});
		JLabel maximize = new JLabel("maximize");
		maximize.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				((JFrame) thisWindow).setExtendedState(JFrame.MAXIMIZED_BOTH);
			}
		});
		JLabel close = new JLabel("close");
		close.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				thisWindow.dispose();
			}
		});
		if (thisWindow != null) {
			content.add(info);
		}
		content.add(checkboxes);
		buttonsPanel.add(context);
		content.add(buttonsPanel);
		if (thisWindow != null) {
			additional.add(moveButton);
			if (thisWindow instanceof JFrame) {
				additional.add(maximize);
			}
			additional.add(close);
			content.add(additional);
		}
		add(content);
		setBorder(new LineBorder(Color.BLACK));
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), "testX");
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), "testC");
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), "testV");
		this.getActionMap().put("testX", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Pressed CTRL+SHIFT+X");

			}
		});
		this.getActionMap().put("testC", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Pressed CTRL+SHIFT+C");

			}
		});
		this.getActionMap().put("testV", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Pressed CTRL+SHIFT+V");

			}
		});
	}

	private static String getID(Window parent) {
		if (parent == null) {
			return "N";
		} else {
			if (parent instanceof JFrame) {
				return ((WindowDecorationDemo) ((JFrame) parent).getContentPane().getComponent(0)).id + "";
			}

			if (parent instanceof JDialog) {
				return ((WindowDecorationDemo) ((JDialog) parent).getContentPane().getComponent(0)).id + "";
			}

			if (parent instanceof JWindow) {
				return ((WindowDecorationDemo) ((JWindow) parent).getContentPane().getComponent(0)).id + "";
			}

		}
		return null;
	}

	public static JFrame frame(Point position, boolean undecorated, boolean ontop) {
		JFrame frame = new JFrame("Demo") {
			@Override
			public String toString() {
				return "Me:" + getID(this) + " Par:" + getID((Window) this.getParent());
			}
		};
		frame.setLocation(position);
		frame.setAlwaysOnTop(ontop);
		frame.setUndecorated(undecorated);
		WindowDecorationDemo panel = new WindowDecorationDemo(frame, null, position, undecorated, ontop);
		frame.getContentPane().add(panel);

		frame.pack();
		frame.setVisible(true);
		return frame;
	}

	public static void dialog(Window owner, Point position, boolean undecorated, boolean ontop, boolean modal) {
		JDialog frame = new JDialog(owner) {
			@Override
			public String toString() {
				return "Me:" + getID(this) + " Par:" + getID((Window) this.getParent());
			}
		};
		frame.setModal(modal);
		frame.setAlwaysOnTop(ontop);
		frame.setUndecorated(undecorated);
		WindowDecorationDemo panel = new WindowDecorationDemo(frame, owner, position, undecorated, ontop);
		frame.getContentPane().add(panel);
		frame.setLocation(position);

		frame.pack();
		frame.setVisible(true);
	}

	public void addPopupButton(JPanel jpanel) {
		this.mPopup = new JButton("popup");
		this.mPopup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WindowDecorationDemo.this.doPopupTest();
			}
		});
		jpanel.add(this.mPopup);
	}

	private void doPopupTest() {
		PopupFactory popupFactory = PopupFactory.getSharedInstance();
		Point buttonLocation = this.mPopup.getLocationOnScreen();
		JPanel jp = new JPanel(new BorderLayout(5, 5));
		jp.add(new JTextField(10), "North");
		JButton x = new JButton("X");
		jp.add(x);
		final Popup popup = popupFactory.getPopup(this, jp, buttonLocation.x, buttonLocation.y);
		x.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				popup.hide();
			}
		});
		popup.show();
	}

	public static void window(Window owner, Point position, boolean ontop) {
		JWindow frame = new JWindow(owner) {
			@Override
			public String toString() {
				return "Me:" + getID(this) + " Par:" + getID((Window) this.getParent());
			}
		};
		frame.setLocation(position);
		frame.setAlwaysOnTop(ontop);
		WindowDecorationDemo panel = new WindowDecorationDemo(frame, owner, position, false, ontop);
		frame.getContentPane().add(panel);

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		JFrame f = frame(new Point(0, 0), false, false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static class TranslucentWindowDemo extends JFrame {
		public TranslucentWindowDemo() {
			super("TranslucentWindow");
			setLayout(new GridBagLayout());

			setSize(300, 200);
			setLocationRelativeTo(null);
			setUndecorated(true);
			setOpacity(0.55f);
			//Add a sample button.
			JButton close = new JButton("Close");
			close.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					TranslucentWindowDemo.this.dispose();
				}
			});
			add(close);

		}
	}

	public static class GradientTranslucentWindowDemo extends JFrame {
		public GradientTranslucentWindowDemo() {
			super("GradientTranslucentWindow");
			setUndecorated(true);

			setBackground(new Color(0, 0, 0, 0));
			setSize(new Dimension(300, 200));
			setLocationRelativeTo(null);

			JPanel panel = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					if (g instanceof Graphics2D) {
						final int R = 240;
						final int G = 240;
						final int B = 240;

						Paint p = new GradientPaint(0.0f, 0.0f, new Color(R, G, B, 0), 0.0f, getHeight(), new Color(R, G, B, 255), true);
						Graphics2D g2d = (Graphics2D) g;
						g2d.setPaint(p);
						g2d.fillRect(0, 0, getWidth(), getHeight());
					}
				}
			};
			setContentPane(panel);
			setLayout(new GridBagLayout());
			JButton close = new JButton("Close");
			close.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					GradientTranslucentWindowDemo.this.dispose();
				}
			});
			add(close);
		}
	}

	public class ShapedWindowDemo extends JFrame {
		public ShapedWindowDemo() {
			super("ShapedWindow");
			setLayout(new GridBagLayout());

			// It is best practice to set the window's shape in
			// the componentResized method.  Then, if the window
			// changes size, the shape will be correctly recalculated.
			addComponentListener(new ComponentAdapter() {
				// Give the window an elliptical shape.
				// If the window is resized, the shape is recalculated here.
				@Override
				public void componentResized(ComponentEvent e) {
					setShape(new Ellipse2D.Double(0,0,getWidth(),getHeight()));
				}
			});

			setUndecorated(true);
			setSize(300,200);
			setLocationRelativeTo(null);

			JButton close = new JButton("Close");
			close.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ShapedWindowDemo.this.dispose();
				}
			});
			add(close);		}
	}
}
