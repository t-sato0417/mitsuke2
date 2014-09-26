package com.example.sshconnect;


import java.io.*;
import java.util.Vector;

import android.os.Environment;

import com.jcraft.jsch.*;

public class Sftp extends Thread{
	private static final String hostname = "153.121.37.74";
	private static final int	portnumber	= 5750;
	private static final String userid   = "osawa";
	private static final String password = "mitsuke2";
	
	boolean get;//trueならget falseならput
	String srcfile;
	String dstfile;
	public void setSetting(String filename,String dstfile,boolean get){
		System.out.println("Debug:sftp:srcfile:"+srcfile+" dstfile:"+dstfile+" ifget:"+get);
		this.get=get;
		this.srcfile=filename;
		this.dstfile=dstfile;
	}
	
	public void run(){
		try {
			doProc();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void doProc() throws JSchException, SftpException, IOException {
		System.out.println("Debug:"+"SFTPSTART");
		JSch jsch=new JSch();
		//jsch.setKnownHosts(knownhost);	// known_hosts を設定して HostKey チェックをおこなう
		
		// connect session
		Session session=jsch.getSession(userid, hostname, portnumber);
		java.util.Properties config = new java.util.Properties(); 
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
		session.setPassword(password);
		session.connect();

		// sftp remotely
		ChannelSftp channel=(ChannelSftp)session.openChannel("sftp");
		channel.connect();

		// ls
		Vector list = channel.ls(".");
		// System.out.println(list.get(0).getClass().getName());
		System.out.println("---- ls");
		for (int i=0;i<list.size();i++) {
			System.out.println(list.get(i));
		}

		// lstat
		try {
			
			SftpATTRS stat = channel.lstat("index.html");
			System.out.println("---- lstat");
			System.out.println(stat);
			System.out.println(stat.getSize());
		} catch (SftpException ex) { 
			// ファイルが存在しないとき
			System.out.println("Debug:notfile");
			ex.printStackTrace();
		}

		// get
		if(get){
			//channel.get("./index.html", Environment.getExternalStorageDirectory().getPath()+"/index.html.dst");
			channel.get(srcfile,dstfile);
		}else{
			channel.put(srcfile,dstfile);
		}

		channel.disconnect();
		session.disconnect();
		
	}
}