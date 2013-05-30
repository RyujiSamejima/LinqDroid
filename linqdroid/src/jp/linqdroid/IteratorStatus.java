package jp.linqdroid;

/**
 * 
 * @author samejima
 *
 * @param <T>
 */
public class IteratorStatus {
	private final String status;
	private IteratorStatus(String status) {
		this.status = status;
	}
	public String toString() { return status; }
	public static final IteratorStatus BeforeEnumeration = new IteratorStatus("BeforeEnumeration");
	public static final IteratorStatus Enumerating = new IteratorStatus("Enumerating");
	public static final IteratorStatus Disposed = new IteratorStatus("Disposed");
}
