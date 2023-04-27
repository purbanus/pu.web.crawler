package pu.web.crawler;

import java.io.File;

public class FileUtil
{
private FileUtil()
{
	super();
}

/**
 * @param completePath
 */
public static void mkDirs( String completePath )
{
	String dirs = completePath.substring( 0, completePath.lastIndexOf( '/' ) );

	File file = new File( dirs );
	file.mkdirs();
}


}
