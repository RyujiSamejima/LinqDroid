package jp.linqdroid;

public class WhereSelectEnumerator<T,TResult> extends SelectEnumerator<T,TResult> {
	protected F1<T, Boolean> predicate;

	public WhereSelectEnumerator(Iterable<T> source, F1<T,Boolean> predicate, F1<T,TResult> selector) {
		super(source, selector);
		this.predicate = predicate;
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
