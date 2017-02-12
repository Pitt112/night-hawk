package de.moonset.engine.lib.night.hawk.lang.pool;

import com.google.common.base.Throwables;
import de.moonset.engine.lib.night.hawk.lang.reflect.Methods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

/**
 * Created by pitt on 30.01.17.
 */
public class PoolLifeCycle<T> {

		private static final Logger LOGGER = LoggerFactory.getLogger(PoolLifeCycle.class);
		private static final Marker POOL   = MarkerFactory.getMarker("POOL");

		private final MethodHandle[] onDestroy;
		private final MethodHandle[] onRecycle;

		public PoolLifeCycle(final Class<T> clazz) throws IllegalAccessException {

				onDestroy = findMethods(clazz, OnDestroy.class);
				onRecycle = findMethods(clazz, OnRecycle.class);
		}

		private static MethodHandle[] findMethods(Class<?> clazz, Class<? extends Annotation> annotation)
				throws IllegalAccessException {

				final Method[] methods = Methods.findAnnotatedMethods(clazz, annotation, m -> m.getParameterCount() == 0);
				return Methods.toMethodHandles(methods);
		}

		private void invoke(final MethodHandle[] handles, T borrowed) {
				if (handles != null) {
						for (MethodHandle mh : handles) {
								try {
										mh.invoke(borrowed);
								} catch (Throwable throwable) {
										LOGGER.error(POOL, "cannot invoke '{}'", mh);
										Throwables.throwIfUnchecked(throwable);
								}
						}
				}
		}

		public void onDestroy(T borrowed) {
				invoke(onDestroy, borrowed);
		}

		public void onRecycle(T borrowed) {
				invoke(onRecycle, borrowed);
		}
}
