package com.pab.jmxmonitor.util;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MultiMap<K, V> extends Map<K, List<V>> {
	
	void add(K k, V v);

	void remove(K k, V v);

	void addAll(MultiMap<K, V> edges);
	
	public class Utils {
		static MultiMap<Object,Object> theEmptyMap = new EmptyMultiMap();
		@SuppressWarnings("unchecked")
		public static <K, V> MultiMap<K, V> emptyMultiMap() {
			return (MultiMap<K, V>) theEmptyMap;
		}
		
		public static <K,V> MultiMap<K, V> invert(Map<V, K> map) {
			MultiMap<K, V> m = new HashMultiMap<K, V>();
			for (Map.Entry<V, K> ent: map.entrySet()) {
				m.add(ent.getValue(), ent.getKey());
			}
			return m;
		}
	}
	
	public class EmptyMultiMap extends AbstractMap<Object, List<Object>> implements MultiMap<Object, Object> {

		@Override
		public Set<java.util.Map.Entry<Object, List<Object>>> entrySet() {
			return Collections.emptySet();
		}

		@Override
		public void add(Object k, Object v) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void remove(Object k, Object v) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addAll(MultiMap<Object, Object> edges) {
			throw new UnsupportedOperationException();
		}

	}


}
