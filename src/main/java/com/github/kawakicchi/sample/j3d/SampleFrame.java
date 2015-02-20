package com.github.kawakicchi.sample.j3d;

import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class SampleFrame extends JFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = -2849102482965534588L;

	private Canvas3D canvas;

	/** 仮想空間全体 */
	private SimpleUniverse universe;

	public SampleFrame() {
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
		canvas.addKeyListener(new KeyController());

		// ////////////////////////////////////////////////////
		// 3D空間
		universe = new SimpleUniverse(canvas);
		init(universe);

		setSize(400, 400);
	}

	private void init(SimpleUniverse universe) {
		//
		Appearance appr = new Appearance();
		Material mate = new Material();
		mate.setDiffuseColor(0.5f, 0.5f, 1.0f);
		appr.setMaterial(mate);

		// オブジェクト設定
		BranchGroup grpObject = new BranchGroup();
		TransformGroup trans = new TransformGroup();

		trans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Sphere sphere1 = new Sphere(0.3f, appr);
		trans.addChild(sphere1);
		Box sphere2 = new Box(0.5f, 0.1f, 0.3f, appr);
		trans.addChild(sphere2);

		grpObject.addChild(trans);
		universe.addBranchGraph(grpObject);

		Transform3D transf = new Transform3D();
		transf.setTranslation(new Vector3f(0.8f, 0.0f, 0.0f));
		// transf.rotY(Math.PI / 4);
		// transf.setScale(2.0f);
		trans.setTransform(transf);

		// カメラ設定
		ViewingPlatform camera = universe.getViewingPlatform();
		camera.setNominalViewingTransform(); // 自動位置合わせ

		// ライト設定
		Color3f lightColor = new Color3f(1.7f, 1.7f, 1.7f);
		Vector3f lightDirection = new Vector3f(0.2f, -0.2f, -0.6f);
		DirectionalLight light = new DirectionalLight(lightColor, lightDirection);
		BoundingSphere bounds = new BoundingSphere(); // 範囲1
		light.setInfluencingBounds(bounds); // ライトの効果範囲
		BranchGroup grpLight = new BranchGroup();
		grpLight.addChild(light);
		universe.addBranchGraph(grpLight);
	}

	private class KeyController implements KeyListener {

		private boolean up;
		private boolean down;
		private boolean left;
		private boolean right;

		public KeyController() {
			up = false;
			down = false;
			left = false;
			right = false;
		}

		public boolean isUp() {
			return up;
		}

		public boolean isDown() {
			return down;
		}

		public boolean isLeft() {
			return left;
		}

		public boolean isRight() {
			return right;
		}

		private void update(final int keyCode, final boolean press) {
			switch (keyCode) {
			case KeyEvent.VK_W: {
				up = press;
				break;
			}
			case KeyEvent.VK_S: {
				down = press;
				break;
			}
			case KeyEvent.VK_A: {
				left = press;
				break;
			}
			case KeyEvent.VK_D: {
				right = press;
				break;
			}
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			update(e.getKeyCode(), true);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			update(e.getKeyCode(), false);
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}
	}
}
