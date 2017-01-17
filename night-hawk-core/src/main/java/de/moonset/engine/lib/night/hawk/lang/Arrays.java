package de.moonset.engine.lib.night.hawk.lang;

import com.google.common.base.Preconditions;

import java.util.function.BiPredicate;

/**
 * Created by pitt on 17.01.17.
 */
@Utility
public final class Arrays {
		private Arrays() { throw new UnsupportedOperationException(); }

		public static <T> boolean matches(T[] a, T[] b, BiPredicate<T, T> matcher) {

				Preconditions.checkNotNull(a, "a");
				Preconditions.checkNotNull(b, "b");
				Preconditions.checkArgument(a.length == b.length, "'a' and 'b' array differ in length");

				for (int i = 0; i < b.length; i++) {
						if (!matcher.test(a[i], b[i])) {
								return false;
						}
				}

				return true;
		}
}
