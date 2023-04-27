package pu.web.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;


/**
 * Runs the Spider
 */
class PictureSpiderRunner extends AbstractSpiderRunner
{
private int counter = 0;
private Format fourDigitNumberFormat = new DecimalFormat( "0000" );

public PictureSpiderRunner( String aUrlString, String aBaseFileName, int aDepth ) throws MalformedURLException
{
	super( aUrlString, aBaseFileName, aDepth );
}

@SuppressWarnings( "unused" )
public static void main(String[] args)
{
	//final String HTML_START = "http://freshpics.blogspot.com/search/label/Babes";
	//final String HTML_START = "http://ftvgirls.bulletinboardforum.com/carli/index.html";
	//final String HTML_START = "http://justteensite.bulletinboardforum.com/";
	//final String HTML_START = "http://www.bulletinboardforum.com/photos/Tight_and_Toned_Brunette.html";
	final String HTML_START = "http://ann-angel.bulletinboardforum.com/";
	final String TARGET  = "c:/temp/crawler/ftv/ann-angel";
	final int DEPTH = 1;
	try
	{
		new PictureSpiderRunner( HTML_START, TARGET, DEPTH );
	}
	catch ( IOException e )
	{
		e.printStackTrace();
	}
	System.out.println( "Finished" );
}

// This method is called immediately whenever the spider
// discovers a new URL.  It should return as quickly as
// possible since it is holding up the spider.
/**************************
public void update(Observable o, Object arg)
{
	Spider.SpiderArgs spiderArgs = (Spider.SpiderArgs) arg;

	URL uri = spiderArgs.linkedUri;
	if ( HtmlUtil.isProbablyDir( uri.getPath() ) )
	{
		return;
	}
	URL uriToDownload = constructUriToDownload( uri );
	String localPath = baseFileName + constructLocalPath( uri, uriToDownload );
	if ( ! shouldProcess( localPath ) )
	{
		return;
	}

	FileUtil.mkDirs( localPath );

	StringBuffer sb = new StringBuffer();
	for ( int x = 0; x < spiderArgs.depth; x++ )
	{
		sb.append( "  ");
	}
	sb.append( uriToDownload ).append( " ==> " ).append( localPath );

	try
	{
		FileDownloader.download( uriToDownload, localPath );
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
 *************************************/
@Override
protected URL constructUriToDownload( URL aOriginalUri )
{
	if ( aOriginalUri.getPath().endsWith( "img.php" ) )
	{
		URL newUri = createImgPhpPath( aOriginalUri );
		if ( newUri != null )
		{
			return newUri;
		}
	}
	return aOriginalUri;
}
@Override
protected String constructLocalPath( URL aOriginalUri, URL aUriToDownload )
{
	if ( aOriginalUri.getPath().endsWith( "img.php" ) )
	{
		if ( ! aOriginalUri.getPath().equals( aUriToDownload.getPath() ) )
		{
			String number = fourDigitNumberFormat.format( ++counter );
			return aUriToDownload.getPath() + "." + number + ".jpg";
		}
	}
	return aUriToDownload.getPath();
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

@Override
protected boolean shouldProcess( String aLocalPath )
{
	return true;
}
}
