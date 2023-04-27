package pu.web.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
class Spider extends Observable implements Runnable
{
/* The tags we want to look at */
public static final LinkTag [] wantTags =
{
	new LinkTag( "<a "     , "href=" ),
	new LinkTag( "<applet ", "src="  ),
	new LinkTag( "<img "   , "src="  ),
	new LinkTag( "<frame " , "src="  ),
	new LinkTag( "<link "  , "href=" ),
};
private static final String [] FILETYPES_NOT_TO_BE_SEARCHED_FOR_TAGS = new String [] { ".jpg", ".jpeg", ".pdf", ".gif", ".png" };

// HIGH Moet dit niet een global set zijn? Als iets gevisiteerd wordt in een andere Spider moet
// het ook als reeds bezocht gelden.
private final Set<URL> walked = new HashSet<URL>();
private final int maxDepth;
private final URL homeURL;

private boolean acceptDifferentHosts = false;
private boolean acceptDifferentPorts = true;

public static class SpiderArgs
{
public final URL baseUri;
public final URL linkedUri;
public final int depth;

SpiderArgs( URL aBaseUri, URL aLinkedUri, int aDepth )
{
	baseUri = aBaseUri;
	linkedUri = aLinkedUri;
	depth = aDepth;
}
}
public static class LinkTag
{
public final String tag;
public final String href;
public LinkTag( String aTag, String aHref )
{
	super();
	tag = aTag;
	href = aHref;
}
}

public Spider( URL url, int depth )
{
	homeURL = url;
	maxDepth = depth;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Getters en setters

public boolean isAcceptDifferentHosts()
{
	return acceptDifferentHosts;
}

public void setAcceptDifferentHosts( boolean aAcceptDifferentHosts )
{
	acceptDifferentHosts = aAcceptDifferentHosts;
}

public boolean isAcceptDifferentPorts()
{
	return acceptDifferentPorts;
}

public void setAcceptDifferentPorts( boolean aAcceptDifferentPorts )
{
	acceptDifferentPorts = aAcceptDifferentPorts;
}

public Set<URL> getWalked()
{
	return walked;
}

/**
 * Returns the maximum depth to which links are followed.
 * <ul>
 * <li>0 = only visit the starting URL and don't follow any links
 * <li>1 = Visit the starting URL and all the pages that this page references
 * <li>2 = Visit the starting URL and all the pages that this page references, and all
 *         the pages that those pages reference.
 * <li>Etcetera
 * </ul>
 * @return the maximum depth
 */
public int getMaxDepth()
{
	return maxDepth;
}

public URL getHomeURL()
{
	return homeURL;
}


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Run / walk

@Override
public void run()
{
	visitPage( new SpiderArgs( getHomeURL(), getHomeURL(), 0 ) );
	if ( getMaxDepth() > 0 )
	{
		walk( getHomeURL(), 1 );
	}
}

private void walk( URL aBaseUri, int curDepth )// throws IOException
{
	List<String> links;
	try
	{
		links = findLinks(aBaseUri);
	}
	catch ( IOException e1 )
	{
		// Kan wellicht handig zijn
		//System.out.println("*** " + baseUrl + " -> " + ur);
		System.out.println("*** IOException getting " + aBaseUri );
		return;
	}

	for ( String link : links )
	{
		URL linkedUri;
		try
		{
			linkedUri = new URL( aBaseUri, link );
		}
		catch (MalformedURLException e)
		{
			System.out.println( "*** MalformedURLException " + aBaseUri + " --> " + link );
			return;
		}
		if ( accept( linkedUri ) )
		{
			visitPage( new SpiderArgs( aBaseUri, linkedUri, curDepth ) );
			if ( curDepth < getMaxDepth() )
			{
				walk( linkedUri, curDepth + 1 );
			}

		}
	}
}

private void visitPage( SpiderArgs spiderArgs )
{
	getWalked().add( spiderArgs.linkedUri );
	setChanged();
	notifyObservers( spiderArgs );
}

/**
 * Finds all the links in 'url' and returns them in a List.
 */
private List<String> findLinks( URL aUrl ) throws IOException
{
	List<String> ret = new ArrayList<String>();
	for ( String element : FILETYPES_NOT_TO_BE_SEARCHED_FOR_TAGS )
	{
		if ( aUrl.getPath().endsWith( element ) )
		{
			return ret;
		}
	}
	List<String> tags = new TagReader( aUrl ).getAllTags();
	for ( String tag : tags )
	{
		String tagLC = tag.toLowerCase();
		int p = -1;
		for ( LinkTag wantTag : wantTags )
		{
			p = tagLC.indexOf( wantTag.tag );
			if ( p >= 0 )
			{
				String link = HtmlUtil.extractLink( tag, p, wantTag.href );
				if ( link != null )
				{
					ret.add( link );
				}
				break;
			}
		}
	}
	return ret;
}

private boolean accept( URL aUrl )
{
	if ( walked.contains( aUrl ) )
	{
		return false;
	}
	if ( ! ( aUrl.getProtocol().equals( "http" ) || aUrl.getProtocol().equals( "https" ) || aUrl.getProtocol().equals( "file" ) ) )
	{
		return false;
	}
	if ( ! isAcceptDifferentHosts() && ! getHomeURL().getHost().equals( aUrl.getHost() ) )
	{
		return false;
	}
	if ( ! isAcceptDifferentPorts() && HtmlUtil.getPort( getHomeURL() ) != HtmlUtil.getPort( aUrl ) )
	{
		return false;
	}
	return true;
}

}
