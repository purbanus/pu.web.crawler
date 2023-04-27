package pu.web.crawler.simplepattern;

import nl.mediacenter.services.Range;

public class HeavyMetalDownloader extends AbstractSimplePatternDownloader
{
private static final String TARGET = "C:\\Users\\purba\\dwhelper\\heavymetal";

public static void main( String [] args )
{
	new HeavyMetalDownloader().run();
}

@Override
public Range getRange()
{
	return new Range( 77, 77 );
}
@Override
public String makeTarget( int aNumber )
{
	return TARGET + "/" + makeFileName( aNumber );
}

@Override
public String makeUrl( int aNumber )
{
	return "http://www.heavymetalmagazinefanpage.com/" + makeFileName( aNumber );
}
private String makeFileName( int aNumber )
{
	return "hmlist" + getFormat2().format( aNumber ) + ".html";
}
}
