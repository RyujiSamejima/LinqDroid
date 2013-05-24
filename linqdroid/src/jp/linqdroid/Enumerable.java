package jp.linqdroid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author samejima
 *
 * @param <T>
 */
public class Enumerable<T> implements Iterable<T> {
	protected Iterator<T> source;

	/**
	 * 
	 */
	protected Enumerable() { }
	protected Enumerable(Iterable<T> source) { 
		this.source = source.iterator();
	}
	protected Enumerable(Iterator<T> source) { 
		this.source = source;
	}

	@Override
	public Iterator<T> iterator() {
		return this.source;
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	public static <T> Enumerable<T> from(Iterable<T> source) {
		return new Enumerable<T>() {
			private Iterable<T> source;
			public Enumerable<T> setSource(Iterable<T> source) {
				this.source = source;
				return this;
			}
			public Iterator<T> iterator() {
				return source.iterator();
			};
		}.setSource(source);
	}
	
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
	public Enumerable<T> ofClass(final Class<?> classType) {
		return new WhereEnumerable<T>(this, new F1<T,Boolean>() {
			@Override
			public Boolean invoke(T arg1) {
				return (arg1.getClass() == classType);
			}
		});
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
	public Enumerable<T> skip(int count) {
		return new Enumerable<T>(new Enumerator<T>(this) {
			private int limit = 0;
			private int counter = 0;
			public Enumerator<T> setCount(final int limit) {
				this.limit = limit;
				return this;
			}
			public T next() {
				while(this.counter++ < this.limit) {
					final T item = super.next();
					if (item == null) return null;
				}
				return super.next();
			}
		}.setCount(count));
	}
	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public Enumerable<T> skipWhile(F1<T, Boolean> predicate) {
		return this.skipWhile(new F2<T, Integer, Boolean>() {
			private F1<T, Boolean> predicate;
			public F2<T, Integer, Boolean> setPredicate(F1<T, Boolean> predicate) {
				this.predicate = predicate;
				return this;
			}
			@Override
			public Boolean invoke(T arg1, Integer arg2)  {
				return this.predicate.invoke(arg1);
			}
		}.setPredicate(predicate));
	}	
	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public Enumerable<T> skipWhile(F2<T, Integer, Boolean> predicate) {
		return new Enumerable<T>(new Enumerator<T>(this) {
			private boolean skipped = false;
			private int counter = 0;
			private F2<T, Integer, Boolean> predicate;
			public Enumerator<T> setPredicate(F2<T, Integer, Boolean> predicate) {
				this.predicate = predicate;
				return this;
			}
			@Override
			public T next() {
				if (!this.skipped) {
					T item;
					do {
						item = super.next();
						if (item == null) return null;
					} while (this.predicate.invoke(item, counter++));
					this.skipped = true;
					return item;
				}
				return super.next();
			}
		}.setPredicate(predicate));
	}	
	/**
	 * 
	 * @param count
	 * @return
	 */
	public Enumerable<T> take(int count) {
		return new Enumerable<T>(new Enumerator<T>(this) {
			private int count = 0;
			private int counter = 0;
			public Enumerator<T> setCounter(int count) {
				this.count = count;
				return this;
			}
			@Override
			public T next() {
				T item;
				if (counter++ < count && (item = super.next()) != null) {
					return item;
				}
				return null;
			}
		}.setCounter(count));
	}
	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public Enumerable<T> takeWhile(F1<T, Boolean> predicate) {
		return this.takeWhile(new F2<T, Integer, Boolean>() {
			private F1<T, Boolean> predicate;
			public   F2<T, Integer, Boolean> setPredicate(F1<T, Boolean> predicate) {
				this.predicate = predicate;
				return this;
			}
			@Override
			public Boolean invoke(T arg1, Integer arg2) {
				return this.predicate.invoke(arg1);
			}
		}.setPredicate(predicate));
	}	
	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public Enumerable<T> takeWhile(F2<T, Integer, Boolean> predicate) {
		return new Enumerable<T>(new Enumerator<T>(this) {
			private boolean taking = true;
			private int counter = 0;
			private F2<T, Integer, Boolean> predicate;
			public  Enumerator<T> setPredicate(F2<T, Integer, Boolean> predicate) {
				this.predicate = predicate;
				return this;
			}
			@Override
			public T next() {
				T item ;
				if (taking) {
					while ((item = super.next()) != null && (taking = this.predicate.invoke(item, counter++)) == true) {
						return item;
					}
				}
				return null;
			}
		}.setPredicate(predicate));
	}
	/**
	 * 
	 * @param accumlator
	 * @return
	 */
	public Enumerable<T> scan(F2<T, T, T> accumlator) {
		return new Enumerable<T>(new Enumerator<T>(this) {
			private boolean isFirst = true;
			private T result;
			private F2<T, T, T> accumlator;
			public Enumerator<T> setAccumlator(F2<T, T, T> accumlator) {
				this.accumlator = accumlator;
				return this;
			}
			@Override
			public T next() {
				T item = super.next();
				if (item != null) {
					if (isFirst) {
						this.result =  item;
						this.isFirst = false;
					} else {
						this.result = this.accumlator.invoke(this.result, item);
					}
					return result;
				}
				return null;
			}
		}.setAccumlator(accumlator));
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
	public <TResult> SelectManyEnumerator<T,TResult> selectMany(F1<T, Enumerator<TResult>> selector) {
		return new SelectManyEnumerator<T, TResult>(this, selector);
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
				return result;
			}
		});
	}
	/**
	 * 
	 * @param second
	 * @return
	 */
	public Enumerable<T> concat(Iterable<T> second) {
		return new Enumerable<T>(new Enumerator<T>(this) {
			private boolean isSecond = false;
			private Iterable<T> second;
			public Enumerator<T> setSecond(Iterable<T> second) {
				this.second =  second;
				return this;
			}
			@Override
			public boolean hasNext() 
			{
				if(!isSecond) {
					if (super.hasNext()) {
						return true;
					} else {
						super.source = this.second.iterator();
					}
				}
				return super.hasNext();
				
			}
		}.setSecond(second));
	}
	public Enumerable<T> union(Iterable<T> second) {
		return this.concat(second).disinct();
	}
	
