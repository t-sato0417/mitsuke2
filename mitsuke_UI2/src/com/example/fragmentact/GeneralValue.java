package com.example.fragmentact;

import android.os.Environment;

public class GeneralValue {
	static String approot = Environment.getExternalStorageDirectory().getPath()+"/mitsuke2";
	//�A�v���P�[�V���������p����t�H���_
	static String savefolder = Environment.getExternalStorageDirectory().getPath()+"/mitsuke2/save";
	//���[�U���Z�[�u����XML��ۑ�����t�H���_
	static String cachefolder = Environment.getExternalStorageDirectory().getPath()+"/mitsuke2/cache";
	//�I�����C������擾����XML��ۑ�����t�H���_�B
	static String onlinedir = "xml";
}
