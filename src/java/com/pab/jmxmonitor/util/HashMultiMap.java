package com.pab.jmxmonitor.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class HashMultiMap<K, V> extends HashMap<K, List<V>> implements MultiMap<K, V> {
	private static final long serialVersionUID = 4026280025018606624L;

	
	public void add(K k, V v) {
		List<V> l = get(k);
		if (l == null) {
			l = new LinkedList<V>();
			put(k, l);
		}
		l.add(v);
	}

	public List<V> put(K key, List<V> value) {
		return super.put(key, value);
	}

	public void putAll(Map<? extends K, ? extends List<V>> m) {
		super.putAll(m);
	}

	@Override
	public void remove(K k, V v) {
		List<V> l = get(k);
		l.remove(v);
	}

	@Override
	public void addAll(MultiMap<K, V> edges) {
		for (Map.Entry<K, List<V>> ent: edges.entrySet()) {
			List<V> l = get(ent.getKey());
			if (l == null) {
				l = new LinkedList<V>(ent.getValue());
				put(ent.getKey(), l);
			}
			else {
				l.addAll(ent.getValue());
			}
		}
	}
}
