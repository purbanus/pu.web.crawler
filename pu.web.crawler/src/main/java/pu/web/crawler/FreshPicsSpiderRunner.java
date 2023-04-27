package pu.web.crawler;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 * Runs the Spider
 */
class FreshPicsSpiderRunner implements Observer
{
private final String baseFileName;
@SuppressWarnings( "unused" )
private Format twoDigitNumberFormat = new DecimalFormat( "00" );
@SuppressWarnings( "unused" )
private Format fourDigitNumberFormat = new DecimalFormat( "0000" );
private static int METHOD = 1;

public FreshPicsSpiderRunner( String aUrlString, String aBaseFileName, int aDepth ) throws MalformedURLException
{
	super();
	baseFileName = aBaseFileName;

	URL url = new URL( HtmlUtil.adjustIfDir( aUrlString ) );
	Spider spider = new Spider( url, aDepth );
	spider.addObserver(this);
	spider.setAcceptDifferentHosts( true );
	spider.run();
}

public static void main(String[] args)
{
	// http://freshpics.blogspot.com/2008/06/russian-cheerleaders.html
	//
	//final String SUBJECT = "beautiful-photos-of-nature-by-mike";
	//final String HTML_START = "http://freshpics.blogspot.com/2008/04/" + SUBJECT + ".html";
	//final String TARGET  = "/tmp/crawler/freshpics/" + SUBJECT;

	//final String SUBJECT = "russian-cheerleaders";
	//final String HTML_START = "http://freshpics.blogspot.com/2008/06/" + SUBJECT + ".html";
	//final String TARGET  = "/tmp/crawler/freshpics/" + SUBJECT;

	//final String SUBJECT = "30-fantastic-hdr-photos-by-grey-jones";
	//final String HTML_START = "http://freshpics.blogspot.com/2009/08/" + SUBJECT + ".html";
	//final String TARGET  = "/tmp/crawler/freshpics/" + SUBJECT;

	//final String SUBJECT = "cannes-lions-2010-winners";
	//final String HTML_START = "http://freshpics.blogspot.com/2010/06/" + SUBJECT + ".html";
	//final String TARGET  = "/home/purbanus/Pictures/new/" + SUBJECT;

	final String SUBJECT = "world-bodypainting-festival-2010-in-seeboden-austria";
	final String HTML_START = "http://freshpics.blogspot.com/2010/08/world-bodypainting-festival-2010-in.html";
	final String TARGET  = "/home/purbanus/Pictures/new/" + SUBJECT;
	METHOD = 2;

	final int DEPTH = 1;
	try
	{
		new FreshPicsSpiderRunner( HTML_START, TARGET, DEPTH );
	}
	catch ( IOException e )
	{
		//Log.error( SpiderRunner.class, e );
		e.printStackTrace();
	}
	//Log.info( SpiderRunner.class, "Finished" );
	System.out.println( "Finished" );
}

// This method is called immediately whenever the spider
// discovers a new URL.  It should return as quickly as
// possible since it is holding up the spider.
@Override
public void update(Observable o, Object arg)
{
	Spider.SpiderArgs aSpiderArgs = (Spider.SpiderArgs) arg;

	URL aUriToDownload = aSpiderArgs.linkedUri;
	String aLocalPath = aUriToDownload.getPath();
	if ( HtmlUtil.isProbablyDir( aLocalPath ) )
	{
		return;
	}
	if ( aUriToDownload.getPath().endsWith( ".html" )
			|| aUriToDownload.getPath().endsWith( ".js" )
			|| aUriToDownload.getPath().endsWith( ".css" )
			|| aUriToDownload.getPath().endsWith( ".ico" )
			)
	{
		return;
	}
	switch ( METHOD )
	{
		case 1:
			updateMethod1( aSpiderArgs, aUriToDownload, aLocalPath );
			break;
		case 2:
			updateMethod2( aSpiderArgs, aUriToDownload, aLocalPath );
			break;
	}
}

private void updateMethod1( Spider.SpiderArgs aSpiderArgs, URL aUriToDownload, String aLocalPath )
{
	if ( aLocalPath.contains( "/s1600-h/" ) )
	{
		URL newUri = createImgPhpPath( aUriToDownload );
		if ( newUri != null )
		{
			aUriToDownload = newUri;
			//String number = fourDigitNumberFormat.format( ++counter );
			//localPath = localPath + "." + number + ".jpg";

			// Haal de pure filenaam eruit
			int slashIndex = aUriToDownload.getPath().lastIndexOf( "/" );
			aLocalPath = "/hires" + aUriToDownload.getPath().substring( slashIndex );
		}
	}

	String completeLocalPath = baseFileName + aLocalPath;

	mkDirs( completeLocalPath );

	StringBuffer sb = new StringBuffer();
	for ( int x = 0; x < aSpiderArgs.depth; x++ )
	{
		sb.append( "  ");
	}
	sb.append( aUriToDownload ).append( " ==> " ).append( completeLocalPath );

	try
	{
		FileDownloader.download( aUriToDownload, completeLocalPath );
		sb.append( " OK" );
	}
	catch ( IOException e )
	{
		// e.printStackTrace();
		sb.append( " ERROR " + e );
	}
	//Log.info( this, sb.toString() );
	System.out.println( sb.toString() );
}
private void updateMethod2( Spider.SpiderArgs aSpiderArgs, URL aUriToDownload, String aLocalPath )
{
	if ( aLocalPath.contains( "/s1600/" ) )
	{
		// Haal de pure filenaam eruit
		int slashIndex = aUriToDownload.getPath().lastIndexOf( "/" );
		aLocalPath = aUriToDownload.getPath().substring( slashIndex );

		String completeLocalPath = baseFileName + aLocalPath;

		// mkDirs( completeLocalPath );

		StringBuffer sb = new StringBuffer();
		for ( int x = 0; x < aSpiderArgs.depth; x++ )
		{
			sb.append( "  ");
		}
		sb.append( aUriToDownload ).append( " ==> " ).append( completeLocalPath );

		try
		{
			FileDownloader.download( aUriToDownload, completeLocalPath );
			sb.append( " OK" );
		}
		catch ( IOException e )
		{
			// e.printStackTrace();
			sb.append( " ERROR " + e );
		}
		//Log.info( this, sb.toString() );
		System.out.println( sb.toString() );
	}
}

private URL createImgPhpPath( URL aUri )
{
	try
	{
		return createImgPhpPath_1( aUri );
	}
	catch ( MalformedURLException e )
	{
		System.out.println( "ERROR reading " + aUri + ": " + e );
	}
	catch ( IOException e )
	{
		System.out.println( "ERROR malformed URL: " + aUri + ": " + e );
	}
	return null;
}
private URL createImgPhpPath_1( URL aUri ) throws MalformedURLException, IOException
{
	// aUri is een geheimzinnige html pagina. we moeten daarin het pad vinden naar de
	// echte file
	// En die &amp; moet eruit
	String uriString = aUri.toExternalForm();
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
					//return new URL( "http://" + aUri.getHost() + "/" + link );
					return new URL( aUri, link );
				}
			}
		}
	}
	return null;
}

/**
 * @param completePath
 */
private void mkDirs( String completePath )
{
	String dirs = completePath.substring( 0, completePath.lastIndexOf( '/' ) );

	File file = new File( dirs );
	file.mkdirs();
}


}
