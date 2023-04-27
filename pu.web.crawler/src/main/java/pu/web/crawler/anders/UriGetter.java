package pu.web.crawler.anders;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import pu.web.crawler.TagReader;

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
	ArrayList<String> urls = ug.getUris();
	for ( String url : urls )
	{
		System.out.println( url );
	}
}

public ArrayList<String> getUris() throws IOException
{
	ArrayList<String> al = new ArrayList<String>();
	String tag;
	while ((tag = reader.nextTag()) != null)
	{
		for ( String wantTag : wantTags )
		{
			if (tag.startsWith(wantTag))
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
	{
		reader.close();
	}
}
}
