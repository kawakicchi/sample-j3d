package com.github.kawakicchi.sample.j3d;

import java.awt.GraphicsConfiguration;
import java.util.Timer;
import java.util.TimerTask;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class Sample01 {

	/** 仮想空間全体 */
	SimpleUniverse universe;

	BranchGroup group1;

	TransformGroup transformGroup1;

	Transform3D transform1;

	public static void main(String[] args) {
		new Sample01();
	}

	public Sample01() {
		JFrame frame = new JFrame();
		frame.setSize(400, 400);
		frame.setTitle("Java3D　動作確認");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel cp = new JPanel();
		cp.setLayout(null);

		// 現在使用している画面の、ハードウェア情報を取得する
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		canvas.setBounds(0, 0, 400, 400);
		cp.add(canvas);
		frame.add(cp);

		// 3D空間
		universe = new SimpleUniverse(canvas);

		group1 = new BranchGroup();

		ColorCube cube = new ColorCube(0.4f);

		transformGroup1 = new TransformGroup();
		transformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); // 座標変換の書き換えを許可
		transformGroup1.addChild(cube); // カラーキューブを座標変換クラスに登録

		group1.addChild(transformGroup1);
		universe.addBranchGraph(group1);

		// 座標変換内容
		transform1 = new Transform3D();

		// 視点

		// 視点についてのハードウェア情報を取得。
		ViewingPlatform vp = universe.getViewingPlatform();
		// 視点のための座標変換クラスを用意
		TransformGroup Camera = vp.getViewPlatformTransform();
		// カメラの位置ベクトル
		Transform3D view_pos = new Transform3D();
		// カメラの位置を決める
		Vector3f pos_vec = new Vector3f(1.4f, 1.4f, 1.4f);
		// カメラの位置について、座標変換実行
		view_pos.setTranslation(pos_vec);

		// カメラの向きを示すベクトル
		Transform3D view_dir = new Transform3D();
		Transform3D view_dir2 = new Transform3D();

		// カメラの向きを決める
		view_dir.rotY(Math.PI / 4);
		view_dir2.rotX(-Math.PI / 4 + 0.1f);
		view_dir.mul(view_dir2);

		// カメラの位置およびカメラの向きを統合
		view_pos.mul(view_dir);
		// カメラの位置情報を登録
		Camera.setTransform(view_pos);

		// Timer
		Timer timer = new Timer();
		timer.schedule(new RotTimer(), 0, 40);

		// ウィンドウを可視化
		frame.setVisible(true);
	}

	class RotTimer extends TimerTask {
		private float rot;

		public RotTimer() {
			rot = 0.0f;
		}

		public void run() {
			rot += Math.PI / 60; // 回転角を増やす
			transform1.rotY(rot); // 回転の座標変換実行
			transformGroup1.setTransform(transform1);// 座標変換を登録
			// １回転したら、回転角をリセットしておく
			if (rot > Math.PI * 2) {
				rot = 0.0f;
			}
		}
	}

}