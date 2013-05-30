package jp.linqdroid;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * @author samejima
 *
 * @param <T>
 */
public class EnumeratorImple<T> implements Enumerator<T> {
	protected Disposable disposable;
	protected IteratorStatus status;
	protected  Iterator<T> source;
	protected T next;

	public EnumeratorImple(Iterator<T> source, Disposable disposable) {
		this.status  = IteratorStatus.BeforeEnumeration;
		this.source = source;
		this.disposable = disposable;
		this.next = null;
	}

	public boolean hasNext() {
		//要素取得済みなら次はある
		if (next != null) return true;
		//破棄済みであれば次はない
		if (status == IteratorStatus.Disposed) return false;
		//未列挙であれば要素の有無をチェック
		if (status == IteratorStatus.BeforeEnumeration) {
			this.status = IteratorStatus.Enumerating;
		}
		if (this.source.hasNext()) {
			next = this.source.next();
			return true;
		} else {
			this.dispose();
			return false;
		}
	}
	
	public T next() {
		//破棄済み、未取得かつ次の要素が取れなければ例外送出
		if (status == IteratorStatus.Disposed || (this.next == null && !this.hasNext())) {
			throw new NoSuchElementException();
		}
		//次の要素を戻り値として返却。取得済みは次回に備えてクリア
		T result = this.next;
		this.next = null;
		return result;
	}
	@Deprecated
	public void remove() {
		throw new UnsupportedOperationException();
	}
	public void dispose() {
		if (disposable != null) {
			disposable.dispose();
		}
		status = IteratorStatus.Disposed;
	}

	public static class IteratorStatus {
    	private final String status;
    	private IteratorStatus(String status) {
    		this.status = status;
    	}
    	public String toString() { return status; }
    	public static final IteratorStatus BeforeEnumeration = new IteratorStatus("BeforeEnumeration");
    	public static final IteratorStatus Enumerating = new IteratorStatus("Enumerating");
    	public static final IteratorStatus Disposed = new IteratorStatus("Disposed");
    }
}
