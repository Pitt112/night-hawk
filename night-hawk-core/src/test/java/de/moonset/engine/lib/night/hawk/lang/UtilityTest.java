package de.moonset.engine.lib.night.hawk.lang;

import org.junit.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by pitt on 15.01.17.
 */
public class UtilityTest {

		private static final Logger LOGGER  = LoggerFactory.getLogger(UtilityTest.class);
		private static final Marker UTILITY = MarkerFactory.getMarker("UTILITY");

		@Test
		public void testPrivateInvoke() throws NoSuchMethodException, IllegalAccessException, InstantiationException {

				Reflections reflections = new Reflections("de.moonset.engine.lib.night.hawk");

				final Set<Class<?>> types = reflections.getTypesAnnotatedWith(Utility.class);

				for (Class<?> type : types) {
						LOGGER.info(UTILITY, "checking '{}' utility", type.getSimpleName());

						final Constructor<?> constructor = type.getDeclaredConstructor();

						assertThat(constructor).isNotNull();
						assertThat(constructor.isAccessible()).isFalse();

						constructor.setAccessible(true);
						assertThat(constructor.isAccessible()).isTrue();

						Exception expected = null;

						try {
								constructor.newInstance();
						} catch (InvocationTargetException e) {
								expected = e;
						}

						assertThat(expected).isNotNull();
						assertThat(expected).hasCauseExactlyInstanceOf(UnsupportedOperationException.class);
						constructor.setAccessible(false);
				}
		}
}
