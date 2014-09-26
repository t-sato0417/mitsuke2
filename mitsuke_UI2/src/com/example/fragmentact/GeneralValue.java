package com.example.fragmentact;

import android.os.Environment;

public class GeneralValue {
	static String approot = Environment.getExternalStorageDirectory().getPath()+"/mitsuke2";
	//アプリケーションが利用するフォルダ
	static String savefolder = Environment.getExternalStorageDirectory().getPath()+"/mitsuke2/save";
	//ユーザがセーブしたXMLを保存するフォルダ
	static String cachefolder = Environment.getExternalStorageDirectory().getPath()+"/mitsuke2/cache";
	//オンラインから取得したXMLを保存するフォルダ。
	static String onlinedir = "xml";
}
