package jp.linqdroid;

import java.util.Iterator;

import jp.linqdroid.IteratorStatus;

public class WhereSelectEnumerator<T,TResult> extends SelectEnumerator<T,TResult> {
	protected F1<T, Boolean> predicate;

	public WhereSelectEnumerator(Iterator<T> source,Disposable disposable, F1<T,Boolean> predicate, F1<T,TResult> selector) {
		super(source,disposable, selector);
		this.predicate = predicate;
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
		while (this.source.hasNext()) {
			//次の要素を取得する
			T obj = this.source.next();
			//条件に合うもののみ
			if (this.predicate.invoke(obj)) {
				this.next = obj;
				return true;
			}
		}
		return false;
	}
}
