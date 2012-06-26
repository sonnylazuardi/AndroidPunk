package com.gamblore.androidpunk;

import net.androidpunk.Engine;
import net.androidpunk.FP;
import android.util.Log;

public class Main extends Engine {

	private static final String TAG = "Game";
	
	public Main(int width, int height, float frameRate, boolean fixed) {
		super(width, height, frameRate, fixed);
		FP.setWorld(new OgmoEditorWorld(R.raw.intro_1));
	}

	@Override
	public void init() {
		Log.d(TAG, "At init!");
	}
}
