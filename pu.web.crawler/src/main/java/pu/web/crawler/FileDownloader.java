package pu.web.crawler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import nl.mediacenter.services.FileHelper;

/**
 * Zit ook in pu.web pu.download
 */
public class FileDownloader
{

/**
 * Download constructor comment.
 */
private FileDownloader()
{
	super();
}
public static void download( String aUrl, String aOutPath ) throws IOException
{
	download( new URL( aUrl ), aOutPath, null );
}

public static void download( String aUrl, String aOutPath, String aReferer ) throws IOException
{
	download( new URL( aUrl ), aOutPath, aReferer );
}

public static void download( URL aUrl, String aOutPath ) throws IOException
{
	download( aUrl, aOutPath, null );
}
public static void download( URL aUrl, String aOutPath, String aReferer ) throws IOException
{
	URLConnection urlConnection = aUrl.openConnection();
	if ( aReferer != null )
	{
		urlConnection.setRequestProperty( "Referer", aReferer );
	}
	try ( InputStream in = urlConnection.getInputStream();
	      OutputStream out = new FileOutputStream( aOutPath );
	)
	{
		//FileHelper.copyFile( in, out );
		//in.transferTo( out );
		FileHelper.copyStream( in, out );
	}

}

}
