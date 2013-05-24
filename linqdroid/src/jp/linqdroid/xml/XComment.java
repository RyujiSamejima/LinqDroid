package jp.linqdroid.xml;

/**
 * 
 * @author samejima
 *
 */
public class XComment extends XNode{
	/**  */
	protected String value;
	/**
	 * 
	 * @param comment
	 */
	public XComment(XComment comment) {
		super();
		this.document = comment.document;
		this.parent = comment.parent;
		this.value = comment.value;
	}
	/**
	 * 
	 * @param value
	 */
	public XComment(String value) {
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
		return XNodeType.Comment;
	}
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "<!--" + this.value + "-->";
	}
}
