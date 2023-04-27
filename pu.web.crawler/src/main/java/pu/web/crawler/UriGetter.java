package pu.web.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Extract the links from a html page.
 */
// HIGH Waarom doe ik niet een toLower()?
// TODO Als de TagReader een Iterator is dan kan dit een filter worden
// HIGH Dit get nog helemaal geen uris, alleen tags die mogelijk een uri bevatten
public class UriGetter
{
	/** The tags we want to look at */
	public static final String[] wantTags =
	{ 
		"<a ", 
		"<A ",
		"<applet ", 
		"<APPLET ", 
		"<img ", 
		"<IMG ", 
		"<frame ", 
		"<FRAME ",
		"<link ", 
		"<LINK ", 
	};
	
	/** The tag reader */
	TagReader reader;

public UriGetter(URL theURL) throws IOException
{
	reader = new TagReader(theURL);
}

public UriGetter(String theURL) throws MalformedURLException, IOException
{
	reader = new TagReader(theURL);
}

public static void main(String[] argv) throws MalformedURLException, IOException
{
	String theURL = argv.length == 0 ? "http://localhost/" : argv[0];
	UriGetter ug = new UriGetter(theURL);
	List<String> urls = ug.getUris();
	Iterator<String> urlIterator = urls.iterator();
	while (urlIterator.hasNext())
	{
		System.out.println(urlIterator.next());
	}
}

public List<String> getUris() throws IOException
{
	List<String> al = new ArrayList<>();
	String tag;
	while ((tag = reader.nextTag()) != null)
	{
		for (int i = 0; i < wantTags.length; i++)
		{
			if (tag.startsWith(wantTags[i]))
			{
				al.add(tag);
				continue; // optimization
			}
		}
	}
	return al;
}

public void close() throws IOException
{
	if (reader != null)
		reader.close();
}
}
