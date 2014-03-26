package de.ismll.bootstrap;

import java.util.Iterator;
import java.util.Map.Entry;

public class _WrappedIterator implements Iterator<Entry<String, Object>> {

	private final Iterator<Entry<String, String>> iterator;

	public _WrappedIterator(Iterator<Entry<String, String>> iterator) {
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public Entry<String, Object> next() {
		return new _WrappedEntry(iterator.next());
	}

	@Override
	public void remove() {

	}

}