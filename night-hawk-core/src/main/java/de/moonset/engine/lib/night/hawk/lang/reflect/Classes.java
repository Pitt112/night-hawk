package de.moonset.engine.lib.night.hawk.lang.reflect;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Primitives;
import de.moonset.engine.lib.night.hawk.lang.util.Arrays;
import de.moonset.engine.lib.night.hawk.lang.util.Utility;

/**
 * Created by pitt on 16.01.17.
 */
@Utility
public final class Classes {

		private Classes() { throw new UnsupportedOperationException(); }

		public static boolean isAssignable(Class<?> to, Class<?> from) {
				if (to == from) {
						return true;
				}

				if (to.isAssignableFrom(Primitives.wrap(from))) {
						return true;
				}

				return false;
		}

		public static boolean areAssignable(Class<?>[] to, Class<?>[] from) {
				Preconditions.checkNotNull(to, "to");
				Preconditions.checkNotNull(from, "from");
				Preconditions.checkArgument(to.length == from.length, "'to' and 'from' array differ in length");

				return Arrays.match(to, from, Classes::isAssignable);
		}
}
