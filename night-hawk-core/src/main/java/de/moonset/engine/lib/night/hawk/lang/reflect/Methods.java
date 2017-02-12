package de.moonset.engine.lib.night.hawk.lang.reflect;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import de.moonset.engine.lib.night.hawk.lang.util.Collections;
import de.moonset.engine.lib.night.hawk.lang.util.Utility;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
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

		public static Method[] getMethodsByName(final Class<?> clazz, final String methodName) {

				Preconditions.checkNotNull(clazz, "clazz");
				Preconditions.checkNotNull(methodName, "methodName");

				final Method[] methods = clazz.getMethods();
				return Stream.of(methods).filter(m -> m.getName().equals(methodName)).toArray(Method[]::new);
		}

		public static Method[] findAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotation,
				Predicate<Method> filter) {

				final List<Method> result = new ArrayList<>();

				final Set<? extends Class<?>> superTypes = TypeToken.of(clazz).getTypes().rawTypes();

				for (Class<?> type : superTypes) {
						if (type == Object.class) {
								continue;
						}

						for (Method method : type.getDeclaredMethods()) {
								if (method.isAnnotationPresent(annotation) && filter.test(method)) {
										result.add(method);
								}
						}
				}

				return Collections.toArray(result, Method[]::new);
		}

		public static MethodHandle toMethodHandle(Method method) throws IllegalAccessException {
				Preconditions.checkNotNull(method, "method");
				return MethodHandles.publicLookup().unreflect(method);
		}

		public static MethodHandle[] toMethodHandles(Method[] methods) throws IllegalAccessException {
				Preconditions.checkNotNull(methods, "methods");
				final MethodHandle[] handles = new MethodHandle[methods.length];

				int i = 0;
				for (Method method : methods) {
						handles[i++] = toMethodHandle(method);
				}

				return handles;
		}

}
