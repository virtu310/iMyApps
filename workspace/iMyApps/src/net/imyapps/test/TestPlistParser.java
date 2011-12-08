package net.imyapps.test;

import static org.junit.Assert.*;

import java.io.InputStream;
import junit.framework.Assert;
import net.imyapps.common.AppItem;
import net.imyapps.utils.ISO8601DateParser;
import net.imyapps.utils.IpaXMLParser;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestPlistParser {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testParse() throws Exception {
		//fail("Not yet implemented");
		InputStream input = getClass().getResourceAsStream("iTunesMetadata2.plist");
		IpaXMLParser parser = new IpaXMLParser();
		
		parser.parse(input);
		
		Assert.assertEquals(2, parser.getItems().size());
		
		AppItem item = parser.getItems().get(0);
		
		Assert.assertEquals("iOS", item.getPlatform());
		Assert.assertEquals("sample@sample.org", item.getBuyerId());
		Assert.assertEquals("304219585", item.getArtistId());
		Assert.assertEquals("Skype Software S.a.r.l", item.getArtistName());
		Assert.assertEquals("© (c) 2003 - 2010 Skype Limited, " +
				"Patents Pending. Skype, " +
				"associated trade marks and logos and the \"S\" symbol are " +
				"trade marks of Skype Limited", item.getCopyright());
		Assert.assertEquals("Social Networking", item.getGenre());
		Assert.assertEquals("6005", item.getGenreId());
		Assert.assertEquals("304878510", item.getItemId());
		Assert.assertEquals("304878510", item.getItemId());
		Assert.assertEquals("Skype", item.getItemName());
		Assert.assertEquals("software", item.getKind());
		Assert.assertEquals("Skype Software S.a.r.l", 
				item.getPlaylistArtistName());
		Assert.assertEquals("Skype", item.getPlaylistName());
		Assert.assertEquals("0", item.getPrice());
		Assert.assertEquals("Free", item.getPriceDisplay());
		Assert.assertEquals(ISO8601DateParser.parse("2011-01-08T09:18:11Z").getTime(), 
				item.getPurchaseDate().longValue());
		Assert.assertEquals(ISO8601DateParser.parse("2009-03-31T07:00:00Z").getTime(), 
						item.getReleaseDate().longValue());
		Assert.assertEquals("http://a1.phobos.apple.com/us/r1000/048/Purple/" +
				"71/9e/ef/mzi.wtkrwnpm.png", item.getSoftwareIcon57x57URL());
		Assert.assertEquals("1", item.getSoftwareSupportedDeviceIds());
		Assert.assertEquals("com.skype.skype", 
				item.getSoftwareVersionBundleId());
		Assert.assertEquals("3253914", 
				item.getSoftwareVersionExternalIdentifier());
		Assert.assertEquals("1466803,1529132,1602608,1651681,1750461,1930253," + 
				"1961532,1973932,2026202,2526384,2641622,2703653,2890413," + 
				"3024489,3253914", 
				item.getSoftwareVersionExternalIdentifiers());
		Assert.assertEquals("30244", item.getVendorId());
		Assert.assertEquals("16843008", item.getVersionRestrictions());
	}

}
