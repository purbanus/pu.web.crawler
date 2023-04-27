package pu.web.crawler.tryout;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import pu.web.crawler.FileDownloader;
import pu.web.crawler.TagReader;

public class TryImageVue
{

public TryImageVue()
{
	super();
}

public static void main( String [] args )
{
	try
	{
		new TryImageVue().run();
	}
	catch ( MalformedURLException e )
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	catch ( IOException e )
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

private void run() throws MalformedURLException, IOException
{
	String urlString = "http://img44.imagevenue.com/img.php?loc=loc163&image=62e_carli01.jpg";
	URL url = new URL( urlString );

	//FileDownloader.download( url, "effe.txt" );

	TagReader tagReader = new TagReader( url );
	List<String> tags = tagReader.getAllTags();
	for ( String tag : tags )
	{
		tag = tag.toLowerCase();
		if ( tag.startsWith( "<img" ) )
		{
			int start = tag.indexOf( "src=" );
			if ( start >= 0 )
			{
				start += 5;
				int end = tag.indexOf( "\"", start + 1 );
				if ( end >= 0 )
				{
					String link = tag.substring( start, end );
					String completePath = "http://" + url.getHost() + "/" + link;
					FileDownloader.download( completePath, "effe.jpg" );
				}
			}

		}
	}
}

}
