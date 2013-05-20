package jp.linqdroid;

import java.util.Iterator;

/**
 * 
 * @author samejima
 *
 * @param <T>
 */
public class Enumerable<T> {

	/**
	 * 
	 */
	private Enumerable() { }

	/**
	 * 
	 * @param source
	 * @return
	 */
	public static <E> Enumerator<E> from(Iterable<E> source) {
		return new Enumerator<E>(source);
	}
}
