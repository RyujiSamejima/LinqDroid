package jp.linqdroid;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * @author samejima
 *
 * @param <T>
 */
public class Enumerator<T> implements Iterator<T> {
	protected  Iterator<T> source;
	protected T next;

	protected Enumerator(Iterable<T> source) {
		this.source = source.iterator();
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
	public T next() {
		//未取得かつ次の要素が取れなければ例外送出
		if (this.next == null && !this.hasNext()) {
			throw new NoSuchElementException();
		}
		//次の要素を戻り値として返却。取得済みは次回に備えてクリア
		T result = this.next;
		this.next = null;
		return result;
	}

	@Deprecated
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
