package jp.linqdroid.xml;

public class XNodeType {
	private String type;
	private XNodeType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return type;
	}
	public static final XNodeType Element = new XNodeType("Element");
	public static final XNodeType Attribute = new XNodeType("Attribute");
	public static final XNodeType Text = new XNodeType("Text");
//	public static final XNodeType CDATA = new XNodeType("CDATA");
//	public static final XNodeType EntityReference = new XNodeType("EntityReference");
//	public static final XNodeType Entity = new XNodeType("Entity");
//	public static final XNodeType ProcessingInstruction = new XNodeType("ProcessingInstruction");
	public static final XNodeType Comment = new XNodeType("Comment");
	public static final XNodeType Document = new XNodeType("Document");
//	public static final XNodeType DocumentType = new XNodeType("DocumentType");
//	public static final XNodeType DocumentFragment = new XNodeType("DocumentFragment");
//	public static final XNodeType Notation = new XNodeType("Notation");
//	public static final XNodeType Whitespace = new XNodeType("Whitespace");
//	public static final XNodeType SignificantWhitespace = new XNodeType("SignificantWhitespace");
//	public static final XNodeType EndElement = new XNodeType("EndElement");
	public static final XNodeType Declaration = new XNodeType("Declaration ");
  }
