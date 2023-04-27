package pu.web.crawler;

import java.io.IOException;
import java.net.MalformedURLException;


/**
 * Runs the Spider
 */
class SpiderRunner extends AbstractSpiderRunner
{
private static final String RESUME_NAME = null;
//private static final String EXCLUDE_NAME = "null";
//private static final String INCLUDE_NAME = "null";
private static final String EXCLUDE_NAME = null;
private static final String INCLUDE_NAME = null;
@SuppressWarnings( "unused" )
private boolean resumed = false;

public SpiderRunner( String aUrlString, String aBaseFileName, int aDepth ) throws MalformedURLException
{
	super( aUrlString, aBaseFileName, aDepth );
	// @@NOG dit werkt niet, we zijn al klaar als we hier komen
	//       resume-mechanosme in base inbouwen
	if ( RESUME_NAME == null )
	{
		resumed = true;
	}
}

@SuppressWarnings( "unused" )
public static void main(String[] args)
{
	//final String HTML_START = "http://freshpics.blogspot.com/search/label/Babes";
	//final String HTML_START = "http://ftvgirls.bulletinboardforum.com/carli/";
	//final String HTML_START = "http://freshpics.blogspot.com/2008/04/sexy-nfl-cheerleaders.html";
	//final String HTML_START = "http://greenshines.com/wp-content/gallery/";
	//final String HTML_START = "http://www.asianjoke.com/beautiful_asian_girls.htm";
	//final String TARGET  = "/home/purbanus/Pictures/vrouwen/asianjokes.com";

	// Om de een of andere reden geeft dit 0 bytes of zo.
	//final String HTML_START = "http://commons.wikimedia.org/wiki/Category:Maps_of_polders_in_the_Netherlands_drawn_by_W.H._Hoekwater";
	
	final String HTML_START = "file:///C:/temp/kaarten/Category%20Maps%20of%20polders%20in%20the%20Netherlands%20drawn%20by%20W.H.%20Hoekwater%20-%20Wikimedia%20Commons.html";
	final String TARGET  = "c:/temp/kaarten/polderkaarten van W.H. Hoekwater 1901/";

	final int DEPTH = 1;
	try
	{
		new SpiderRunner( HTML_START, TARGET, DEPTH );
	}
	catch ( IOException e )
	{
		//Log.error( SpiderRunner.class, e );
		e.printStackTrace();
	}
	//Log.info( SpiderRunner.class, "Finished" );
	System.out.println( "Finished" );
}

@Override
protected boolean shouldProcess( String localPath )
{
	if ( EXCLUDE_NAME != null && localPath.contains( EXCLUDE_NAME ) )
	{
		return false;
	}
	if ( INCLUDE_NAME != null && ! localPath.contains( INCLUDE_NAME ) )
	{
		return false;
	}
	// Dit is fout, wat is de bedoeling???
//	if ( ! resumed && RESUME_NAME != null && localPath.contains( RESUME_NAME ) )
//	{
//		resumed = true;
//	}
//	if ( ! resumed )
//	{
//		return false;
//	}
	return true;
}


}
