package jp.linqdroid.xml;

public class XText extends XNode{
	/**  */
	protected String value;
	/**
	 * 
	 * @param text
	 */
	public XText(XText text) {
		super();
		this.document = text.document;
		this.parent = text.parent;
		this.value = text.value;
	}
	/**
	 * 
	 * @param value
	 */
	public XText(String value) {
		super();
		this.value = value;
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
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 
	 */
	@Override
	public XNodeType getNodeType() {
		return XNodeType.Text;
	}
	/**
	 * 
	 */
	@Override
	public String toString() {
		return value;
	}
}
