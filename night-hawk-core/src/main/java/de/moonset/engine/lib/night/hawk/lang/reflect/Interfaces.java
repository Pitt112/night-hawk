package de.moonset.engine.lib.night.hawk.lang.reflect;

import com.google.common.base.Preconditions;
import de.moonset.engine.lib.night.hawk.lang.util.Utility;

import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * Created by pitt on 08.01.17.
 */
@Utility
public final class Interfaces {
		private Interfaces() { throw new UnsupportedOperationException(); }

		public static boolean isFunctional(Class<?> clazz) {

				if (clazz.isAnnotationPresent(FunctionalInterface.class)) {
						return true;
				}

				return Stream.of(clazz.getMethods()).filter(Methods::isAbstract).count() == 1;
		}

		public static Method extractFunctionalMethod(Class<?> clazz) {
				Preconditions.checkArgument(isFunctional(clazz),
				                            "interface '%s' needs to be functional",
				                            clazz.getSimpleName());

				return Stream.of(clazz.getMethods()).filter(Methods::isAbstract).findAny().orElse(null);
		}
}
