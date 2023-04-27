package pu.web.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple but reusable HTML tag extractor.
 * It delivers the tags and their attributes, but not the content (nor the ending tags).
 * @author Ian Darwin, Darwin Open Systems, www.darwinsys.com.
 * @version $Id: TagReader.java,v 1.1 2006/10/26 19:02:23 purbanus Exp $
 */
// TODO Misschien een beetje structureren met een Tag class die een Map met attributes heeft
// TODO Echte Iterator van maken? Kun je filteren etc.
// HIGH Commentaar wordt geen rekening mee gehouden
public class TagReader
{
/** The Reader for this object */
private final BufferedReader reader;

/**
 *  Construct a TagReader given a URL String
 *  @throws IOException
 *  @throws MalformedURLException
 **/
public TagReader( String theURLString ) throws IOException, MalformedURLException
{
	this( new URL( theURLString ) );
}

/**
 * Construct a TagReader given a URL
 *  @throws IOException
 **/
public TagReader( URL theURL ) throws IOException
{
	super();
	reader = new BufferedReader(new InputStreamReader(theURL.openStream()));
}

/**
 * Get all tags in a List. When you read several large web-pages (or web pages with a lot of links)
 * at the same time this can use a lot of memory. If this is a concern, use a loop around nextTag().
 * @return All tags on a web page.
 * @throws IOException
 */
public List<String> getAllTags() throws IOException
{
	List<String> ret = new ArrayList<String>();
	String tag;
	while ((tag = nextTag()) != null)
	{
		ret.add( tag );
	}
	close();
	return ret;
}

/**
 * Read the next tag. This method is very memory-efficient because at any time
 * only a small portion (a few K) of the web page is in memory.
 * @throws IOException
 */
public String nextTag() throws IOException
{
	int i;
	while ((i = reader.read()) != -1)
	{
		char thisChar = (char) i;
		if (thisChar == '<')
		{
			String tag = readTag();
			return tag;
		}
	}
	return null;
}

/**
 * Read one tag. Adapted from code by Elliotte Rusty Harold
 * @throws IOException
 */
private String readTag() throws IOException
{
	StringBuffer theTag = new StringBuffer("<");
	int i = '<';

	while (i != '>' && (i = reader.read()) != -1)
	{
		// HIGH BUG: Je moet enters skippen
		if ( i != '\n' && i != '\r' )
		{
			theTag.append((char) i);
		}
	}
	return theTag.toString();
}

/**
 * Closes the internal streams
 * @throws IOException
 */
public void close() throws IOException
{
	reader.close();
}


/** Return a String representation of this object */
@Override
public String toString()
{
	// Er is geen zinvolle toString meer te verzinnen.
	//return "TagReader" + myURL.toString() + "]";
	return super.toString();
}
}
