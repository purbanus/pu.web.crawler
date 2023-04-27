package pu.web.crawler.simplepattern;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import nl.mediacenter.services.Range;

import pu.web.crawler.FileDownloader;
import pu.web.crawler.FileUtil;

public abstract class AbstractSimplePatternDownloader
{
private final NumberFormat format2 = new DecimalFormat( "00" );
private final NumberFormat format3 = new DecimalFormat( "000" );
private final String referrer;


public AbstractSimplePatternDownloader()
{
	this( null );
}

public AbstractSimplePatternDownloader( String aReferer )
{
	super();
	referrer = aReferer;
}

public NumberFormat getFormat2()
{
	return format2;
}
public NumberFormat getFormat3()
{
	return format3;
}

public void run()
{
	int min = getRange().from;
	int max = getRange().to;
	for ( int x = min; x <= max; x++ )
	{
		try
		{
			if ( x % 50 == 0 )
			{
				Thread.sleep( 10000 );
			}
			verwerk( x );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			return;
		}
	}
}
public abstract Range getRange();
public abstract String makeTarget( int aNumber );
public abstract String makeUrl( int aNumber);

private void verwerk( int aX ) throws IOException
{
	final String target = makeTarget( aX );
	final String url = makeUrl( aX );
	FileUtil.mkDirs( target );
	FileDownloader.download( url, target, referrer );
	System.out.println( target );
}

}
