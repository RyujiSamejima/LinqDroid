package jp.linqdroid;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SelectEnumerator<T,TResult> implements Iterator<TResult> {
	protected Iterator<T> source;
	protected F1<T,TResult> selector;
	protected T next;

	@SuppressWarnings("unused")
	private  SelectEnumerator() { }

	public SelectEnumerator(Iterable<T> source, F1<T,TResult> selector) {
		this.source = source.iterator();
		this.selector = selector;
		this.next = null;
	}

	@Override
	public boolean hasNext() {
		//要素取得済みなら次はある
		if (this.next != null) return true;
		//次の要素が取れなければ次はない
		if (!this.source.hasNext()) return false;
		//次の要素を取得する
		this.next = this.source.next();
		return true;
	}

	@Override
	public TResult next() {
		//未取得かつ次の要素が取れなければ例外送出
		if (this.next == null && !this.hasNext()) {
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

}

