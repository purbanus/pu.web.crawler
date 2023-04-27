package pu.web.crawler;

import java.io.IOException;
import java.net.URL;

import nl.mediacenter.services.StringHelper;

/**
 * Html utilities
 */
public class HtmlUtil
{

/**
 * No instances allowed
 */
private HtmlUtil()
{
	super();
}

/**
 * This method implements a heuristic for URLs that are probably
 * directories.  If the last component of the URL does not contain
 * a dot and does not end with a "/", then it is explicitly
 * converted to a directory by appending a "/".
 *
 * @param s
 * @return
 */
public static String adjustIfDir(String s)
{
	int p = s.lastIndexOf("/") + 1;

	if (!s.endsWith("/") && s.indexOf(".", p) < 0)
	{
		s += "/";
	}
	return s;
}

/**
 * This method implements a heuristic for URLs that are probably
 * directories.  It returns <code>true</code> if the URL ends with a
 * &quot;/&quot; or if the last component of the URL does not contain
 * a dot.
 *
 * @param s
 * @return
 */
public static boolean isProbablyDir( String s )
{
	if ( s.endsWith( "/" ) )
	{
		return true;
	}
	int p = s.lastIndexOf("/") + 1;

	return s.indexOf( ".", p ) < 0;
}

public static String createSafeFileName( String s )
{
	final String [] FORBIDDEN = new String []
			{
		":", "_",
		"/", "_",
		">", "_",
		"<", "_",
		"\\", "_",

		// Of:
		/*********
		":", "_COLON_",
		"/", "_SLASH_",
		">", "_GT_",
		"<", "_LT_",
		"\\", "_BACKSLASH_",
		 *********/
			};
	String ret = s;
	for ( int x = 0; x < FORBIDDEN.length; x += 2 )
	{
		ret = StringHelper.replaceAll( ret, FORBIDDEN[x], FORBIDDEN[x+1] );
	}
	return ret;
}

/**
 * Extracts the <a> tag from s and then returns the remainder of
 * the line.
 *
 * @param s The raw string
 * @param p Position in the string to start searching
 * @param attribute The attribute to search for, i.e. href or src
 * @return The extracted link, or <code>null</code> if no link was found
 * @throws IOException
 */

public static String extractLink( String s, int p, String attribute)
{
	// Find the href attribute.
	String lc = s.toLowerCase();
	p = lc.indexOf( attribute, p );
	if ( p < 0 )
	{
		// No href so skip the tag.
		return null;
	}

	// Skip the "href=" or "src="
	p += attribute.length();
	int q = -1;

	// BUG: Hier mag whitespace staan
	//if (s.charAt(p) == '"')
	//{
	//	p++;
	//	q = s.indexOf('"', p);
	//}
	// BUG: Je mag ' of " gebruiken
	int startQuote1 = s.indexOf( '"', p );
	int startQuote2 = s.indexOf( '\'', p );
	if ( startQuote1 < 0 && startQuote2 < 0 )
	{
		// No quote
		q = s.indexOf(' ', p);
		int q2 = s.indexOf('>', p);
		if (Math.min(q, q2) < 0 && Math.max(q, q2) >= 0)
		{
			// If one is > 0 and the other < 0, use the > 0 one.
			q = Math.max(q, q2);
		}

		// Use the smaller of the two.
		q = Math.min(q, q2);
	}
	else if ( startQuote2 < 0 )
	{
		p = startQuote1 + 1;
		q = s.indexOf('"', p);
	}
	else if ( startQuote1 < 0 )
	{
		p = startQuote2 + 1;
		q = s.indexOf('\'', p);
	}
	else
	{
		p = 1 + Math.min( startQuote1, startQuote2 );
		q = s.indexOf( s.charAt( p - 1 ), p );
	}

	// Could not complete the href tag for some reason
	// so skip the tag.
	if (q < 0)
	{
		return null;
	}
	s = s.substring(p, q);

	// Remove the reference, if any.
	p = s.indexOf('#');
	if (p == 0)
	{
		return null;
	}
	else if (p > 0)
	{
		s = s.substring(0, p);
	}
	return s;
}

/**
 * Returns the port number of 'url'.  If the port number is
 * not defined, returns the default HTTP port number.
 *
 * @param url
 * @return
 */
static int getPort( URL uri )
{
	return uri.getPort() == -1 ? 80 : uri.getPort();
}

public static boolean hasAttribute( String aTag, String aAttribute )
{
	return getAttributeIndex( aTag, aAttribute ) >= 0;
}

public static String extractAttribute( String aTag, String aAttribute )
{
	int leftIndex = getAttributeIndex( aTag, aAttribute ) + aAttribute.length() + 1;
	if ( aTag.charAt( leftIndex ) == '"' )
	{
		int rQuoteIndex = aTag.indexOf( "\"", leftIndex + 1 );
		if ( rQuoteIndex < 0 )
		{
			return null;
		}
		return aTag.substring( leftIndex + 1, rQuoteIndex );
	}
	else
	{
		// Als het zonder quotes is, dan geldt whitespace of > als einde
		int rightIndex = -31415;
		for ( int x = leftIndex; x < aTag.length(); x++ )
		{
			char c = aTag.charAt( x );
			if ( c == '>' || Character.isWhitespace( c ) )
			{
				rightIndex = x;
				break;
			}
		}
		if ( rightIndex < 0 )
		{
			return null;
		}
		return aTag.substring( leftIndex, rightIndex );
	}
}

public static int getAttributeIndex( String aTag, String aAttribute )
{
	String lTag = aTag.toLowerCase();
	String lAttribute = aAttribute.toLowerCase() + "=";
	return lTag.indexOf( lAttribute );
}

}
