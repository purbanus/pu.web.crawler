package pu.web.crawler.simplepattern;

import nl.mediacenter.services.Range;

public class ExtremelyFapWorthyPicturesDownloader extends AbstractSimplePatternDownloader
{
private static final String REFERRER = "http://www.cavemancircus.com/galleries2/2010/6/extremely_fap_worthy_pictures/extremely_fap_worthy_pictures_001.jpg";

public static void main( String [] args )
{
	new ExtremelyFapWorthyPicturesDownloader().run();
}

public ExtremelyFapWorthyPicturesDownloader()
{
	super( REFERRER );
}

@Override
public Range getRange()
{
	return new Range( 1, 100 );
}

@Override
public String makeTarget( int aNumber )
{
	return "/home/purbanus/Pictures/new/extremely-fap-worthy-pictures/" + makeFileName( aNumber );
}

@Override
public String makeUrl( int aNumber )
{
	return "http://www.cavemancircus.com/galleries2/2010/6/extremely_fap_worthy_pictures/extremely_fap_worthy_pictures_" + makeFileName( aNumber );
}

private String makeFileName ( int aNumber )
{
	return getFormat3().format( aNumber ) + ".jpg";
}
}
