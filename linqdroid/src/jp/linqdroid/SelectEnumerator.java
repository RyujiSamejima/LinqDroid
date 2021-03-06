package jp.linqdroid;

import java.util.Iterator;
import java.util.NoSuchElementException;

import jp.linqdroid.IteratorStatus;

public class SelectEnumerator<T,TResult> implements Enumerator<TResult> {
	protected Disposable disposable;
	protected IteratorStatus status;
	protected Iterator<T> source;
	protected F1<T,TResult> selector;
	protected T next;

	@SuppressWarnings("unused")
	private  SelectEnumerator() { }

	public SelectEnumerator(Iterator<T> source, Disposable disposable, F1<T,TResult> selector) {
		this.source = source;
		this.disposable = disposable;
		this.selector = selector;
		this.next = null;
	}

	@Override
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
	

	@Override
	public TResult next() {
		//破棄済み、未取得かつ次の要素が取れなければ例外送出
		if (status == IteratorStatus.Disposed || (this.next == null && !this.hasNext())) {
			throw new NoSuchElementException();
		}
		//次の要素を戻り値として返却。取得済みは次回に備えてクリア
		T result = this.next;
		this.next = null;
		return this.selector.invoke(result);
	}
	@Deprecated
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	@Override
	public void dispose() {
		if (disposable != null) {
			disposable.dispose();
		}
		status = IteratorStatus.Disposed;
	}

}

