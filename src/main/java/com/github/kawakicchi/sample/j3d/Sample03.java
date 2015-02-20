package com.github.kawakicchi.sample.j3d;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.github.kawakicchi.sample.j3d.J3DViewFrame.J3DViewFrameListener;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class Sample03 {

	public static void main(final String[] args) {
		new Sample03();
	}

	public Sample03() {

		J3DViewFrame frm = new J3DViewFrame(new J3DViewFrameListener() {
			@Override
			public void init(final SimpleUniverse universe) {
				doInit(universe);
				;
			}
		});
		frm.setVisible(true);
	}

	public void doInit(final SimpleUniverse universe) {
		// カメラ設定
		ViewingPlatform camera = universe.getViewingPlatform();
		camera.setNominalViewingTransform(); // 自動位置合わせ

		// ライト設定
		Color3f lightColor = new Color3f(1.7f, 1.7f, 1.7f);
		Vector3f lightDirection = new Vector3f(0.2f, -0.2f, -0.6f);
		DirectionalLight light = new DirectionalLight(lightColor, lightDirection);
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0d, 0.0d, 0.0d), 100); // 範囲1
		light.setInfluencingBounds(bounds); // ライトの効果範囲
		BranchGroup grpLight = new BranchGroup();
		grpLight.addChild(light);
		universe.addBranchGraph(grpLight);

		Transform3D cameraTrans = new Transform3D();
		cameraTrans.lookAt(new Point3d(0, 1.6, 10), new Point3d(0, 0, -10), new Vector3d(0, 1, 0));
		cameraTrans.invert();
		universe.getViewingPlatform().getViewPlatformTransform().setTransform(cameraTrans);

		//
		BranchGroup grpWorldBranch = new BranchGroup();

		for (int x = -100; x < 100; x++) {
			for (int z = -100; z < 100; z++) {
				Appearance appr = new Appearance();
				Material mate = new Material();
				mate.setDiffuseColor( (float)(x+100)/200.f , (float)(z+100)/200.f, 1.0f);
				appr.setMaterial(mate);

				Box box = new Box(0.1f, 0.1f, 0.1f, appr);

				TransformGroup tran = new TransformGroup();
				tran.addChild(box);

				Transform3D transf = new Transform3D();
				transf.setTranslation(new Vector3f(0.1f * (float) x, 0.0f, -0.1f * (float) z));
				tran.setTransform(transf);

				grpWorldBranch.addChild(tran);
			}
		}

		universe.addBranchGraph(grpWorldBranch);
	}
}
