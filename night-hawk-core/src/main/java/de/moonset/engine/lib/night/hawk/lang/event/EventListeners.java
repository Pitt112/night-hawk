package de.moonset.engine.lib.night.hawk.lang.event;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pitt on 18.12.16.
 */
public class EventListeners<E> implements Iterable<E> {

		private final Collection<E> listeners;

		public EventListeners() {
				this(8);
		}

		public EventListeners(final int initialCapacity) {
				listeners = Collections.newSetFromMap(new ConcurrentHashMap<E, Boolean>(initialCapacity));
		}

		public boolean add(E listener) {
				return listeners.add(listener);
		}

		public boolean remove(E listener) {
				return listeners.remove(listener);
		}

		public int size() {
				return listeners.size();
		}

		@Override
		public Iterator<E> iterator() {
				return listeners.iterator();
		}
}
