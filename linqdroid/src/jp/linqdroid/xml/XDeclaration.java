package jp.linqdroid.xml;

public class XDeclaration {
	private String encoding;
	private String version;
	private String standalone;
	XDeclaration(XDeclaration declaration) {
		this.encoding = declaration.encoding;
		this.version = declaration.version;
		this.standalone = declaration.standalone;
	}
	/**
	 * バージョン、エンコーディング、およびスタンドアロン ステータスを指定して、XDeclaration クラスの新しいインスタンスを初期化します。
	 * @param encoding
	 * @param version
	 * @param standalone
	 */
	XDeclaration(String encoding, String version, String standalone) {
		this.encoding = encoding;
		this.version = version;
		this.standalone = standalone;
	}
	public String toString() {
		return "<?xml version=\"" + version + "\" encoding=\"" + encoding + "\" standalone=\"" + standalone + "\"?>";
	}
}
