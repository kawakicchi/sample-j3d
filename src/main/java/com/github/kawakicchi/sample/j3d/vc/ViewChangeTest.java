package com.github.kawakicchi.sample.j3d.vc;

import java.awt.GraphicsConfiguration;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class ViewChangeTest {

	public static void main(final String args[]) {
		JFrame frame = new JFrame();
		frame.setSize(400, 400);
		frame.setTitle("ViewChange_test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel cp = new JPanel();
		cp.setLayout(null);
		frame.add(cp);

		// ============================================================================
		// 次にJava3D関係の設定。
		// ============================================================================

		// 現在使用している画面の、ハードウェア情報を取得する
		GraphicsConfiguration g_config = SimpleUniverse.getPreferredConfiguration();

		// Cnavas3D_ViewChangeクラスを用意する（距離は3, マウス感度は0.01）
		Canvas3DViewChange canvas = new Canvas3DViewChange(3.0f, 0.01f, g_config);

		// 3D表示領域の大きさを設定。今回はウィンドウいっぱいに表示する
		canvas.setBounds(0, 0, 400, 400);

		// コンテントペインにCanvas3Dを登録
		cp.add(canvas);

		// ============================================================================
		// 3D空間を構築していきます
		// ============================================================================

		// Canvas3DクラスのSimpleUniverseを利用。
		SimpleUniverse universe = canvas.universe;

		// オブジェクトの「枝」を１つ作る
		BranchGroup group1 = new BranchGroup();

		// 「カラーキューブ」を１つ生成
		ColorCube cube = new ColorCube(0.4f);

		// 座標変換クラスを生成
		TransformGroup transform_group1 = new TransformGroup();

		// 座標変換の書き換えを許可
		transform_group1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		// カラーキューブを座標変換クラスに登録
		transform_group1.addChild(cube);

		// 「枝」に座標変換クラスを登録
		group1.addChild(transform_group1);

		// 仮想空間に「枝」を登録（これは最後に・・・！）
		universe.addBranchGraph(group1);

		// ウィンドウを可視化
		frame.setVisible(true);
	}

}