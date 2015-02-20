package com.github.kawakicchi.sample.j3d.vc;

import java.awt.GraphicsConfiguration;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class Canvas3DViewChange extends Canvas3D {

	/** serialVersionUID */
	private static final long serialVersionUID = -8224958251153355502L;

	// マウスの動きに対する感度
	float sensitivity;

	// 動径の長さ（座標中心と視点との距離）
	float camera_distance;

	// 新規取得するマウスのx,y座標
	int new_x, new_y;

	// 前に取得したマウスのx,y座標
	int pre_x, pre_y;

	// SimpleUniverseをフィールドとしてもっておく。
	SimpleUniverse universe;

	// 視点の座標変換のためのグループ
	TransformGroup Camera;

	// 視点（カメラ）の座標
	float camera_x, camera_y, camera_z, camera_xz, camera_xy, camera_yz = 0;

	// 極座標のパラメータ
	float phi = 0;// (float)Math.PI;
	float theta = (float) Math.PI / 3;

	// 座標変換クラス
	Transform3D Transform_camera_pos; // カメラの位置
	Transform3D Transform_camera_phi; // phiに関する回転
	Transform3D Transform_camera_theta; // thetaに関する回転

	// 3次元ベクトル（カメラの位置用）
	Vector3f Vector_camera_pos;

	// =============================================================================
	// コンストラクタ
	public Canvas3DViewChange(float Distance, float Sensitivity, GraphicsConfiguration config) {
		// 親クラスのコンストラクタを呼ぶ. 引数はGraphicsConfiuration.
		super(config);

		// カメラの原点からの距離を設定
		camera_distance = Distance;

		// マウス感度を調節
		sensitivity = Sensitivity;

		// 空のSimpleUniverseを生成
		universe = new SimpleUniverse(this);

		// ============================================================================
		// 視点（カメラ）について設定
		// ============================================================================

		// ------------------------------------------------------------------
		// カメラ全般の初期設定

		// SimpleUniverseが生成したViewingPlatformを取得
		ViewingPlatform vp = universe.getViewingPlatform();

		// ViewingPlatformの座標変換グループとして，“Camera”を割り当てる
		Camera = vp.getViewPlatformTransform();

		// ------------------------------------------------------------------
		// カメラの位置に関する初期設定

		// theta関係の計算（球座標→直交座標）
		camera_y = camera_distance * (float) Math.sin(theta);
		camera_xz = camera_distance * (float) Math.cos(theta);

		// phi関係の計算（球座標→直交座標）
		camera_x = camera_xz * (float) Math.sin(phi);
		camera_z = camera_xz * (float) Math.cos(phi);

		// カメラの位置ベクトルを用意
		Vector_camera_pos = new Vector3f(camera_x, camera_y, camera_z);

		// カメラ位置の座標変換クラスを用意
		Transform_camera_pos = new Transform3D();

		// 初期位置設定ための座標変換を用意する。
		Transform_camera_pos.setTranslation(Vector_camera_pos);

		// ------------------------------------------------------------------
		// カメラの向きに関する初期設定

		// カメラの向きの座標変換クラスを用意
		Transform_camera_phi = new Transform3D();
		Transform_camera_theta = new Transform3D();

		// カメラの向きの初期設定
		Transform_camera_theta.rotX(-theta);
		Transform_camera_phi.rotY(phi);

		// ------------------------------------------------------------------
		// 以上の設定をカメラに反映

		// 合成する
		Transform_camera_theta.mul(Transform_camera_phi);
		Transform_camera_pos.mul(Transform_camera_theta);

		// 座標変換実行
		Camera.setTransform(Transform_camera_pos);

		// ============================================================================
		// マウスの設定
		// ============================================================================

		// マウス入力用のクラス，mouse_classをインスタンス化
		mouse_ViewChange mouse = new mouse_ViewChange();

		// このフレームにマウス入力クラスを登録
		addMouseMotionListener(mouse);

	}

	// *****************************************************************************
	// mouse_ViewChange：マウス入力用のクラス
	// *****************************************************************************
	class mouse_ViewChange implements MouseMotionListener {

		// ============================================================================
		// マウスが動いた時に呼ばれるメソッド
		// ============================================================================
		public void mouseMoved(MouseEvent event) {
			// 常にマウス座標を更新しておく
			pre_x = event.getX();
			pre_y = event.getY();
		}

		// ============================================================================
		// マウスがドラッグされた時に呼ばれるメソッド
		// ============================================================================
		public void mouseDragged(MouseEvent event) {

			// 現在のx座標を取得
			new_x = event.getX();
			new_y = event.getY();

			// thetaとphiの値を更新
			theta -= sensitivity * (new_y - pre_y);
			phi += sensitivity * (new_x - pre_x);

			// ===========================================================================
			// 極座標を直交座標へ直す

			// theta関係の計算（球座標→直交座標）
			camera_y = camera_distance * (float) Math.sin(theta);
			camera_xz = camera_distance * (float) Math.cos(theta);

			// phi関係の計算（球座標→直交座標）
			camera_x = camera_xz * (float) Math.sin(phi);
			camera_z = camera_xz * (float) Math.cos(phi);

			// ===========================================================================
			// 座標変換クラスを用意する

			// カメラの位置ベクトルを作る
			Vector_camera_pos.x = camera_x;
			Vector_camera_pos.y = camera_y;
			Vector_camera_pos.z = camera_z;

			// 座標変換クラスを初期化（重要！）
			Transform_camera_pos.setIdentity();

			// 平行移動の座標変換を用意
			Transform_camera_pos.setTranslation(Vector_camera_pos);

			// 回転の座標変換を用意
			Transform_camera_theta.rotX(-theta);
			Transform_camera_phi.rotY(phi);

			// ===========================================================================
			// カメラの座標変換実行

			// 合成する
			Transform_camera_phi.mul(Transform_camera_theta);
			Transform_camera_pos.mul(Transform_camera_phi);

			// 座標変換実行
			Camera.setTransform(Transform_camera_pos);

			// ===========================================================================
			// マウス座標を更新しておく
			pre_x = event.getX();
			pre_y = event.getY();

		}
	}
	// mouse_ViewChangeここまで
	// *****************************************************************************

}