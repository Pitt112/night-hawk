package de.moonset.engine.lib.night.hawk.lang.util;

import java.util.Collection;
import java.util.function.IntFunction;

/**
 * Created by pitt on 11.02.17.
 */
@Utility
public final class Collections {

		private Collections() { throw new UnsupportedOperationException();}

		public static <T> T[] toArray(Collection<T> items, IntFunction<T[]> generator) {

				final T[] result = generator.apply(items.size());
				int       i      = 0;
				for (T item : items) {
						result[i++] = item;
				}

				return result;
		}
}
