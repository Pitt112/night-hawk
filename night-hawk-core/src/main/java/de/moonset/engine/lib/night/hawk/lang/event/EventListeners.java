package de.moonset.engine.lib.night.hawk.lang.event;

import com.google.common.annotations.VisibleForTesting;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by pitt on 18.12.16.
 */
public class EventListeners<E> implements Iterable<E> {

		private final Collection<E> listeners;

		public EventListeners() {
				listeners = new CopyOnWriteArraySet<>();
		}

		@VisibleForTesting
		EventListeners(final Collection<E> collection) {
				listeners = collection;
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


		@NotNull
		@Override
		public Iterator<E> iterator() {
				return listeners.iterator();
		}
}
