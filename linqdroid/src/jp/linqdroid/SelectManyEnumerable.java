package jp.linqdroid;

import java.util.Iterator;

import jp.linqdroid.IteratorStatus;

public class SelectManyEnumerable<T,TResult>  extends Enumerable<TResult> {
	protected IteratorStatus status;
	protected Iterator<T> source;
	protected Iterator<TResult> iterator;
	protected TResult result;
	protected F1<T, Enumerable<TResult>> selector;

	@SuppressWarnings("unused")
	private  SelectManyEnumerable() { }

	protected SelectManyEnumerable(Iterable<T> source, F1<T, Enumerable<TResult>> selector) {
		super(new SelectManyEnumerator<T, TResult>(source.iterator(),null , selector));
	}
}
