package de.ismll.bootstrap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public class _WrappedSet implements Set<Entry<String, Object>> {

	private final Set<Entry<String, String>> set;

	public int size() {
		return set.size();
	}

	public boolean isEmpty() {
		return set.isEmpty();
	}

	public boolean contains(Object o) {
		return set.contains(o);
	}

	public Iterator<Entry<String, Object>> iterator() {
		return new _WrappedIterator(set.iterator());
	}

	public Object[] toArray() {
		return set.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return set.toArray(a);
	}

	public boolean add(Entry<String, Object> e) {
		return false;
	}
	
	public boolean remove(Object o) {
		return set.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return set.containsAll(c);
	}

	public boolean addAll(Collection<? extends Entry<String, Object>> c) {
		boolean ret = true;
		for (Entry<String, Object> o : c) {
			ret |= add(o);
		}
		return ret;
	}

	public boolean retainAll(Collection<?> c) {
		return set.retainAll(c);
	}

	public boolean removeAll(Collection<?> c) {
		return set.removeAll(c);
	}

	public void clear() {
		set.clear();
	}

	public boolean equals(Object o) {
		return set.equals(o);
	}

	public int hashCode() {
		return set.hashCode();
	}

	public _WrappedSet(Set<java.util.Map.Entry<String, String>> entrySet) {
		this.set = entrySet;
	}


}