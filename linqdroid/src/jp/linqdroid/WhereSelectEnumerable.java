package jp.linqdroid;

import java.util.Iterator;

public class WhereSelectEnumerable<T,TResult> extends Enumerable<TResult> {
	private F<Iterator<TResult>> creator;
	protected WhereSelectEnumerable(Iterable<T> source,F1<T,Boolean> predicate,F1<T,TResult> selector) {
		this.creator = new F<Iterator<TResult>>() {
			private Iterable<T> source;
			private F1<T,Boolean> predicate;
			private F1<T,TResult> selector;
			public F<Iterator<TResult>> setParameter(Iterable<T> source,F1<T,Boolean> predicate,F1<T,TResult> selector) {
				this.source = source;
				this.predicate = predicate;
				this.selector = selector;
				return this;
			}
			@Override
			public Iterator<TResult> invoke() {
				return new WhereSelectEnumerator<T, TResult>(this.source, this.predicate, this.selector);
			}
		}.setParameter(source, predicate, selector);
	}
	
	@Override
	public Iterator<TResult> iterator() {
		return this.creator.invoke();
	}
}
