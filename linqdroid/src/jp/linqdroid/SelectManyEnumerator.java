package jp.linqdroid;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SelectManyEnumerator<T,TResult> implements  Iterator<TResult> {
	protected Iterator<T> source;
	protected Enumerator<TResult> enumerator;
	protected TResult next;
	protected F1<T, Enumerator<TResult>> selector;

	@SuppressWarnings("unused")
	private  SelectManyEnumerator() { }

	protected SelectManyEnumerator(Iterable<T> source, F1<T, Enumerator<TResult>> selector) {
		this.source = source.iterator();
		this.enumerator = null;
		this.next = null;
		this.selector = selector;
	}

	@Override
	public boolean hasNext() {
		//要素取得済みなら次はある
		if (this.next != null) return true;
		if (enumerator == null) {
			//次の要素が取れなければ次はない
			if (!this.source.hasNext()) return false;
		}
		do {
			if (this.enumerator == null) {
				this.enumerator = this.selector.invoke(this.source.next());
			}
			if (this.enumerator.hasNext()) {
				this.next = this.enumerator.next();
				return true;
			}
			this.enumerator = null;
		} while (this.enumerator.hasNext());
		return false;
	}

	@Override
	public TResult next() {
		//未取得かつ次の要素が取れなければ例外送出
		if (this.next == null && !this.hasNext()) {
			throw new NoSuchElementException();
		}
		//次の要素を戻り値として返却。取得済みは次回に備えてクリア
		TResult result = this.next;
		this.next = null;
		return result;
	}

	@Deprecated
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
