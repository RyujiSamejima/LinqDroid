package jp.linqdroid.xml;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;
import jp.linqdroid.Enumerable;

public class XDocument extends XContainer {
	/** Root */
	protected XElement root;
	/** 属性 */
	protected List<XAttribute> namespaces;

	/**
	 * ファイルから XElement を読み込みます。
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static XDocument load(String fileName) throws FileNotFoundException {
		return XDocument.load(new FileInputStream(fileName));
	}
	public static XDocument load(URL url) throws IOException {
		return XDocument.load(url.openStream());
	}

	/**
	 * XML を格納した文字列から XElement を読み込みます。
	 * @param xmlString
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static XDocument parse(String xmlString) throws UnsupportedEncodingException {
		return XDocument.load(new ByteArrayInputStream(xmlString.getBytes("UTF-8")));
	}	

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static XDocument load(InputStream stream) {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(stream , "UTF-8");
			return XDocument.load(parser);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * XmlParser から XElement を読み込みます。
	 * @param parser
	 * @return
	 */
	public static XDocument load(XmlPullParser parser) {
		XDocument document = new XDocument();
		document.root = XElement.load(parser);
		return document;
	}
	
	public XDocument() {
		this.root = new XElement(new XName("Root"));
		this.nodes = new ArrayList<XNode>();
		this.namespaces = new ArrayList<XAttribute>();
	}

	public XElement getRoor() {
		return root;
	}
	@Override
	public void add(Object... objects) {
		this.root.add(objects);
	}

	@Override
	public void addFirst(Object... objects) {
		this.root.addFirst(objects);
	}

	@Override
	public Enumerable<XNode> getDescendantNodes() {
		return this.root.getDescendantNodes();
	}

	@Override
	public Enumerable<XElement> getDescendants() {
		return this.root.getDescendants();
	}

	@Override
	public Enumerable<XElement> getDescendants(XName name) {
		return this.root.getDescendants(name);
	}

	@Override
	public XElement getElement(XName name) {
		return this.root.getElement(name);
	}

	@Override
	public Enumerable<XElement> getElements() {
		return this.root.getElements();
	}

	@Override
	public Enumerable<XElement> getElements(XName name) {
		return this.root.getElements(name);
	}

	@Override
	public Enumerable<XNode> getNodes() {
		return this.root.getNodes();
	}

	@Override
	public void removeNodes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void replaceNodes(Object... contents) {
		this.root.replaceNodes(contents);
	}

	@Override
	public String toString() {
		return this.root.toString();
	}

	@Override
	public XNodeType getNodeType() {
		return XNodeType.Document;
	}

}
