package de.moonset.engine.lib.night.hawk.lang;

import java.lang.reflect.Method;

/**
 * Created by pitt on 25.01.17.
 */
public class InvisibleMethodError extends Error {
		public InvisibleMethodError(final Method method, final IllegalAccessException cause) {
				super("method '" + method.toString() + "' became unexpectedly invisible", cause);
		}
}
