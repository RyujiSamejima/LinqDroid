package jp.linqdroid;

import java.util.Iterator;

public class SelectEnumerable<T,TResult> extends Enumerable<TResult> {
	private F<Iterator<TResult>> creator;
	protected SelectEnumerable(Iterable<T> source, F1<T,TResult> selector) {
		this.creator = new F<Iterator<TResult>>() {
			private Iterable<T> source;
			private F1<T,TResult> selector;
			public F<Iterator<TResult>> setParameter(Iterable<T> source,F1<T,TResult> selector) {
				this.source = source;
				this.selector = selector;
				return this;
			}
			@Override
			public Iterator<TResult> invoke() {
				return new SelectEnumerator<T, TResult>(this.source.iterator(),null , this.selector);
			}
		}.setParameter(source, selector);
	}
	
	protected SelectEnumerable(Iterable<T> source, F2<T,Integer,TResult> selector) {
		this.creator = new F<Iterator<TResult>>() {
			private Iterable<T> source;
			private F1<T,TResult> handler;
			private int counter = 0;

			public F<Iterator<TResult>> setParameter(Iterable<T> source,F2<T,Integer,TResult> selector) {
				this.source = source;
				this.handler = new F1<T,TResult>() {
					private F2<T,Integer,TResult> selector;
					public F1<T,TResult> setSelector(F2<T,Integer,TResult> selector) {
						this.selector = selector;
						return this;
					}
					@Override
					public TResult invoke(T arg) {
						return this.selector.invoke(arg,counter++);
					}
				}.setSelector(selector);
				return this;
			}
			@Override
			public Iterator<TResult> invoke() {
				return new SelectEnumerator<T, TResult>(this.source.iterator(),null , this.handler);
			}
		}.setParameter(source, selector);
	}

	@Override
	public Iterator<TResult> iterator() {
		return this.creator.invoke();
	}
}
