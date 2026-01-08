package com.tayek.utilities;
import java.awt.BorderLayout;
import javax.swing.JApplet;
public class MyJApplet extends JApplet {
	public void init() {
		getContentPane().setLayout(new BorderLayout());
		addContent();
	}
	public void addContent() {
		getContentPane().add(new MyTopJPanel(this), BorderLayout.CENTER);
	}
	public static void main(String[] args) {
		new MyTopJPanel(new MyJApplet());
	}
	private static final long serialVersionUID = 1;
}