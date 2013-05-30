package jp.linqdroid.xml;
/**
 * 名前空間クラス
 * @author samejima
 *
 */
public final class XNamespace  {
	/**  */
    public static final XNamespace NONE = new XNamespace("","");
	/**  */
    public static final XNamespace XML = new XNamespace("xml","http://www.w3.org/XML/1998/namespace");
	/**  */
    public static final XNamespace XMLNS = new XNamespace("xmlns","http://www.w3.org/2000/xmlns/");
	/**  */
    private final String name;
	/**  */
    private final String uri;

    public  XNamespace(final String name, final String uri) {
        this.name = name;
        this.uri = uri;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof XNamespace)) {
            return false;
        }

        final XNamespace that = (XNamespace) obj;
        return name.equals(that.name) && uri.equals(that.uri);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result += 31 * uri.hashCode();
        return result;
    }

    public String getPrefixName() {
        return name;
    }

    public String getNamespaceName() {
        return uri;
    }

    @Override
    public String toString() {
        return "XNamespace{name = '" + name + "', uri='" + uri + "\'}";
    }
 
}
