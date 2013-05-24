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
	public abstract void add(Object[] ...objects);
	/**
	 * 
	 * @param objects
	 */
	public abstract void addFirst(Object[] ...objects);
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
	public abstract void replaceNodes(Object[] ...contents);
}
