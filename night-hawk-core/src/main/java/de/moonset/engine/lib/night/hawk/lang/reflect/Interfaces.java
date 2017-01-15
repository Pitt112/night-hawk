package de.moonset.engine.lib.night.hawk.lang.reflect;

import de.moonset.engine.lib.night.hawk.lang.Utility;

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
}
