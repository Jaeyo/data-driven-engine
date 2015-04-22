package org.jaeyo.dde.common;

import java.io.File;

public class Path{
	public static File getPackagePath() {
		String jarPath = Conf.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		File jarFile = new File(jarPath);
		File packagePath = jarFile.getParentFile();
		return packagePath;
	} // getPackagePath
	
	public static File getWebInfPath(){
		return new File(getPackagePath(), "../WEB-INF");
	} //getWebInfPath
} //class