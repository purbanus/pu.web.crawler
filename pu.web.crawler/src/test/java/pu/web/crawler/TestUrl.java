package pu.web.crawler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import nl.mediacenter.services.FileHelper;

public class TestUrl
{
	private static final String FILE_STRING = "hmlist77.html";
	private static final String URL_STRING = "http://www.heavymetalmagazinefanpage.com/hmlist77.html";
	public static void main( String [] args ) throws IOException
	{
		new TestUrl().runCopyStream();
		//new TestUrl().runJSoup();
	}
	public void runCopyStream() throws IOException
	{
		URL url = new URL( URL_STRING );
		URLConnection connection = url.openConnection();
		System.out.println( connection.getHeaderFields() );
		
		try ( InputStream in =  url.openStream();
			FileOutputStream out = new FileOutputStream( FILE_STRING );)
		{
			FileHelper.copyStream( in, out );
		}

	}
	public void runJSoup() throws IOException
	{
		Charset charset = Charset.forName( "UTF-8" );
		//Charset charset = Charset.forName( "windows-1252" );
		Connection jsoupConnection = Jsoup.connect(URL_STRING); 
		//jsoupConnection.data( , null )
		Document document = jsoupConnection.get();
		document.charset( charset );
		String html = document.html();
		// Dit werkt niet want er is geen gelegenheid om dee html string in de writer te zetten!!
//		StringWriter writer = new StringWriter();
//		IOUtils.copy( url.openStream(), writer, "UTF-8" );
//		writer.write( html );
		FileOutputStream out = new FileOutputStream( FILE_STRING );
		
		try ( OutputStreamWriter writer = new OutputStreamWriter( out, charset ) )
		{
			writer.write( html );
		}
	}
	
}
