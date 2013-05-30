package jp.linqdroid.xml;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import jp.linqdroid.*;
import jp.linqdroid.parser.TextStreamingReader;

public class XElement extends XContainer {
	/** この要素につけられた名前 */
	protected XName name;
	protected String value;
	/** 属性 */
	protected List<XAttribute> attributes;

	private static XName createName(String prefix, String uri, String name) {
		XName xName;
		if ("".equals(uri)) {
			xName = new XName(name);
		} else {
			String namespaseName = "".equals(prefix) ? "xmlns" : "xmlns:" + prefix;
			XNamespace namespace = new XNamespace(namespaseName, uri);
			xName = new XName(namespace, name);
		}
		return xName;
	}
	private static XAttribute createAttribute(XmlPullParser parser, int index) {
		String name = parser.getAttributeName(index);
		String namespace = parser.getAttributeNamespace(index);
		String prefix = "".equals(namespace) ? "" : parser.getAttributePrefix(index);
		return new XAttribute(createName(prefix,namespace,name), parser.getAttributeValue(index));
	}

	private static Enumerable<XAttribute> createAttributes(final XmlPullParser parser) {
		return Enumerable.from(new Iterator<XAttribute>() {
			int index = 0;
			@Override
			public boolean hasNext() {
				return (index < parser.getAttributeCount());
			}

			@Override
			public XAttribute next() {
				return  createAttribute(parser, index++);
			}

			@Override
			public void remove() { throw new UnsupportedOperationException(); }
		});		
	}

	/**
	 * ファイルから XElement を読み込みます。
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static XElement load(String fileName) throws FileNotFoundException {
		return XElement.load(new FileInputStream(fileName));
	}
	public static XElement load(URL url) throws IOException {
		return XElement.load(url.openStream());
	}

	/**
	 * XML を格納した文字列から XElement を読み込みます。
	 * @param xmlString
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static XElement parse(String xmlString) throws UnsupportedEncodingException {
		return XElement.load(new ByteArrayInputStream(xmlString.getBytes("UTF-8")));
	}	

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static XElement load(InputStream stream) {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(stream , "UTF-8");
			return XElement.load(parser);
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
	public static XElement load(XmlPullParser parser) {
		XElement element = null;
		try {
			while(parser.next() != XmlPullParser.START_TAG) { }
			element = createElement(parser);
			return element;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return element;
	}

	private static XElement createElement(XmlPullParser parser) {
		XElement element = null;
		try {
			int eventType = parser.getEventType();
			String name = parser.getName();
			String namespace = parser.getNamespace();
			String prefix = "".equals(namespace) ? "" : parser.getPrefix();
			element = new XElement(createName(prefix,namespace,name), createAttributes(parser));
			if (parser.isEmptyElementTag()) return element;
			eventType = parser.next();
			while (eventType != XmlPullParser.END_DOCUMENT && eventType != XmlPullParser.END_TAG) {
				switch(eventType) {
				case XmlPullParser.START_TAG: {
					element.add(createElement(parser));
				} break;
				case XmlPullParser.TEXT: {
					element.add(new XText(parser.getText()));				
				} break;
				case XmlPullParser.COMMENT: {
					element.add(new XComment(parser.getText()));
				} break;
				default:
					break;
				} 
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return element;
	}
	/**
	 * XElement クラスの新しいインスタンスを、別の XElement オブジェクトから初期化します。
	 */
	public XElement(XElement element) {
		super();
		//this.document = element.document;
		//this.parent = element.parent;
		this.attributes = new ArrayList<XAttribute>(element.attributes);
		this.nodes = new ArrayList<XNode>(element.nodes);
	}
	/**
	 * 指定した名前を使用して、XElement クラスの新しいインスタンスを初期化します。
	 */
	public XElement(XName name)	{
		super();
		this.name = name;
		this.attributes = new ArrayList<XAttribute>();
		this.nodes = new ArrayList<XNode>();
	}
	/**
	 * XStreamingElement オブジェクトから XElement クラスの新しいインスタンスを初期化します。
	 */
	//public XElement(XStreamingElement)	 {
	//}
	/**
	 * 指定した名前と内容を持つ XElement クラスの新しいインスタンスを初期化します。
	 */
	public XElement(XName name, Object ...contents) {
		this(name);
		this.add(contents);
	}
	@Override
	public XNodeType getNodeType() {
		return XNodeType.Element;
	}
	/**
	 * 
	 * @return
	 */
	public Enumerable<XElement> getEmptySequence() {
		return Enumerable.<XElement>getEmpty();
	}
	/**
	 * 
	 * @return
	 */
	public XName getName() {
		return name;
	}
	
