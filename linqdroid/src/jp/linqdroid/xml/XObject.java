package jp.linqdroid.xml;

/**
 * Xml要素の基本クラス
 * @author samejima
 *
 */
public abstract class XObject {
	/** ドキュメント */
	protected XDocument document;
	/** 親要素 */
	protected XElement parent;
	/** ノードタイプを取得する */
	public abstract XNodeType getNodeType();

	/**
	 * ドキュメントを取得する
	 * @return
	 */
	public XDocument getDocument() {
		return document;
	}
	/**
	 * 親要素を取得する
	 * @return
	 */
	public XElement getParent() {
		return parent;
	}
}
