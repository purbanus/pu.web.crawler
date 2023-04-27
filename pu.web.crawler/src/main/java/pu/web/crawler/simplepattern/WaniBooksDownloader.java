package pu.web.crawler.simplepattern;

import nl.mediacenter.services.Range;

public class WaniBooksDownloader extends AbstractSimplePatternDownloader
{

public static void main( String [] args )
{
	new WaniBooksDownloader().run();
}

public WaniBooksDownloader()
{
	super();
}

@Override
public Range getRange()
{
	return new Range( 201, 260 );
}

@Override
public String makeTarget( int aNumber )
{
	return "/home/purbanus/Pictures/new/japan/ai-shinozaki/ai-shinozaki-" + makeFileName( aNumber );
}

@Override
public String makeUrl( int aNumber )
{
	return "http://gallery0.com/lk35kd59kf986kg/beauty/asian_beauty/Ai_Shinozaki_11/" + makeFileName( aNumber );
}

private String makeFileName ( int aNumber )
{
	return getFormat3().format( aNumber ) + ".jpg";
}
}