	/**
	 * この要素が 1 つ以上の属性を持っているかどうかを示す値を取得します。
	 * @return
	 */
	public boolean hasAttributes()	{
		return this.attributes.size() != 0;
	}
	/**
	 * この要素が 1 つ以上の子要素を持っているかどうかを示す値を取得します。
	 */
	public boolean hasElements() {
		return getElements().count() != 0;
	}
	/**
	 * この要素に内容が格納されていないかどうかを示す値を取得します。
	 */
	public boolean isEmpty() {
		return nodes.isEmpty() && attributes.isEmpty();
	}
	/**
	 * この要素の連結されたテキスト コンテンツを取得または設定します。
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 指定した XName を持つ、この XElement の XAttribute を返します。
	 * @param name
	 * @return
	 */
	public XAttribute getAttribute	(final XName name) {
		return getAttributes().firstOrEmpty(new F1<XAttribute,Boolean>() {
			@Override
			public Boolean invoke(XAttribute arg) {
				return arg.getName().equals(name);
			}
		});
	}
	/**
	 * この要素の属性のコレクションを返します。
	 * @return
	 */
	public Enumerable<XAttribute> getAttributes() {
		return Enumerable.from(this.attributes);
		
	}
	/**
	 * この要素の属性のフィルター処理されたコレクションを返します。 一致する XName を持つ要素のみがコレクションに含められます。
	 * @param name
	 * @return
	 */
	public Enumerable<XAttribute> getAttributes(final XName name) {
		return getAttributes().where(new F1<XAttribute,Boolean>() {
			@Override
			public Boolean invoke(XAttribute arg) {
				return arg.getName().equals(name);
			}
		});
	}
	
	/**
	 * この要素およびこの要素のすべての子孫ノードをドキュメント順で格納している、ノードのコレクションを返します。
	 * @return
	 */
	public Enumerable<XNode> getDescendantNodesAndSelf() {
		return Enumerable.singleReturn((XNode)this).selectMany(new F1<XNode, Enumerable<XNode>>() {
			@Override
			public Enumerable<XNode> invoke(XNode arg) {
				return Enumerable.from(arg.nodes);
			}
		});
	}
	/**
	 * この要素およびこの要素のすべての子孫要素をドキュメント順で格納している、要素のコレクションを返します。
	 * 
	 * @return
	 */
	public Enumerable<XElement> getDescendantsAndSelf(){
		return Enumerable.singleReturn(this).selectMany(new F1<XElement, Enumerable<XElement>>() {
			@Override
			public Enumerable<XElement> invoke(XElement arg) {
				return Enumerable.from(arg.getElements());
			}
		});
	}
	/**
	 * この要素およびこの要素のすべての子孫要素をドキュメント順で格納している、フィルター処理された要素のコレクションを返します。 一致する XName を持つ要素のみがコレクションに含められます。
	 * @param name
	 * @return
	 */
	public Enumerable<XElement> getDescendantsAndSelf(final XName name) {
		return Enumerable.singleReturn(this).selectMany(new F1<XElement, Enumerable<XElement>>() {
			@Override
			public Enumerable<XElement> invoke(XElement arg) {
				return Enumerable.from(arg.getElements());
			}
		}).where(new F1<XElement,Boolean>() {
			@Override
			public Boolean invoke(XElement arg) {
				return arg.name.equals(name);
			}
		});
	}

	/**
	 * この XElement からノードおよび属性を削除します。
	 */
	public void removeAll() {
		this.attributes.clear();
		this.nodes.clear();
	}
	/**
	 * この要素の子ノードおよび属性を、指定された内容で置き換えます。
	 */
	public void replaceAll(Object...objects){
		//ワークを作成
		XElement work = new XElement(this.name,objects);
		this.attributes = work.attributes;
		this.nodes = work.nodes;
	}
	/**
	 * 
	 * @param attributes
	 */
	public void replaceAttributes(Object...attributes) {
		//ワークを作成
		XElement work = new XElement(this.name,attributes);
		this.attributes = work.attributes;
	}
	/**
	 * この XElement を指定した Stream に出力します。
	 * @param stream
	 */
	//public void save(Stream stream) {
	//}
	/**
	 * この要素をシリアル化してファイルに書き込みます。
	 * @param fileName
	 */
	public void save(String fileName) {
		// TODO Auto-generated method stub

	}
	/**
	 * 属性の値の設定、属性の追加、または属性の削除を行います。
	 * @param name
	 * @param value
	 */
	public void setAttributeValue(final XName name, String value)	{
		XAttribute attr = Enumerable.from(this.attributes).firstOrEmpty(new F1<XAttribute,Boolean>() {
			@Override
			public Boolean invoke(XAttribute arg) {
				return arg.getName().equals(name);
			}
		});
		if (attr == null) {
			this.attributes.add(new XAttribute(name, value));
		} else {
			if (value == null) {
				this.attributes.remove(attr);
			} else {
				attr.setValue(value);
			}
		}
	}
	/**
	 * 子要素の値の設定、子要素の追加、または子要素の削除を行います。
	 * @param name
	 * @param value
	 */
	public void setElementValue(XName name, Object value)	{
		XElement element = this.getElement(name);
		if (element == null) {
			this.nodes.add(new XElement(name, value));
		} else {
			if (value == null) {
				this.nodes.remove(element);
			} else {
				element.setValue(value);
			}
		}
	}
	/**
	 * 現在の要素の値を設定します。
	 * @param value
	 * @exception ArgumentNullException value は null です。
	 * @exception ArgumentException value が XObject です。
	 */
	public void setValue(Object value)	{
		if (value == null || value instanceof XObject)
			throw new InvalidParameterException();
		else 
			this.value = value.toString();
	}

