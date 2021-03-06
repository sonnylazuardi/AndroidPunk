package net.androidpunk.tweens.misc;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Vector;

import net.androidpunk.Tween;
import net.androidpunk.flashcompat.OnCompleteCallback;
import net.androidpunk.flashcompat.OnEaseCallback;
import android.util.Log;

public class MultiVarTween extends Tween {

	private static final String TAG = "MultiVarTween";
	
	private Object mObject;
	private Vector<String> mVars = new Vector<String>();
	private Vector<Float> mStart = new Vector<Float>();
	private Vector<Float> mRange = new Vector<Float>();
	
	public MultiVarTween() {
		super(0, 0, null);
	}
	
	public MultiVarTween(OnCompleteCallback completeFunction) {
		super(0, 0, completeFunction);
	}
	
	/**
	 * Constructor.
	 * @param	complete		Optional completion callback.
	 * @param	type			Tween type.
	 */
	public MultiVarTween(OnCompleteCallback completeFunction, int type) {
		super(0, type, completeFunction);
	}
	
	/**
	 * Tweens multiple numeric public properties.
	 * @param	object		The object containing the properties.
	 * @param	values		An object containing key/value pairs of properties and target values.
	 * @param	duration	Duration of the tween.
	 * @param	ease		Optional easer function.
	 */
	public void tween(Object object, Map<String, Number> values, float duration, OnEaseCallback easeFunction) {
		mObject = object;
		mVars.clear();
		mStart.clear();
		mRange.clear();
		mTarget = duration;
		mEase = easeFunction;
		for (String p : values.keySet()) {
			try {
				Field f = object.getClass().getField(p);
				
				Number v = values.get(p);
				float startValue;
				float destValue = v.floatValue();
				
				if (Float.TYPE.equals(f.getType())) {
					startValue = f.getFloat(object);
				} else if (Integer.TYPE.equals(f.getType())) {
					startValue = f.getInt(object);
				} else if (Double.TYPE.equals(f.getType())) {
					startValue = (float)f.getDouble(object);
				} else {
					Log.e(TAG, "Cannot tween type: \"" + f.getType().toString() + "\".");
					return;
				}
				//Log.d(TAG, "Start value: " + startValue + "");
				mVars.add(p);
				mStart.add(startValue);
				mRange.add(destValue - startValue);
				
			}catch (NoSuchFieldException e) {
				Log.e(TAG, "The Object does not have the property \"" + p + "\", or it is not accessible.");
				continue;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		start();
	}
	
	/** @private Updates the Tween. */
	@Override
	public void update() {
		super.update();
		int i = mVars.size();
		while (i-- > 0) {
			try {
				
				Float v = mStart.get(i);
				float value = v + mRange.get(i) * mT;
				//Log.d(TAG, "Setting \""+ mVars.get(i)+"\" value: " + value + "");
				Field f = mObject.getClass().getField(mVars.get(i));
				if (Float.TYPE.equals(f.getType())) {
					f.setFloat(mObject, value);
				} else if (Integer.TYPE.equals(f.getType())) {
					f.setInt(mObject, (int)value);
				} else if (Double.TYPE.equals(f.getType())) {
					f.setDouble(mObject, value);
				}
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
	}
}
