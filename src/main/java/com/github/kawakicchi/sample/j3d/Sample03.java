package com.github.kawakicchi.sample.j3d;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.github.kawakicchi.sample.j3d.J3DViewFrame.J3DViewFrameListener;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
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
		BoundingSphere bounds = new BoundingSphere(); // 範囲1
		light.setInfluencingBounds(bounds); // ライトの効果範囲
		BranchGroup grpLight = new BranchGroup();
		grpLight.addChild(light);
		universe.addBranchGraph(grpLight);

		//
		BranchGroup grpWorldBranch = new BranchGroup();

		for (int x = 0; x < 10; x++) {
			for (int z = 0; z < 10; z++) {
				Appearance appr = new Appearance();
				Material mate = new Material();
				mate.setDiffuseColor(0.5f, 0.5f, 1.0f);
				appr.setMaterial(mate);

				 Box box = new Box(0.1f, 0.1f, 0.1f, appr);
				//Sphere box = new Sphere(0.1f, appr);

				TransformGroup tran = new TransformGroup();
				tran.addChild(box);

				Transform3D transf = new Transform3D();
				transf.setTranslation(new Vector3f( -0.5f +(0.1f * (float) x), 0.5f + (-0.1f * (float) z), 0.0f));
				tran.setTransform(transf);

				grpWorldBranch.addChild(tran);
			}
		}

		universe.addBranchGraph(grpWorldBranch);
	}
}
