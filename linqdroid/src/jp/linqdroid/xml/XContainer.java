package jp.linqdroid.xml;

import jp.linqdroid.Enumerable;
/**
 * 
 * @author samejima
 *
 */
public abstract class XContainer extends XNode {
	/**
	 * 
	 * @param objects
	 */
	public abstract void add(Object...objects);
	/**
	 * 
	 * @param objects
	 */
	public abstract void addFirst(Object...objects);
	/**
	 * 
	 * @return
	 */
	public abstract Enumerable<XNode> getDescendantNodes();
	/**
	 * 
	 * @return
	 */
	public abstract Enumerable<XElement> getDescendants();
	/**
	 * 
	 * @param name
	 * @return
	 */
	public abstract Enumerable<XElement> getDescendants(XName name);
	/**
	 * 
	 * @param name
	 * @return
	 */
	public abstract XElement getElement(XName name);
	/**
	 * 
	 * @return
	 */
	public abstract Enumerable<XElement> getElements();
	/**
	 * 
	 * @param name
	 * @return
	 */
	public abstract Enumerable<XElement> getElements(XName name);
	/**
	 * 
	 * @return
	 */
	public abstract Enumerable<XNode> getNodes();
	/**
	 * 
	 */
	public abstract void removeNodes();
	/**
	 * 
	 * @param contents
	 */
	public abstract void replaceNodes(Object...contents);

	/**
	 * 
	 * @param nameSpaces
	 * @param obj
	 * @return
	 */
	public XObject createXObject(XNamespace nameSpaces,Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof XAttribute) {
			XAttribute attr = (XAttribute)obj;
			if (attr.parent == null) return attr;
			else return new XAttribute(attr);
		} else if (obj instanceof XComment) {
			XComment comment = (XComment)obj;
			if (comment.parent == null) return comment;
			else return new XComment(comment);
		} else if (obj instanceof XText) {
			XText text = (XText)obj;
			if (text.parent == null) return text;
			else return new XText(text);
		} else if (obj instanceof XElement) {
			XElement element = (XElement)obj;
			if (element.parent == null) return element;
			else return new XElement(element);
		} else if (obj instanceof String) {
			return new XElement(new XName(nameSpaces, (String)obj));
		} else {
			return new XElement(new XName(nameSpaces, obj.toString()));
		}
	}
}
