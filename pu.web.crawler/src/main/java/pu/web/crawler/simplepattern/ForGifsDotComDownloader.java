package pu.web.crawler.simplepattern;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import pu.web.crawler.FileDownloader;
import pu.web.crawler.FileUtil;
import pu.web.crawler.TagReader;

public class ForGifsDotComDownloader
{
private static final String TARGET = "/home/purbanus/Pictures/new/warmers";
private final NumberFormat format = new DecimalFormat( "000" );

public static void main( String [] args )
{
	new ForGifsDotComDownloader().run();
}

public void run()
{
	for ( int x = 1; x < 200; x++ )
	{
		try
		{
			if ( x % 50 == 0 )
			{
				Thread.sleep( 10000 );
			}
			verwerk( x );
		}
		catch ( Exception e )
		{
			//System.out.println( "FOUT BIJ " + x );
			e.printStackTrace();
			return;
		}
	}
}

private void verwerk( int aX ) throws IOException
{
	final String file = "4GIFsDotCom" + format.format( aX ) + ".jpg";
	final String target = TARGET + "/" + file;
	final String urlString = "http://pix.4gifs.com/gallery/v/Warmers/" + file + ".html";
	URL imgUrl = findOurImage( new URL( urlString ) );
	if ( imgUrl == null )
	{
		System.out.println( "Image not found in " + urlString );
	}
	else
	{
		FileUtil.mkDirs( target );
		FileDownloader.download( imgUrl, target );
		System.out.println( target );
	}
}

private URL findOurImage( URL aUrl ) throws MalformedURLException, IOException
{
	// aUri is een geheimzinnige html pagina. we moeten daarin het pad vinden naar de
	// echte file
	// En die &amp; moet eruit
	String uriString = aUrl.toExternalForm();
	uriString = uriString.replaceAll( "&amp;", "&" );
	TagReader tagReader = new TagReader( uriString );
	List<String> tags = tagReader.getAllTags();
	for ( String tag : tags )
	{
		String tagLC = tag.toLowerCase();
		if ( tagLC.startsWith( "<img" ) )
		{
			int start = tagLC.indexOf( "src=" );
			if ( start >= 0 )
			{
				start += 5;
				int end = tag.indexOf( "\"", start + 1 );
				if ( end >= 0 )
				{
					String link = tag.substring( start, end );
					if ( link.startsWith( "/gallery/d/15" ) )
					{
						return new URL( aUrl, link );
					}
				}
			}
		}
	}
	return null;
}


}