	public Enumerable<T> intersect(Iterable<T> second) {
		return this.where(new F1<T,Boolean>() {
			private List<T> secondList;
			public F1<T,Boolean> setSecond(Iterable<T> second) {
				this.secondList = Enumerable.from(second).toList();
				return this;
			}
			public Boolean invoke(T arg) {
				return this.secondList.contains(arg);
			}
		}.setSecond(second));
	}

	public Enumerable<T> except(Iterable<T> second) {
		return this.concat(second).where(new F1<T,Boolean>() {
			private List<T> firstList;
			private List<T> secondList;
			public F1<T,Boolean> setTarget(Iterable<T> first, Iterable<T> second) {
				this.firstList = Enumerable.from(first).toList();
				this.secondList = Enumerable.from(second).toList();
				return this;
			}
			public Boolean invoke(T arg) {
				boolean first = this.firstList.contains(arg);
				boolean second = this.secondList.contains(arg);
				return ((first && !second) || (!first && second));
			}
		}.setTarget(this, second));
	}

	/*
	orderByDescription(NSSortDescriptor *firstObj, ...);
	selectMany(id(^selector)(id index));
	distinct();
	concat(NSEnumerator *dst);
	unions(NSEnumerator *dst);
	intersect(NSEnumerator *dst);
	except(NSEnumerator *dst);
	buffer(int count);
	toArray();
	toMutableArray();
	toDictionary(id(^keySelector)(id item));
	toDictionaryWithSelector(id(^keySelector)(id item), id(^elementSelector)(id item));
	toMutableDictionary(id(^keySelector)(id item));
	toMutableDictionaryWithSelector(id(^keySelector)(id), id(^elementSelector)(id item));
	toData();
	toString();
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
