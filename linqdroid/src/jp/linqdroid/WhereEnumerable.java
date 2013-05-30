package jp.linqdroid;

import java.util.Iterator;

/**
 * 
 * @author samejima
 *
 * @param <T>
 */
public class WhereEnumerable<T> extends Enumerable<T> {
	protected F1<T, Boolean> predicate;

	protected WhereEnumerable(Iterable<T> source, F1<T, Boolean> predicate) {
		super(source);
		this.predicate = predicate;
	}

	protected WhereEnumerable(Iterable<T> source, F2<T,Integer,Boolean> predicate) {
		super(source);
		this.predicate = new F1<T, Boolean>() {
			private F2<T,Integer,Boolean> predicate;
			private int counter = 0;
			public F1<T, Boolean> setPredicate(F2<T,Integer,Boolean> predicate) {
				this.predicate = predicate;
				return this;
			}
			@Override
			public Boolean invoke(T arg) {
				return this.predicate.invoke(arg, counter++);
			};
		}.setPredicate(predicate);
	}

	@Override
	public Iterator<T> iterator() {
		return new WhereEnumerater(this.source.iterator(),null , this.predicate);
	}

	@Override
	public WhereEnumerable<T> where(F1<T, Boolean> predicate) {
		this.predicate = new CombinePredicate<T>(this.predicate, predicate);
		return this;
	}
/*
	public <TResult> WhereSelectEnumerator<T, TResult> select(F1<T, TResult> selector) {
		return new WhereSelectEnumerator<T, TResult>(this, this.predicate, selector);
	}
*/
	private class WhereEnumerater extends EnumeratorImple<T> {
		protected F1<T, Boolean> predicate;
		
		public WhereEnumerater(Iterator<T> source, Disposable disposable, F1<T, Boolean> predicate) {
			super(source,disposable);
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
}

