package com.tayek.utilities;
import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;
public class MyTopJPanel extends JPanel {
	public MyTopJPanel() {
		this(null);
	}
	public MyTopJPanel(MyJApplet applet) {
		this(applet,true);
	}
	public MyTopJPanel(MyJApplet applet,boolean wait) {
		this.applet=applet;
		if(!isApplet()) {
			frame=new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} else frame=null;
		setLayout(new BorderLayout()); // maybe get rid of this?
		// setPreferredSize(new Dimension(640, 480));
		if(wait) try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					MyTopJPanel.this.run();
				}
			});
		} catch(InvocationTargetException|InterruptedException e) {
			throw new RuntimeException(e);
		}
		else SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MyTopJPanel.this.run();
			}
		});
	}
	public String title() {
		return "title";
	}
	public void addContent() {
		add(new JLabel("add content! top"));
	}
	void run() {
		if(isApplet()) addContent();
		else {
			System.out.println(getLayout());
			System.out.println("run/set title");
			frame.setTitle(title());
			frame.getContentPane().add(this,BorderLayout.CENTER);
			System.out.println("before add content");
			addContent();
			System.out.println("after add content");
			frame.pack();
			System.out.println(getSize());
			frame.setVisible(true);
		}
	}
	boolean isApplet() {
		return applet!=null;
	}
	public static void main(String[] args) {
		new MyTopJPanel(null);
	}
	public final JFrame frame;
	final MyJApplet applet;
	private static final long serialVersionUID=1;
}
