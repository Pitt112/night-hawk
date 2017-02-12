package de.moonset.engine.lib.night.hawk.lang;

import com.google.common.base.Preconditions;
import de.moonset.engine.lib.night.hawk.lang.util.Utility;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by pitt on 15.12.16.
 */
@Utility
public final class BitMasks {

		private BitMasks() { throw new UnsupportedOperationException(); }

		public static <E extends Enum<E> & BitMaskFlag> Set<E> toSet(Class<E> type, long bitmask) {

				Preconditions.checkNotNull(type, "type must not be null");

				final EnumSet<E> all = EnumSet.allOf(type);

				for (Iterator<E> it = all.iterator(); it.hasNext(); ) {
						E value = it.next();
						if ((value.value() & bitmask) != value.value()) {
								it.remove();
						}
				}

				return all;
		}

		public static <E extends Enum<E> & BitMaskFlag> Set<E> toSet(Class<E> type, long bitmask, boolean strict) {

				Preconditions.checkNotNull(type, "type must not be null");

				final EnumSet<E> all = EnumSet.allOf(type);

				long resolved = 0;

				for (Iterator<E> it = all.iterator(); it.hasNext(); ) {
						final E    value = it.next();
						final long mask  = value.value();
						if ((mask & bitmask) != mask) {
								it.remove();
						} else {
								resolved |= mask;
						}
				}

				Preconditions.checkArgument(!strict || (bitmask == resolved),
				                            "bitmask '%s' contains unresolved values '%s'. type %s",
				                            bitmask,
				                            resolved,
				                            type.getSimpleName());

				return all;
		}

		public static <E extends Enum<E> & BitMaskFlag> long toBitMask(E e1) {

				Preconditions.checkNotNull(e1, "e1 may not be null");

				return e1.value();
		}

		public static <E extends Enum<E> & BitMaskFlag> long toBitMask(E e1, E e2) {

				Preconditions.checkNotNull(e1, "e1 may not be null");
				Preconditions.checkNotNull(e2, "e2 may not be null");

				return e1.value() | e2.value();
		}

		public static <E extends Enum<E> & BitMaskFlag> long toBitMask(E e1, E e2, E e3) {

				Preconditions.checkNotNull(e1, "e1 may not be null");
				Preconditions.checkNotNull(e2, "e2 may not be null");
				Preconditions.checkNotNull(e3, "e3 may not be null");

				return e1.value() | e2.value() | e3.value();
		}

		public static <E extends Enum<E> & BitMaskFlag> long toBitMask(E e1, E e2, E e3, E e4) {

				Preconditions.checkNotNull(e1, "e1 may not be null");
				Preconditions.checkNotNull(e2, "e2 may not be null");
				Preconditions.checkNotNull(e3, "e3 may not be null");
				Preconditions.checkNotNull(e4, "e4 may not be null");

				return e1.value() | e2.value() | e3.value() | e4.value();
		}

		public static <E extends Enum<E> & BitMaskFlag> long toBitMask(E e1, E e2, E e3, E e4, E e5) {

				Preconditions.checkNotNull(e1, "e1 may not be null");
				Preconditions.checkNotNull(e2, "e2 may not be null");
				Preconditions.checkNotNull(e3, "e3 may not be null");
				Preconditions.checkNotNull(e4, "e4 may not be null");
				Preconditions.checkNotNull(e5, "e5 may not be null");

				return e1.value() | e2.value() | e3.value() | e4.value() | e5.value();
		}

		@SafeVarargs
		public static <E extends Enum<E> & BitMaskFlag> long toBitMask(E e1, E... rest) {

				Preconditions.checkNotNull(e1, "e4 may not be null");
				Preconditions.checkNotNull(rest, "rest may not be null");

				long bitmask = e1.value();

				for (E flag : rest) {
						bitmask |= flag.value();
				}

				return bitmask;
		}

		public static <E extends Enum<E> & BitMaskFlag> long toBitMask(final Iterable<E> values) {


				Preconditions.checkNotNull(values, "values may not be null");

				long bitmask = 0;

				for (E flag : values) {
						bitmask |= flag.value();
				}

				return bitmask;
		}
}
