package de.moonset.engine.lib.night.hawk.lang.reflect;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Created by pitt on 15.01.17.
 */
public class InterfacesTest {

		@Test
		public void testIsFunctionalAnnotated() {
				assertThat(Interfaces.isFunctional(FunctionalAnnotated.class)).isTrue();
		}

		@Test
		public void testIsFunctional() {
				assertThat(Interfaces.isFunctional(Functional.class)).isTrue();
		}

		@Test
		public void testIsFunctionalDefault() {
				assertThat(Interfaces.isFunctional(FunctionalDefault.class)).isTrue();
		}

		@Test
		public void testNonFunctional() {
				assertThat(Interfaces.isFunctional(NonFunctional.class)).isFalse();
		}


		private interface Functional {
				void test();
		}

		private interface NonFunctional {
				void test();

				void test(boolean flag);
		}


		private interface FunctionalDefault {

				void test();

				default boolean testResult() { return false; }
		}


		@FunctionalInterface
		private interface FunctionalAnnotated {
				void test();
		}

}
