package jp.linqdroid;


/**
 * 
 * @author samejima
 *
 * @param <T>
 */
public class CombinePredicate<T> implements F1<T, Boolean> {
	private F1<T, Boolean> first;
	private F1<T, Boolean> second;
	public CombinePredicate(F1<T, Boolean> first, F1<T, Boolean> second) {
		this.first = first;
		this.second = second;
	}
	/**
	 * 
	 */
	@Override
	public Boolean invoke(T arg1) {
		return this.first.invoke(arg1) && this.second.invoke(arg1);
	}
}