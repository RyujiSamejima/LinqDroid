package jp.linqdroid.xml;

import jp.linqdroid.Enumerable;

/**
 * 
 * @author samejima
 *
 */
public class XAttribute extends XObject {
	/**  */
	private XName name;
	/**  */
	private String value;
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public XAttribute(XName name, String value) {
		this.document = null;
		this.parent = null;
		this.name = name;
		this.value = value;
	}
	/**
	 * 
	 * @param attribute
	 */
	public XAttribute(XAttribute attribute) {
		this.document = null;
		this.parent = null;
		this.name = attribute.name;
		this.value = attribute.value;
	}
	/**
	 * 
	 */
	@Override
	public XNodeType getNodeType() {
		return XNodeType.Attribute;
	}
	/**
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 
	 * @return
	 */
	public XName getName() {
		return name;
	}
	/**
	 * 
	 * @return
	 */
	public String getValue() {
		return value;
	}
	/**
	 * 
	 * @return
	 */
	public Enumerable<XAttribute> getEmptySequence() {
		return Enumerable.<XAttribute>getEmpty();
	}
	/**
	 * 
	 */
	public void remove() {
		this.parent.attributes.remove(this);
	}
}
