package jp.linqdroid.xml;

public final class XName implements Comparable<XName> {
    private final XNamespace namespace;
    private final String localName;
    
    public XName(final String name) {
        this(XNamespace.NONE, name);
    }

    public XName(final XNamespace namespace, final String localName) {
        if (namespace == null) {
            throw new NullPointerException("namespace");
        }
        this.localName = localName;
        this.namespace = namespace;
    }

    public XName(final XName base) {
        this(base.namespace, base.localName);
    }

    @Override
    public int compareTo(final XName o) {
        return localName.compareTo(o.localName);
    }

    @Override
    public String toString() {
    	String namespaceName = getNamespaceName();
        return "".equals(namespaceName) ? localName : "{" + namespaceName + "}" + localName;
    }

    public String getLocalName() {
        return localName;
    }

    public XNamespace getNamespace() {
        return namespace;
    }

    public String getNamespaceName() {
        return namespace.getNamespaceName();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof XName)) {
            return false;
        }

        final XName xName = (XName) o;
        return localName.equals(xName.localName) && namespace.equals(xName.namespace);
    }

    @Override
    public int hashCode() {
        int result = namespace.hashCode();
        result = 31 * result + localName.hashCode();
        return result;
    }

    @Override
    public XName clone() {
        return new XName(this);
    }
 }