	@Override
	public void add(Object ... objects) {
		for (Object obj : objects) {
			this.AddCore(obj);
		}
	}
	/**
	 * 
	 * @param obj
	 * @param allowAttribute
	 * @param allowNode
	 */
	private void AddCore(Object obj) {
		if (obj instanceof Iterable<?>) {
			for (Object item : (Iterable<?>)obj) {
				this.AddCore(item);
			}
		} else {
			XObject xObj = this.createXObject(this.name.getNamespace(), obj);
			xObj.parent = this;
			xObj.document = this.document;
			if (xObj instanceof XAttribute) {
				this.attributes.add((XAttribute)xObj);
			} else if (xObj instanceof XNode) {
				this.nodes.add((XNode)xObj);
			}
		}
	}
	@Override
	public void addFirst(Object... objects) {
		//ワークを作成
		XElement work = new XElement(this.name,objects);
		this.attributes = Enumerable.from(this.attributes).concat(work.attributes).toList();
		this.nodes =Enumerable.from(this.nodes).concat(work.nodes).toList();
	}
	@Override
	public Enumerable<XNode> getDescendantNodes() {
		return getNodes().selectMany(new F1<XNode, Enumerable<XNode>>() {
			@Override
			public Enumerable<XNode> invoke(XNode arg) {
				return Enumerable.from(arg.nodes);
			}
		});
	}
	@Override
	public Enumerable<XElement> getDescendants() {
		return getElements().selectMany(new F1<XElement, Enumerable<XElement>>() {
			@Override
			public Enumerable<XElement> invoke(XElement arg) {
				return Enumerable.from(arg.getElements());
			}
		});
	}
	@Override
	public Enumerable<XElement> getDescendants(final XName name) {
		return getElements().selectMany(new F1<XElement, Enumerable<XElement>>() {
			@Override
			public Enumerable<XElement> invoke(XElement arg) {
				return Enumerable.from(arg.getElements());
			}
		}).where(new F1<XElement,Boolean>() {
			@Override
			public Boolean invoke(XElement arg) {
				return arg.name.equals(name);
			}
		});
	}
	@Override
	public XElement getElement(final XName name) {
		return getElements().firstOrEmpty(new F1<XElement,Boolean>() {
			@Override
			public Boolean invoke(XElement arg) {
				return arg.name.equals(name);
			}
		});
	}
	@Override
	public Enumerable<XElement> getElements() {
		return Enumerable.from(this.nodes).ofClass(XElement.class);
	}
	@Override
	public Enumerable<XElement> getElements(final XName name) {
		return getElements().where(new F1<XElement,Boolean>() {
			@Override
			public Boolean invoke(XElement arg) {
				return arg.name.equals(name);
			}
		});
	}
	@Override
	public Enumerable<XNode> getNodes() {
		return Enumerable.from(this.nodes);
	}
	@Override
	public void removeNodes() {
		this.nodes.clear();
	}
	@Override
	public void replaceNodes(Object... contents) {
		//ワークを作成
		XElement work = new XElement(this.name,contents);
		this.nodes = work.nodes;
	}
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<");
		buffer.append(this.name.toString());
		if (this.hasAttributes()) {
			for (XAttribute attr : this.attributes) {
				buffer.append(" ");
				buffer.append(attr.toString());
			}
		}
		if (!this.isEmpty()) {
			buffer.append(">");
			for (XNode node : this.nodes) {
				/*TextStreamingReader reader = TextStreamingReader.parse(node.toString());
				for (String line : reader.getLines()) {
					buffer.append("    ");
					buffer.append(line);
					buffer.append("\n");
				}
				*/
				buffer.append(node.toString());
				buffer.append("</");
				buffer.append(this.name.toString());
				buffer.append(">\n");
			}
		} else {
			buffer.append(" />\n");
		}
		return buffer.toString();
	}

/*
String
Double
Single
Decimal
Boolean
DateTime
TimeSpan
DateTimeOffset
ToString() を実装する任意の型
IEnumerable<T> を実装する任意の型
複合コンテンツを追加するときは、次のような型をこのメソッドに渡すことができます。
XObject
XNode
XAttribute
IEnumerable<T> を実装する任意の型 
*/
}

