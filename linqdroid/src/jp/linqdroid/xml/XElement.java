package jp.linqdroid.xml;

import java.util.ArrayList;
import java.util.List;

import jp.linqdroid.Enumerable;

public class XElement extends XContainer {
	/** この要素につけられた名前 */
	protected XName name;
	/** 属性 */
	protected List<XAttribute> attributes;

	/**
	 * XElement クラスの新しいインスタンスを、別の XElement オブジェクトから初期化します。
	 */
	public XElement(XElement element) {
		super();
		//this.document = element.document;
		//this.parent = element.parent;
		this.attributes = new ArrayList<XAttribute>(element.attributes);
	}
	/**
	 * 指定した名前を使用して、XElement クラスの新しいインスタンスを初期化します。
	 */
	public XElement(XName name)	{
		super();
		
	}
	/**
	 * XStreamingElement オブジェクトから XElement クラスの新しいインスタンスを初期化します。
	 */
	//public XElement(XStreamingElement)	 {
	//}
	/**
	 * 指定した名前と内容を持つ XElement クラスの新しいインスタンスを初期化します。
	 */
	public XElement(XName name, Object[] ...contents) {
	}
	@Override
	public XNodeType getNodeType() {
		return XNodeType.Element;
	}
	public Enumerable<XElement> getEmptySequence() {
		return Enumerable.<XElement>getEmpty();
	}
	/**
	 * この要素が 1 つ以上の属性を持っているかどうかを示す値を取得します。
	 * @return
	 */
	public boolean hasAttributes()	{
		return false;
		
	}
	/**
	 * この要素が 1 つ以上の子要素を持っているかどうかを示す値を取得します。
	 */
	public boolean hasElements() {
		return false;}
	/**
	 * この要素に内容が格納されていないかどうかを示す値を取得します。
	 */
	public boolean isEmpty() {
		return false;}
	/**
	 * この要素の連結されたテキスト コンテンツを取得または設定します。
	 * @return
	 */
	public String getValue() {
		return null;}

	/**
	 * 指定した XName を持つ、この XElement の XAttribute を返します。
	 * @param name
	 * @return
	 */
	public XAttribute getAttribute	(XName name) {
		return null;
		
	}
	/**
	 * この要素の属性のコレクションを返します。
	 * @return
	 */
	public Enumerable<XAttribute> getAttributes() {
		return null;
		
	}
	/**
	 * この要素の属性のフィルター処理されたコレクションを返します。 一致する XName を持つ要素のみがコレクションに含められます。
	 * @param name
	 * @return
	 */
	public Enumerable<XAttribute> getAttributes(XName name) {
		return null;}
	
	/**
	 * この要素およびこの要素のすべての子孫ノードをドキュメント順で格納している、ノードのコレクションを返します。
	 * @return
	 */
	public Enumerable<XNode> getDescendantNodesAndSelf() {
		return null;}
	/**
	 * この要素およびこの要素のすべての子孫要素をドキュメント順で格納している、要素のコレクションを返します。
	 * 
	 * @return
	 */
	public Enumerable<XElement> getDescendantsAndSelf(){
		return null;}
	/**
	 * この要素およびこの要素のすべての子孫要素をドキュメント順で格納している、フィルター処理された要素のコレクションを返します。 一致する XName を持つ要素のみがコレクションに含められます。
	 * @param name
	 * @return
	 */
	public Enumerable<XElement> getDescendantsAndSelf(XName name) {
		return null;
		
	}
	/**
	 * ファイルから XElement を読み込みます。
	 * @param fileName
	 * @return
	 */
	public XElement load(String fileName) {
		return parent;}
	/**
	 * XmlParser から XElement を読み込みます。
	 * @param parser
	 * @return
	 */
	//public XElement load(XmlParser parser) {}

	/**
	 * XML を格納した文字列から XElement を読み込みます。
	 * @param xmlString
	 * @return
	 */
	public XElement parse(String xmlString) {
		return parent;}	
	/**
	 * この XElement からノードおよび属性を削除します。
	 */
	public void removeAll() {
		
	}
	/**
	 * この要素の子ノードおよび属性を、指定された内容で置き換えます。
	 */
	public void replaceAll(Object[] ...objects){
		
	}
	/**
	 * 
	 * @param attributes
	 */
	public void replaceAttributes(Object[] ...attributes) {
		
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
	public void save(String fileName) {}
	/**
	 * 属性の値の設定、属性の追加、または属性の削除を行います。
	 * @param name
	 * @param value
	 */
	public void setAttributeValue(XName name, Object value)	{}
	/**
	 * 子要素の値の設定、子要素の追加、または子要素の削除を行います。
	 * @param name
	 * @param value
	 */
	public void setElementValue(XName name, Object value)	{}
	/**
	 * 現在の要素の値を設定します。
	 * @param value
	 * @exception ArgumentNullException value は null です。
	 * @exception ArgumentException value が XObject です。
	 */
	public void setValue(Object value)	{}
	@Override
	public void add(Object[]... objects) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addFirst(Object[]... objects) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Enumerable<XNode> getDescendantNodes() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Enumerable<XElement> getDescendants() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Enumerable<XElement> getDescendants(XName name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public XElement getElement(XName name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Enumerable<XElement> getElements() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Enumerable<XElement> getElements(XName name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Enumerable<XNode> getNodes() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void removeNodes() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void replaceNodes(Object[]... contents) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
}
