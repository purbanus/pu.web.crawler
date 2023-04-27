package pu.web.crawler;

import java.io.IOException;
import java.net.MalformedURLException;

public class CreateSiteOmdnl extends SpiderRunner
{
	public CreateSiteOmdnl( String aHtmlStart, String aTarget, int aDepth ) throws MalformedURLException
	{
		super( aHtmlStart, aTarget, aDepth );
	}

	public static void main(String[] args)
	{
	    final String HTML_START = "http://web09.local.web.mch/omdnl/index.html";
		final String TARGET  = "Q:\\Projecten\\110 OMD\\omdnl website";
		final int DEPTH = 999;
		try
		{
		    new CreateSiteOmdnl( HTML_START, TARGET, DEPTH );
		}
		catch ( IOException e )
		{
		    //Log.error( SpiderRunner.class, e );
			e.printStackTrace();
		}
		//Log.info( SpiderRunner.class, "Finished" );
		System.out.println( "Finished" );		
	}
}
