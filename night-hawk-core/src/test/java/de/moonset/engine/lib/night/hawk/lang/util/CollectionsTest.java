package de.moonset.engine.lib.night.hawk.lang.util;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by pitt on 11.02.17.
 */
public class CollectionsTest {
		@Test
		public void toArray() throws Exception {
				final List<Integer> input = java.util.Arrays.asList(1, 2, 3, 4, 5);
				final Integer[] actual = Collections.toArray(input, Integer[]::new);
				assertThat(actual).containsExactly(1, 2, 3, 4, 5);
		}

}
