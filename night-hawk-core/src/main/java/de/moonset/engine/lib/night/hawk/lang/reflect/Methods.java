package de.moonset.engine.lib.night.hawk.lang.reflect;

import com.google.common.base.Preconditions;
import de.moonset.engine.lib.night.hawk.lang.Utility;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

/**
 * Created by pitt on 14.01.17.
 */

@Utility
public final class Methods {
		private Methods() { throw new UnsupportedOperationException(); }

		public static boolean isAbstract(final Method method) {

				Preconditions.checkNotNull(method, "method");

				return Modifier.isAbstract(method.getModifiers());
		}

		public static Method[] getMethodByName(final Class<?> clazz, final String methodName) {

				Preconditions.checkNotNull(clazz, "clazz");

				final Method[] methods = clazz.getMethods();
				return Stream.of(methods).filter(m -> m.getName().equals(methodName)).toArray(Method[]::new);
		}
}
