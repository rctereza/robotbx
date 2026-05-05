package com.rctereza.robotbx.wrappers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.rctereza.robotbx.interfaces.Wrappable;

public class AppData implements Serializable {

	private static final long serialVersionUID = -16550397148575121L;

	private final Map<Class<?>, TypeBucket<?>> storage = new HashMap<>();
//	private final Map<Class<?>, TypeBucket<?>> storage = new ConcurrentHashMap<>(); // Thread-safe if needed

	private static class TypeBucket<T extends Wrappable> {
		int sequence = 0;
		NavigableMap<Integer, List<T>> data = new TreeMap<>();
	}

	@SuppressWarnings("unchecked")
	private <T extends Wrappable> TypeBucket<T> getBucket(Class<T> clazz) {
		return (TypeBucket<T>) storage.computeIfAbsent(clazz, k -> new TypeBucket<T>());
	}

	 // ---------------- SEQUENCE ----------------

    public <T extends Wrappable> void setSequence(Class<T> clazz, int sequence) {
        getBucket(clazz).sequence = sequence;
    }

    public <T extends Wrappable> int getSequence(Class<T> clazz) {
        return getBucket(clazz).sequence;
    }
    
    public <T extends Wrappable> int nextSequence(Class<T> clazz) {
        TypeBucket<T> bucket = getBucket(clazz);
        return ++bucket.sequence;
    }
    
    // ---------------- DATA ----------------
    
    public <T extends Wrappable> void addItem(Class<T> clazz, int key, T item) {
        TypeBucket<T> bucket = getBucket(clazz);
        bucket.data.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
    }

    public <T extends Wrappable> void addList(Class<T> clazz, int key, List<T> list) {
        getBucket(clazz).data.put(key, list);
    }

    public <T extends Wrappable> NavigableMap<Integer, List<T>> getAll(Class<T> clazz) {
        return getBucket(clazz).data;
    }

    public <T extends Wrappable> List<T> getLastListAdded(Class<T> clazz) {
        NavigableMap<Integer, List<T>> map = getBucket(clazz).data;
        return map.isEmpty() ? new ArrayList<>() : map.lastEntry().getValue();
    }

}
