package jp.linqdroid;

/**
 * 
 * @author samejima
 *
 * @param <T>
 */
public class WhereEnumerater<T> extends Enumerator<T> {
	protected F1<T, Boolean> predicate;
	
	public WhereEnumerater(Iterable<T> source, F1<T, Boolean> predicate) {
		super(source);
		this.predicate = predicate;
	}
	
	public WhereEnumerater<T> where(F1<T, Boolean> predicate) {
		this.predicate = new CombinePredicate<T>(this.predicate, predicate);
		return this;
	}

	public <TResult> WhereSelectEnumerator<T, TResult> select(F1<T, TResult> selector) {
		return new WhereSelectEnumerator<T, TResult>(this, this.predicate, selector);
	}
	
	@Override
	public boolean hasNext() {
		//要素取得済みなら次はある
		if (this.next != null) return true;
		//次の要素が取れなければ次はない
		while (this.source.hasNext()) {
			//次の要素を取得する
			T obj = this.source.next();
			//条件に合うもののみ
			if (this.predicate.invoke(obj))
				this.next = obj;
				return true;
		}
		return false;
	}
}
