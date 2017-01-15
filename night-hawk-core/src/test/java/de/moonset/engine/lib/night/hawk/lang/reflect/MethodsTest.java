package de.moonset.engine.lib.night.hawk.lang.reflect;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by pitt on 14.01.17.
 */
public class MethodsTest {

		@Test
		public void isAbstract() throws Exception {
				final Method method = Methods.getMethodByName(TestInterface.class, "test")[0];
				assertThat(Methods.isAbstract(method)).isTrue();

				final Method other = Methods.getMethodByName(TestInterface.class, "testDefault")[0];
				assertThat(Methods.isAbstract(other)).isFalse();
		}

		@Test
		public void testGetMethods() throws Exception {
				final Method[] actual = Methods.getMethodByName(TestInterface.class, "testDefault");
				assertThat(actual).isNotEmpty();
				assertThat(actual.length).isEqualTo(1);
				assertThat(actual[0].getName()).isEqualTo("testDefault");
		}

		private interface TestInterface {

				default void testDefault() { }

				void test();
		}
}
