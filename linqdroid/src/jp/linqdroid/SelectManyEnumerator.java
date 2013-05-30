package jp.linqdroid;

import java.util.Iterator;
import java.util.NoSuchElementException;

import jp.linqdroid.EnumeratorImple.IteratorStatus;

public class SelectManyEnumerator<T,TResult> implements Enumerator<TResult> {
	protected Disposable disposable;
	protected IteratorStatus status;
	protected Iterator<T> source;
	protected Iterator<TResult> iterator;
	protected TResult result;
	protected F1<T, Enumerable<TResult>> selector;

	@SuppressWarnings("unused")
	private  SelectManyEnumerator() { }

	protected SelectManyEnumerator(Iterator<T> source, Disposable disposable, F1<T, Enumerable<TResult>> selector) {
		this.source = source;
		this.disposable = disposable;
		this.iterator = null;
		this.result = null;
		this.selector = selector;
	}

	@Override
	public boolean hasNext() {
		//要素取得済みなら次はある
		if (result != null) return true;
		//破棄済みであれば次はない
		if (status == IteratorStatus.Disposed) return false;
		//未列挙であれば要素の有無をチェック
		if (status == IteratorStatus.BeforeEnumeration) {
			this.status = IteratorStatus.Enumerating;
		}
		for(;;) {
			if (this.iterator == null) {
				//次の列挙子が取れるか
				if (this.source.hasNext()) {
					this.iterator = this.selector.invoke(this.source.next()).iterator();
				} else {
					this.dispose();
					return false;
				}
			}
			if (this.iterator.hasNext()) {
				this.result = this.iterator.next();
				return true;
			}
			this.iterator = null;
		}
	}

	@Override
	public TResult next() {
		//未取得かつ次の要素が取れなければ例外送出
		if (this.result == null && !this.hasNext()) {
			throw new NoSuchElementException();
		}
		//次の要素を戻り値として返却。取得済みは次回に備えてクリア
		TResult result = this.result;
		this.result = null;
		return result;
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
