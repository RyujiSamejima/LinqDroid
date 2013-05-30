package jp.linqdroid.parser;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Iterator;

import jp.linqdroid.Disposable;
import jp.linqdroid.Enumerable;
import jp.linqdroid.F;
import jp.linqdroid.F1;
import jp.linqdroid.xml.XElement;
import jp.linqdroid.xml.XName;
import jp.linqdroid.xml.XNamespace;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;


public class XmlStreamingReader {

	public static XmlStreamingReader load(final String fileName) {
		return new XmlStreamingReader(new F<InputStream>() {
			@Override
			public InputStream invoke() {
				try {
					return new FileInputStream(fileName);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return null; 
			}
		});
	}
	public static XmlStreamingReader load(final URL url) {
		return new XmlStreamingReader(new F<InputStream>() {
			@Override
			public InputStream invoke() {
				try {
					return url.openStream();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}
	public static XmlStreamingReader parse(final String xmlString)  {
		return new XmlStreamingReader(new F<InputStream>() {
			@Override
			public InputStream invoke() {
				try {
					return  new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return null; 
			}
		});
	} 
	
	public static XmlStreamingReader load(final InputStream stream) {
		return new XmlStreamingReader(new F<InputStream>() {
			@Override
			public InputStream invoke() {
				return stream;
			}
		});
	}

	/** 元となるストリームを作成するファンクタ */
	private F<InputStream> streamFactory;
	/** 列挙完了時にストリームを閉じるファンクタ */
	private Disposable disposer;

	private XmlStreamingReader(F<InputStream> streamFactory) {
		this.streamFactory = streamFactory;
	}

	private XmlPullParser createParser() {
		XmlPullParser parser = Xml.newPullParser();
		try {
			final InputStream stream = this.streamFactory.invoke();
			//列挙完了時にストリームを閉じるためのDisposableを作成
			this.disposer = new Disposable() {
				@Override
				public void dispose() {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			parser.setInput(stream, "UTF-8");
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return parser;

	}
	
	private void moveToNextElements(final XmlPullParser parser) throws XmlPullParserException, IOException {
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT && eventType != XmlPullParser.START_TAG) {
			eventType = parser.next();
		}
	}
	
	private void moveToNextFollowing(final XmlPullParser parser) throws XmlPullParserException, IOException {
		int depth = parser.getDepth();
		int eventType = parser.getEventType();
		if (eventType == XmlPullParser.START_TAG && !parser.isEmptyElementTag()) {
			while (eventType != XmlPullParser.END_DOCUMENT && depth < parser.getDepth()) {
				eventType = parser.next();
			}
		}
		moveToNextElements(parser);
	}

	private XName createName(XmlPullParser parser) {
		XName name;
		String uri = parser.getNamespace();
		if ("".equals(uri)) {
			name = new XName(parser.getName());
		} else {
			String prefix = parser.getPrefix();
			String namespaseName = "".equals(prefix) ? "xmlns" : "xmlns:" + prefix;
			XNamespace namespace = new XNamespace(namespaseName, uri);
			name = new XName(namespace, parser.getName());
		}
		return name;
	}

	public XElement getElement(final XName name) throws XmlPullParserException, IOException {
		return getElements().firstOrEmpty(new F1<XElement,Boolean>() {
			@Override
			public Boolean invoke(XElement arg) {
				return arg.getName().equals(name);
			}
		});	
	}

	public Enumerable<XElement> getElements(final XName name) throws XmlPullParserException, IOException {
		return getElements().where(new F1<XElement,Boolean>() {
			@Override
			public Boolean invoke(XElement arg) {
				return arg.getName().equals(name);
			}
		});
	}
	public Enumerable<XElement> getElements() throws XmlPullParserException, IOException {
		final XmlPullParser parser = this.createParser();
		parser.next();
		return Enumerable.from(new Iterator<XElement>() {
			@Override
			public boolean hasNext() {
				try {
					moveToNextElements(parser);
					return parser.getEventType() != XmlPullParser.END_DOCUMENT;
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}

			@Override
			public XElement next() {
				XElement element = XElement.load(parser);
				try {
					moveToNextFollowing(parser);
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return element;
			}
			@Override
			public void remove() { throw new UnsupportedOperationException(); }
			
		},this.disposer);
	}
	public Enumerable<XElement> getDescendants(final XName name) throws XmlPullParserException, IOException {
		final XmlPullParser parser = this.createParser();
		parser.next();
		return Enumerable.from(new Iterator<XElement>() {
			@Override
			public boolean hasNext() {
				try {
					moveToNextElements(parser);
					return parser.getEventType() != XmlPullParser.END_DOCUMENT;
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}

			@Override
			public XElement next() {
				try {
					while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
						XName currentName = createName(parser);
						if (currentName.equals(name)) {
							return XElement.load(parser);
						}
						moveToNextElements(parser);
					}
					return null;
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			@Override
			public void remove() { throw new UnsupportedOperationException(); }
		},this.disposer);
	}
}

