package com.example.fragmentact;

import android.os.Environment;

public class GeneralValue {
	static String approot = Environment.getExternalStorageDirectory().getPath()+"/mitsuke2";
	static String savefolder = Environment.getExternalStorageDirectory().getPath()+"/mitsuke2/save";
	static String cachefolder = Environment.getExternalStorageDirectory().getPath()+"/mitsuke2/cache";
}
