package jp.linqdroid.xml;

import java.util.List;

public abstract class XNode extends XObject {
	/** 子ノード */
	protected List<XNode> nodes;
	/**
	 * この要素を親ノードから削除する
	 */
	public void remove() {
		XElement parent = this.parent;
		this.parent = null;
		parent.nodes.remove(this);
	}
	/**
	 * この要素を指定された内容で置き換える
	 */
	public void replaceWith(Object[] ...contents) {
		XElement parent = this.parent;
		this.parent = null;
		parent.nodes.remove(this);
		parent.add(contents);
	}
	/**
	 * このノードに対してインデントが設定された XML を返します。
	 */
	public abstract String toString();
}