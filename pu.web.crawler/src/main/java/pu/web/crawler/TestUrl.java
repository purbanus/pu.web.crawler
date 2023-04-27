package pu.web.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import nl.mediacenter.services.FileHelper;

public class TestUrl
{
	public static void main( String [] args ) throws IOException
	{
		URL url = new URL( "http://www.bol.com/nl/p/elektronica/melitta-e965-101-caffeo-gourmet-s/9000000011920097/index.html" );
		URLConnection connection = url.openConnection();
		System.out.println( connection.getHeaderFields() );
		
		try ( InputStream in =  url.openStream(); )
		{
			FileHelper.copyStream( in, System.out );
		}
	}
}
