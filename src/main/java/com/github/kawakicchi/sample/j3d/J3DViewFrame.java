package com.github.kawakicchi.sample.j3d;

import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.media.j3d.Canvas3D;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.j3d.utils.universe.SimpleUniverse;

public class J3DViewFrame extends JFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = -2081282136495432332L;

	private Canvas3D canvas;

	/** 仮想空間全体 */
	private SimpleUniverse universe;

	public J3DViewFrame(final J3DViewFrameListener listener) {
		setTitle("Java3D　動作確認");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel cp = new JPanel();
		cp.setLayout(null);

		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas = new Canvas3D(config);
		cp.add(canvas);
		add(cp);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Insets insets = getInsets();
				int width = getWidth() - (insets.left + insets.right);
				int height = getHeight() - (insets.top + insets.bottom);

				canvas.setBounds(0, 0, width, height);
			}
		});

		// ////////////////////////////////////////////////////
		// 3D空間
		universe = new SimpleUniverse(canvas);

		if (null != listener) {
			listener.init(universe);
		}

		setSize(800, 600);
	}

	public interface J3DViewFrameListener {
		public void init(final SimpleUniverse universe);
	}
}
