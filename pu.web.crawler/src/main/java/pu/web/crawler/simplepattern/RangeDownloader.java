package pu.web.crawler.simplepattern;

import nl.mediacenter.services.Range;

public class RangeDownloader extends AbstractSimplePatternDownloader
{
private static final String URL_BASE = "http://content5.babesandgirls.com/met-art/0247/";
private static final String REFERRER = "http://www.babesandgirls.com/kira-k";
//URL_BASE + "00.jpg";

public static void main( String [] args )
{
	new RangeDownloader().run();
}

public RangeDownloader()
{
	super( REFERRER );
}

@Override
public Range getRange()
{
	return new Range( 0, 20 );
}

@Override
public String makeTarget( int aNumber )
{
	return "/home/purbanus/Pictures/vrouwen/sex/namen/kira-k/" + makeFileName( aNumber );
}

@Override
public String makeUrl( int aNumber )
{
	return URL_BASE + makeFileName( aNumber );
}

private String makeFileName ( int aNumber )
{
	return getFormat2().format( aNumber ) + ".jpg";
}
}
