package de.moonset.engine.lib.night.hawk.lang.util;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Created by pitt on 18.01.17.
 */
public class ArraysTest {
		@Test
		public void matches() throws Exception {

				Integer[] a = {1, 2, 3, 4, 5};
				Integer[] b = {1, 2, 3, 4, 5};
				Integer[] c = {1, 2, 3, 4, 6};

				assertThat(Arrays.match(a, b, Integer::equals)).isTrue();
				assertThat(Arrays.match(a, c, Integer::equals)).isFalse();
		}

		@Test
		public void testMissMatch() {
				Integer[] a = {1, 2};
				Integer[] b = {2};

				assertThat(Arrays.match(a, b, Integer::equals)).isFalse();
		}
}
