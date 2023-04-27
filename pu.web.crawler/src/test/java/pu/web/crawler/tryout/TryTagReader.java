package pu.web.crawler.tryout;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.junit.Test;

import pu.web.crawler.TagReader;

public class TryTagReader
{
//@Test
public void test() throws MalformedURLException, IOException
{
	//String uri = "file://pu/xml/HTML Voorbeeld.html";
	//String uri = "http://localhost";
	String uri = "http://commons.wikimedia.org/wiki/Category:Maps_of_polders_in_the_Netherlands_drawn_by_W.H._Hoekwater";
	TagReader rt = new TagReader( uri );
	for ( String string : rt.getAllTags() )
	{
		System.out.println( string );
	}
	rt.close();
}

@Test
public void testUrl() throws MalformedURLException, IOException
{
	//String uri = "http://commons.wikimedia.org/wiki/Category:Maps_of_polders_in_the_Netherlands_drawn_by_W.H._Hoekwater";
	//String uri = "http://www.google.com";
	String uri = "file:///C:/temp/kaarten/Category%20Maps%20of%20polders%20in%20the%20Netherlands%20drawn%20by%20W.H.%20Hoekwater%20-%20Wikimedia%20Commons.html";
	URL url = new URL( uri );
	URLConnection connection = url.openConnection();
	System.out.println( connection.getContentType() );
	System.out.println( connection.getContentLength() );
	System.out.println( connection.getContent() );
	TagReader rt = new TagReader( uri );
	for ( String string : rt.getAllTags() )
	{
		System.out.println( string );
	}
	rt.close();
}


}
