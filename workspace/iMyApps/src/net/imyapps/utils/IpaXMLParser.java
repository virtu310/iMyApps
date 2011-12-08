package net.imyapps.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.imyapps.common.AppItem;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class IpaXMLParser {
	List<AppItem> items = new ArrayList<AppItem>();
	
	public List<AppItem> getItems() {
		return items;
	}
	
	public void parse(String xml, String charset) throws Exception {
		parse(new ByteArrayInputStream(xml.getBytes(charset)));
	}
	
	public void parse(InputStream input) throws Exception {
		DocumentBuilder builder;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		builder = factory.newDocumentBuilder();
		Document document = builder.parse(input);
		parseRoot(document);
	}
	
	private void parseRoot(Node node) throws DOMException, ParseException {
		NodeList childNodes = node.getChildNodes();
		Node node2;
		Node node3;
		
		for(int i=0; i < childNodes.getLength(); i++) {
			node2 = childNodes.item(i);
			if(node2.getNodeName().equals("plist") &&
			   node2.hasChildNodes()) {
				NodeList childNodes2 = node2.getChildNodes();
				for (int j=0; j<childNodes2.getLength(); j++) {
					node3 = childNodes2.item(j);
					if (node3.getNodeName().equals("dict")) {
						parseDict(node3);
					}
				}
			}
		}
	}
	
	public void parseDict(Node node) throws DOMException, ParseException {
		NodeList childNodes = node.getChildNodes();
		Node key;
		Node value;
		AppItem item = new AppItem();
		StringBuilder sb = new StringBuilder();
		String nodeValue;
		
		for(int i=0; i < childNodes.getLength(); i++) {
			key = childNodes.item(i);
			if(key.getNodeName().equals("key")) {
			   nodeValue = key.getFirstChild().getNodeValue();
			   if (nodeValue.equals("appleId")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setBuyerId(value.getFirstChild().getNodeValue());
			   }
			   else if (nodeValue.equals("artistId")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setArtistId(value.getFirstChild().getNodeValue());
			   }
			   else if (nodeValue.equals("artistName")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setArtistName(value.getFirstChild().getNodeValue());
			   }
			   else if (nodeValue.equals("copyright")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setCopyright(value.getFirstChild().getNodeValue());
			   }
			   else if (nodeValue.equals("genre")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setGenre(value.getFirstChild().getNodeValue());
			   }
			   else if (nodeValue.equals("genreId")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setGenreId(value.getFirstChild().getNodeValue());
			   }
			   else if (nodeValue.equals("itemId")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setItemId(value.getFirstChild().getNodeValue());
			   }
			   else if (nodeValue.equals("itemName")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setItemName(value.getFirstChild().getNodeValue());
			   }
			   else if (nodeValue.equals("kind")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setKind(value.getFirstChild().getNodeValue());
			   }
			   else if (nodeValue.equals("playlistArtistName")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setPlaylistArtistName(value.getFirstChild().getNodeValue());
			   }
			   else if (nodeValue.equals("playlistName")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setPlaylistName(value.getFirstChild().getNodeValue());
			   }
			   else if (nodeValue.equals("price")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setPrice(value.getFirstChild().getNodeValue());
			   }
			   else if (nodeValue.equals("priceDisplay")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setPriceDisplay(value.getFirstChild().getNodeValue());
			   }
			   else if (nodeValue.equals("purchaseDate")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setPurchaseDate(ISO8601DateParser.parse(
								   value.getFirstChild().getNodeValue()).getTime());
			   }
			   else if (nodeValue.equals("releaseDate")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setReleaseDate(ISO8601DateParser.parse(
								   value.getFirstChild().getNodeValue()).getTime());
			   }
			   else if (nodeValue.equals("softwareIcon57x57URL")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setSoftwareIcon57x57URL(value.getFirstChild().getNodeValue());
			   }
			   else if (nodeValue.equals("softwareSupportedDeviceIds")) {
				   i += 2;
				   Node array = childNodes.item(i);
				   NodeList list = array.getChildNodes();
				   
				   sb.setLength(0);
				   for (int j=0; j<list.getLength(); j++) {
					   value = list.item(++j);
					   if (value == null) continue;
					   if (j > 1)
						   sb.append(',');
					   sb.append(value.getFirstChild().getNodeValue());
				   }
				   item.setSoftwareSupportedDeviceIds(sb.toString());
			   }
			   else if (nodeValue.equals("softwareVersionBundleId")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setSoftwareVersionBundleId(value.getFirstChild().getNodeValue());
			   }
			   else if (nodeValue.equals("softwareVersionExternalIdentifier")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setSoftwareVersionExternalIdentifier(
												value.getFirstChild().getNodeValue());
			   }
			   else if (nodeValue.equals("softwareVersionExternalIdentifiers")) {
				   i += 2;
				   Node array = childNodes.item(i);
				   NodeList list = array.getChildNodes();
				   
				   sb.setLength(0);
				   for (int j=0; j<list.getLength(); j++) {
					   value = list.item(++j);
					   if (value == null) continue;
					   if (j > 1)
						   sb.append(',');
					   sb.append(value.getFirstChild().getNodeValue());
				   }
				   item.setSoftwareVersionExternalIdentifiers(sb.toString());
			   }
			   else if (nodeValue.equals("vendorId")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setVendorId(value.getFirstChild().getNodeValue());
			   }
			   else if (nodeValue.equals("versionRestrictions")) {
				   i += 2;
				   value = childNodes.item(i);
				   item.setVersionRestrictions(value.getFirstChild().getNodeValue());
			   }
			   // to parse binary plist file
			   else if (nodeValue.equals("com.apple.iTunesStore.downloadInfo")) {
				   i += 2;
				   Node downloadInfo = childNodes.item(i);
				   value = findNode(downloadInfo, "accountInfo");
				   value = findNode(value, "AppleID");
				   item.setBuyerId(value.getFirstChild().getNodeValue());
				   value = findNode(downloadInfo, "purchaseDate");
				   item.setPurchaseDate(ISO8601DateParser.parse(
						   value.getFirstChild().getNodeValue()).getTime());
				   System.out.println(value);
			   }
			}
		}
		
		item.setPlatform(AppItem.PLATFORM_IOS);
		items.add(item);
	}

	public Node findNode(Node node, String keyName) {
		NodeList nodes = node.getChildNodes();
		Node key;
		String nodeValue;
		
		for(int i=0; i < nodes.getLength(); i++) {
			key = nodes.item(i);
			if(key.getNodeName().equals("key")) {
				nodeValue = key.getFirstChild().getNodeValue();
				if (nodeValue.equals(keyName)) {
					i += 2;
					return nodes.item(i);
				}
			}
		}
		
		return null;
	}
}
