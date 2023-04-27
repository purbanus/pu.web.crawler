package pu.web.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

/**
 * TODO
 * resumed hierheen halen:
 * - een setResumed methode is dan eleganter
 * - dus niet meteen de spider runnen maar een run() methode maken
 * - kun je meteen het aantal ctorparms verminderen
 */
public abstract class AbstractSpiderRunner implements Observer
{
private final String baseFileName;
private int wachtTeller = 0;

public AbstractSpiderRunner( String aUrlString, String aBaseFileName, int aDepth ) throws MalformedURLException
{
	super();
	baseFileName = aBaseFileName;
	URL url = new URL( HtmlUtil.adjustIfDir( aUrlString ) );
	Spider spider = new Spider( url, aDepth );
	spider.addObserver(this);
	spider.setAcceptDifferentHosts( false );
	spider.run();
}
/**
 * This method is called immediately whenever the spider
 * discovers a new URL.  It should return as quickly as
 * possible since it is holding up the spider.
 */
@Override
public void update( Observable o, Object arg )
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

	if ( ++wachtTeller % 50 == 0 )
	{
		try
		{
			Thread.sleep( 10000 );
		}
		catch ( InterruptedException e )
		{
			e.printStackTrace();
		}
	}
}
/**
 * Create the URI that we're actually going to download.
 * Default is the URI from the spider. This method is meant to be overridden if you need to
 * manipulate this URI
 * @param aUri
 * @return
 */
protected URL constructUriToDownload( URL aUri )
{
	return aUri;
}
protected String constructLocalPath( URL aUri, URL aUriToDownload )
{
	return aUriToDownload.getPath();
}
protected abstract boolean shouldProcess( String aLocalPath );


}
