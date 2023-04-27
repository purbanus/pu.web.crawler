/*
 * Created on 14-dec-04
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package pu.web.crawler.anders;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pu.web.crawler.FileDownloader;
import pu.web.crawler.HtmlUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Crawler
{
private final Set<String> visited = new HashSet<String>();
private final String target;
private final String htmlStart;
private final int maxDepth;

/**
 *
 */
public Crawler( String aHtmlStart, String aTarget, int aMaxDepth )
{
	super();
	htmlStart = aHtmlStart;
	target = aTarget;
	maxDepth = aMaxDepth;
}

public static void main(String[] args)
{
	final String TARGET = "c:\\temp\\crawler\\pu";
	//final String HTML_START = "http://lok-dbrs.pu2005/do-login.xxx?user=Alles&password=tjeeminee15";
	final String HTML_START = "http://localhost";
	final int DEPTH = 2;
	try
	{
		new Crawler( HTML_START, TARGET, DEPTH ).run();
	}
	catch (MalformedURLException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	catch (IOException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
void run() throws MalformedURLException, IOException
{
	run( getHtmlStart(), 0 );
}
private void run( String aUri, int aDepth ) throws MalformedURLException, IOException
{
	// Store de file
	String toPath = getTarget() + "\\" + HtmlUtil.createSafeFileName( aUri );
	FileDownloader.download( aUri, toPath );
	if ( aDepth >= getMaxDepth() )
	{
		return;
	}

	// Haal de links
	List<String> links = new UriGetter( aUri ).getUris();
	for ( String linkTag : links )
	{
		// HIGH Waarom lowercase??????
		linkTag = linkTag.toLowerCase();
		String uri = extractUrl( linkTag );
		uri = discardLocal( uri );
		if ( ! getVisited().contains( uri ) )
		{
			getVisited().add( uri );
			run( uri, maxDepth + 1 );
		}
	}
}

private String discardLocal( String aUrl )
{
	int hekje = aUrl.indexOf( "#" );
	if ( hekje < 0 )
	{
		return aUrl;
	}
	return aUrl.substring(0, hekje );
}

private String extractUrl( String aLink )
{
	// find href
	// HIGH Ook src
	int href = aLink.indexOf( "href" );
	if ( href < 0 )
	{
		System.out.println( "Vreemd, geen href! Link=" + aLink );
		return null;
	}
	//find =
	int equals = aLink.indexOf( '=', href + 1 );
	if ( equals < 0 )
	{
		System.out.println( "Vreemd, geen = na href! Link=" + aLink );
		return null;
	}

	// find quotes
	int firstQuote = aLink.indexOf( '"', equals + 1 );
	int lastQuote;
	if ( firstQuote < 0 )
	{
		System.out.println( "Vreemd, geen quote na href=! Link=" + aLink );

		// Poging om de situatie te redden
		lastQuote = aLink.indexOf( ' ', equals + 1 );
		if ( lastQuote < 0 )
		{
			lastQuote = aLink.indexOf( '>', equals + 1 );
			if ( lastQuote < 0 )
			{
				// Dan geven we het op
				System.out.println( "Vreemd, geen spatie of > na href=! Link=" + aLink );
				return null;
			}
		}
	}
	else
	{
		lastQuote  = aLink.indexOf( '"', firstQuote + 1 );
		if ( lastQuote < 0 )
		{
			System.out.println( "Vreemd, geen eindQuote! Link=" + aLink );
			return null;
		}
	}
	// Onze link zit nu tussen de quotes
	return aLink.substring( firstQuote + 1, lastQuote ).trim();
}

/**
 * @return
 */
public String getTarget()
{
	return target;
}

/**
 * @return
 */
public Set<String> getVisited()
{
	return visited;
}

private int getMaxDepth()
{
	return maxDepth;
}
private String getHtmlStart()
{
	return htmlStart;
}

}
