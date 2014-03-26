package de.ismll.bootstrap;

import java.util.Map.Entry;

public class _WrappedEntry implements Entry<String, Object> {

	private final Entry<String, String> next;

	public _WrappedEntry(Entry<String, String> next) {
		this.next = next;
	}

	@Override
	public String getKey() {
		return next.getKey();
	}

	@Override
	public Object getValue() {	
		return next.getValue();
	}

	@Override
	public Object setValue(Object value) {
		return next.setValue(value.toString());
	}

}