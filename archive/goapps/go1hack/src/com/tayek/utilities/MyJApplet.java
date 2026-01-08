package com.tayek.utilities;
import java.awt.BorderLayout;
import javax.swing.JApplet;
public class MyJApplet extends JApplet {
	public void init() {
		getContentPane().setLayout(new BorderLayout());
		addContent();
	}
	public void addContent() {
		getContentPane().add(new MainGui(this), BorderLayout.CENTER);
	}
	public static void main(String[] args) {
		new MainGui(null);
	}
	private static final long serialVersionUID = 1;
}