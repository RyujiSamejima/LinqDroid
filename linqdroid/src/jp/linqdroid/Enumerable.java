package jp.linqdroid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author samejima
 *
 * @param <T>
 */
public class Enumerable<T> implements Iterable<T> {
	protected Iterable<T> source;
	/**
	 * 
	 */
	protected Enumerable() { }
	protected Enumerable(Iterable<T> source) { 
		this.source = source;
	}
	protected Enumerable(final Iterator<T> source) { 
		this.source = new Iterable<T>() { 
			@Override
			public Iterator<T> iterator() {
				return new EnumeratorImple<T>(source, null);
			}
		};
	}
	/**
	 * 
	 * @param source
	 */
	protected Enumerable(final Iterator<T> source,final Disposable disposable) { 
		this.source = new Iterable<T>() { 
			@Override
			public Iterator<T> iterator() {
				return new EnumeratorImple<T>(source, disposable);
			}
		};
	}
	protected Enumerable(Enumerable<T> source) { 
		this.source = source;
	}
	/**
	 * 
	 * @param source
	 */
	protected Enumerable(final Enumerator<T> source) { 
		this.source = new Iterable<T>() { 
			@Override
			public Iterator<T> iterator() {
				return source;
			}
		};
	}
	/**
	 * 
	 */
	@Override
	public Iterator<T> iterator() {
		return source.iterator();
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	public static <T> Enumerable<T> from(Iterable<T> source) {
		return new Enumerable<T>(source);
	}
	/**
	 * 
	 * @param source
	 * @return
	 */
	public static <T> Enumerable<T> from(Iterator<T> source) {
		return new Enumerable<T>(source);
	}
	public static <T> Enumerable<T> from(Iterator<T> source, Disposable disposable) {
		return new Enumerable<T>(source,disposable);
	}
	/**
	 * 
	 * @param item
	 * @return
	 */
	public  static <T> Enumerable<T> singleReturn(final T item) {
		return new Enumerable<T>(new Iterator<T>() {
			private boolean before = true;
			@Override
			public boolean hasNext() {
				return before;
			}

			@Override
			public T next() {
				before = false;
				return item;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		});
	}

	/**
	 * 
	 * @param array
	 * @return
	 */
	public static <T> Enumerable<T> from(T[] array) {
		return new Enumerable<T>(Arrays.asList(array));
	}
	/**
	 * 
	 * @return
	 */
	public static <T> Enumerable<T> getEmpty() {
		return Enumerable.from(new Iterable<T>() 
				{
					@Override
					public Iterator<T> iterator() {
						return new Iterator<T>() {
							@Override
							public boolean hasNext() {
								return false;
							}

							@Override
							public T next() {
								return null;
							}

							@Override
							public void remove() {
								throw new UnsupportedOperationException();
							}
						};
					}			
				});
	}

	/**
	 * 
	 * @param classType
	 * @return
	 */
	public <TResult> Enumerable<TResult> ofClass(final Class<TResult> classType) {
		return new WhereSelectEnumerable<T, TResult>(this, new F1<T,Boolean>() {
			//final Class<?> type = classType;
			@Override
			public Boolean invoke(T arg1) {
				return classType.isAssignableFrom(arg1.getClass());
			}
		} , new F1<T,TResult>() {
			//final Class<?> type = classType;
			@SuppressWarnings("unchecked")
			@Override
			public TResult invoke(T arg1) {
				return (TResult)arg1;
			}
		});
	}

	/**
	 * 
	 * @param selector
	 * @return
	 */
	public <TResult> Enumerable<TResult> select(F1<T, TResult> selector) {
		return new SelectEnumerable<T, TResult>(this, selector);
	}
	/**
	 * 
	 * @param selector
	 * @return
	 */
	public <TResult> Enumerable<TResult> select(F2<T, Integer, TResult> selector) {
		return new SelectEnumerable<T, TResult>(this, selector);
	}

	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public Enumerable<T> where(F1<T, Boolean> predicate) {
		return new WhereEnumerable<T>(this, predicate);
	}

	public Enumerable<T> where(F2<T, Integer, Boolean> predicate) {
		return new WhereEnumerable<T>(this, predicate);
	}
	/**
	 * 
	 * @param count
	 * @return
	 */
	public Enumerable<T> skip(final int count) {
		return new Enumerable<T>(new EnumeratorImple<T>(this.iterator(), null) {
			private int counter = 0;
			public T next() {
				while(this.counter++ < count) {
					final T item = super.next();
					if (item == null) return null;
				}
				return super.next();
			}
		});
	}
	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public Enumerable<T> skipWhile(final F1<T, Boolean> predicate) {
		return this.skipWhile(new F2<T, Integer, Boolean>() {
			@Override
			public Boolean invoke(T arg1, Integer arg2)  {
				return predicate.invoke(arg1);
			}
		});
	}	
	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public Enumerable<T> skipWhile(final F2<T, Integer, Boolean> predicate) {
		return new Enumerable<T>(new EnumeratorImple<T>(this.iterator(), null) {
			private boolean skipped = false;
			private int counter = 0;
			@Override
			public T next() {
				if (!this.skipped) {
					T item;
					do {
						item = super.next();
						if (item == null) return null;
					} while (predicate.invoke(item, counter++));
					this.skipped = true;
					return item;
				}
				return super.next();
			}
		});
	}	
	/**
	 * 
	 * @param count
	 * @return
	 */
	public Enumerable<T> take(final int count) {
		return new Enumerable<T>(new EnumeratorImple<T>(this.iterator(), null) {
			private int counter = 0;
			@Override
			public boolean hasNext() {
				if (counter++ < count) {
					return super.hasNext();
				} else {
					return false;
				}
			}
		});
	}
	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public Enumerable<T> takeWhile(final F1<T, Boolean> predicate) {
		return this.takeWhile(new F2<T, Integer, Boolean>() {
			@Override
			public Boolean invoke(T arg1, Integer arg2) {
				return predicate.invoke(arg1);
			}
		});
	}	
	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public Enumerable<T> takeWhile(final F2<T, Integer, Boolean> predicate) {
		return new Enumerable<T>(new EnumeratorImple<T>(this.iterator(), null) {
			private boolean taking = true;
			private int counter = 0;
			@Override
			public boolean hasNext() {
				if (!taking) return false;
				while (super.hasNext()) {
					if (predicate.invoke(this.next, counter++)) {
						return true;
					} else {
						taking = false;
						this.next = null;
						return false;
					}
				}
				return false;
			}
		});
	}
	/**
	 * 
	 * @param accumlator
	 * @return
	 */
	public Enumerable<T> scan(final F2<T, T, T> accumlator) {
		return new Enumerable<T>(new EnumeratorImple<T>(this.iterator(), null) {
			private boolean isFirst = true;
			private T result;
			@Override
			public T next() {
				T item = super.next();
				if (item != null) {
					if (isFirst) {
						this.result =  item;
						this.isFirst = false;
					} else {
						this.result = accumlator.invoke(this.result, item);
					}
					return result;
				}
				return null;
			}
		});
	}

	/**
	 * 
	 * @return
	 */
	public List<T> toList() {
		List<T> array = new ArrayList<T>();
		for (T item : this) {
			array.add(item);
		}
		return array;
	}
	
	/**
	 * 
	 * @param selector
	 * @return
	 */
	public <TResult> Enumerable<TResult> selectMany(F1<T, Enumerable<TResult>> selector) {
		return new Enumerable<TResult>(new SelectManyEnumerator<T, TResult>(this.iterator(), null, selector));
	}
	/**
	 * 
	 * @return
	 */
	public Enumerable<T> disinct() {
		return this.where(new F1<T,Boolean>() {
			private List<T> list = new ArrayList<T>();
			@Override
			public Boolean invoke(T arg) {
				boolean result = list.contains(arg);
				if (!result) {
					list.add(arg);
				}
				return !result;
			}
		});
	}
	/**
	 * 
	 * @param second
	 * @return
	 */
	public Enumerable<T> concat(final Iterable<T> second) {
		return new Enumerable<T>(new EnumeratorImple<T>(this.iterator(), null) {
			private boolean isSecond = false;
			@Override
			public boolean hasNext() 
			{
				if(!isSecond) {
					if (super.hasNext()) {
						return true;
					} else {
						super.status = IteratorStatus.BeforeEnumeration;
						super.source = second.iterator();
						isSecond = true;
					}
				}
				return super.hasNext();
			}
		});
	}
	/**
	 * 
	 * @param second
	 * @return
	 */
	public Enumerable<T> union(Iterable<T> second) {
		return this.concat(second).disinct();
	}
	/**
	 * 
	 * @param second
	 * @return
	 */
	public Enumerable<T> intersect(final Iterable<T> second) {
		return this.where(new F1<T,Boolean>() {
			private List<T> secondList = Enumerable.from(second).toList();
			public Boolean invoke(T arg) {
				return this.secondList.contains(arg);
			}
		});
	}
	/**
	 * 
	 * @param second
	 * @return
	 */
	public Enumerable<T> except(final Iterable<T> second) {
		return this.where(new F1<T,Boolean>() {
			private List<T> list = Enumerable.from(second).toList();
			@Override
			public Boolean invoke(T arg) {
				boolean result = list.contains(arg);
				if (!result) {
					list.add(arg);
				}
				return !result;
			}
		});
	}
	/**
	 * 
	 * @param index
	 * @return
	 */
	public T elementAt(int index) {
		if (this.source instanceof List<?>) {
			List<T> list = (List<T>)this.source;
			return list.get(index);
		} else {
			return this.skip(index).first();
		}
	}
	/**
	 * 
	 * @param index
	 * @return
	 */
	public T elementOrEmptyAt(int index) {
		if (this.source instanceof List<?>) {
			List<T> list = (List<T>)this.source;
			if (index < list.size()) {
				return list.get(index);
			} else {
				return null;
			}
		} else {
			int count = 0;
			for (T item : this.source) {
				if (index == count++ ) {
					return item;
				}
			}
			return null;
		}
	}
	/**
	 * 
	 * @return
	 */
	public T single() {
		Iterator<T> iterator = this.source.iterator();
		T result = null;
		if (iterator.hasNext()) {
			result = iterator.next();
		}
		if (!iterator.hasNext()) {
			return result;
		}
		throw new IndexOutOfBoundsException();
	}
	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public T single(F1<T,Boolean> predicate) {
		Iterator<T> iterator = this.where(predicate).iterator();
		T result = null;
		if (iterator.hasNext()) {
			result = iterator.next();
		}
		if (!iterator.hasNext()) {
			return result;
		}
		throw new IndexOutOfBoundsException();
	}
	/**
	 * 
	 * @return
	 */
	public T singleOrEmpty() {
		Iterator<T> iterator = this.source.iterator();
		T result = null;
		if (iterator.hasNext()) {
			result = iterator.next();
		} else {
			return null;
		}
		if (!iterator.hasNext()) {
			return result;
		}
		throw new IndexOutOfBoundsException();
	}
	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public T singleOrEmpty(F1<T,Boolean> predicate) {
		Iterator<T> iterator = this.where(predicate).iterator();
		T result = null;
		if (iterator.hasNext()) {
			result = iterator.next();
		} else {
			return null;
		}
		if (!iterator.hasNext()) {
			return result;
		}
		throw new IndexOutOfBoundsException();
	}
	/**
	 * 
	 * @return
	 */
	public T first() {
		Iterator<T> iterator = this.source.iterator();
		if (iterator.hasNext()) {
			return iterator.next();
		}
		throw new IndexOutOfBoundsException();
	}
	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public T first(F1<T,Boolean> predicate) {
		Iterator<T> iterator = this.where(predicate).iterator();
		if (iterator.hasNext()) {
			return iterator.next();
		}
		throw new IndexOutOfBoundsException();
	}
	/**
	 * 
	 * @return
	 */
	public T firstOrEmpty() {
		Iterator<T> iterator = this.source.iterator();
		if (iterator.hasNext()) {
			return iterator.next();
		}
		return null;
	}
	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public T firstOrEmpty(F1<T,Boolean> predicate) {
		Iterator<T> iterator = this.where(predicate).iterator();
		if (iterator.hasNext()) {
			return iterator.next();
		}
		return null;
	}
	/**
	 * 
	 * @return
	 */
	public T last() {
		Iterator<T> iterator = this.source.iterator();
		if (iterator.hasNext()) {
			T result = iterator.next();
			while (iterator.hasNext()) {
				result = iterator.next();
			}
			return result;
		}
		throw new IndexOutOfBoundsException();
	}
	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public T last(F1<T,Boolean> predicate) {
		Iterator<T> iterator = this.where(predicate).iterator();
		if (iterator.hasNext()) {
			T result = iterator.next();
			while (iterator.hasNext()) {
				result = iterator.next();
			}
			return result;
		}
		throw new IndexOutOfBoundsException();
	}
	/**
	 * 
	 * @return
	 */
	public T lastOrEmpty() {
		Iterator<T> iterator = this.source.iterator();
		if (iterator.hasNext()) {
			T result = iterator.next();
			while (iterator.hasNext()) {
				result = iterator.next();
			}
			return result;
		}
		return null;
	}
	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public T lastOrEmpty(F1<T,Boolean> predicate) {
		Iterator<T> iterator = this.where(predicate).iterator();
		if (iterator.hasNext()) {
			T result = iterator.next();
			while (iterator.hasNext()) {
				result = iterator.next();
			}
			return result;
		}
		return null;
	}
	/**
	 * 
	 * @return
	 */
	public int count() {
		if (this.source instanceof Collection<?>) {
			Collection<T> collection = (Collection<T>)this.source;
			return collection.size();
		} else {
			int count = 0;
			for (Iterator<T> iterator = this.source.iterator(); iterator.hasNext();) {
				count++;
			}
			return count;
		}
	}
	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public boolean all(F1<T,Boolean> predicate) {
		for (Iterator<T> iterator = this.source.iterator(); iterator.hasNext();) {
			if (!predicate.invoke(iterator.next()))
				return false;
		}
		return true;
	}
	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public boolean any(F1<T,Boolean> predicate) {
		for (Iterator<T> iterator = this.source.iterator(); iterator.hasNext();) {
			if (!predicate.invoke(iterator.next()))
				return true;
		}
		return false;
	}
	/**
	 * 
	 * @param item
	 * @return
	 */
	public boolean contains(T item) {
		for (Iterator<T> iterator = this.source.iterator(); iterator.hasNext();) {
			if (item.equals(iterator.next()))
				return true;
		}
		return false;
	}
	/**
	 * 
	 * @param dst
	 * @return
	 */
	public boolean sequenceEqual(Iterable<T> dst) {
		Iterator<T> second = dst.iterator();
		for (Iterator<T> iterator = this.source.iterator(); iterator.hasNext();) {
			if(!second.hasNext() || !(iterator.next().equals(second.next())))
				return false;
		}
		return !second.hasNext();
	}

	/*
	orderByDescription(NSSortDescriptor *firstObj, ...);
	buffer(int count);
	toArray();
	toMutableArray();
	toDictionary(id(^keySelector)(id item));
	toDictionaryWithSelector(id(^keySelector)(id item), id(^elementSelector)(id item));
	toMutableDictionary(id(^keySelector)(id item));
	toMutableDictionaryWithSelector(id(^keySelector)(id), id(^elementSelector)(id item));
	toData();
*/
	
	/**
	 * 
	 * @param action
	 */
	public void foeach(A1<T> action) {
		for (T item : this) {
			action.invoke(item);
		}
	}	
}
