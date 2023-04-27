package pu.web.crawler.simplepattern;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import pu.web.crawler.FileDownloader;
import pu.web.crawler.FileUtil;
import pu.web.crawler.HtmlUtil;
import pu.web.crawler.TagReader;

public class RediculouslyBeautifulDownloader
{
private static final String BASE_URL   = "http://ridiculouslybeautiful.tumblr.com/page/";
private static final String TARGET_DIR = "/home/purbanus/Pictures/new/rediculously-beautiful";
@SuppressWarnings( "unused" )
private final NumberFormat format2 = new DecimalFormat( "00" );
@SuppressWarnings( "unused" )
private final NumberFormat format4 = new DecimalFormat( "0000" );
int dir = 0;
int fileCounteroide = 0;

public static void main( String [] args )
{
	new RediculouslyBeautifulDownloader().run();
}

public void run()
{
	for ( int x = 1326; x >= 1326; x-- )
	{
		try
		{
			if ( x % 10 == 0 )
			{
				Thread.sleep( 10000 );
			}
			verwerk( x );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			return;
		}
	}
}

private void verwerk( int aX ) throws IOException
{
	final String urlString = BASE_URL + aX;

	// Als het de tagreader niet lukt om een pagina te lezen kappen we
	TagReader tagReader = new TagReader( urlString );
	List<String> tags = tagReader.getAllTags();
	List<String> imgUrls = findOurImages( tags );

	int index = 0;
	for ( String url : imgUrls )
	{
		fileCounteroide++;
		if ( fileCounteroide % 100 == 0 )
		{
			fileCounteroide = 0;
			dir++;
		}
		String file = url;
		if ( file.startsWith( "http://" ) )
		{
			file = file.substring( 7 );
		}
		file = file.replaceAll( "/", "-" );
		file  = file + ".jpg";

		final String target = TARGET_DIR + "/" + dir + "/" + aX + "-" + index + "-" + file;
		FileUtil.mkDirs( target );
		try
		{
			FileDownloader.download( url, target );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		System.out.println( url );
		index++;
	}
}

private List<String> findOurImages( List<String> aTags )
{
	// We hebben nu een pagina met 10 images.Die moeten we eruit peuteren. Er zijn twee vormen:
	// Een embedded image
	//   <img src="http://27.media.tumblr.com/tumblr_lwkbqqA5d71qd33kzo1_500.jpg" alt=""/>
	// Een link.
	//   <a href="http://www.tumblr.com/photo/1280/14732563541/1/tumblr_ljju0s4ezc1qilvnj">
	//   	<img src="http://28.media.tumblr.com/tumblr_ljju0s4ezc1qilvnjo1_500.jpg" alt=""/>
	//   </a>
	// Er staan verder geen images op een page, dus we doen
	// - alle <img> tags
	// - behalve als die rechtstreeks voorafgegaan wordt door een <a>

	List<String> links = new ArrayList<String>();
	String currentAtag = null;
	for ( String tag : aTags )
	{
		String tagLC = tag.toLowerCase();
		if ( tagLC.startsWith( "<a " ) )
		{
			currentAtag = tag;
		}
		else
		{
			if ( tagLC.startsWith( "<img " ) )
			{
				if ( currentAtag != null )
				{
					links.add( HtmlUtil.extractLink( currentAtag, 0, "href" ) );
				}
				else
				{
					links.add( HtmlUtil.extractLink( tagLC, 0, "src" ) );
				}
			}
			currentAtag = null;
		}
	}
	return links;
}


}
