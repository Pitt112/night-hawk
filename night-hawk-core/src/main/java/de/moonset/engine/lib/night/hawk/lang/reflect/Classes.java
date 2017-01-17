package de.moonset.engine.lib.night.hawk.lang.reflect;

import com.google.common.primitives.Primitives;
import de.moonset.engine.lib.night.hawk.lang.Utility;

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


}
