package de.moonset.engine.lib.night.hawk.lang;

import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by pitt on 20.12.16.
 */
public class BitMasksTest {
		@Test
		public void toSetEmpty() throws Exception {
				final Set<TestEnum> flags = BitMasks.toSet(TestEnum.class, 0);
				assertThat(flags).isEmpty();
		}

		@Test
		public void toSetUnknownEmpty() throws Exception {
				final Set<TestEnum> flags = BitMasks.toSet(TestEnum.class, 0b010);
				assertThat(flags).isEmpty();
		}

		@Test(expected = IllegalArgumentException.class)
		public void toSetUnknownEmptyStrict() throws Exception {
				final Set<TestEnum> flags = BitMasks.toSet(TestEnum.class, 0b1000, true);
				assertThat(flags).isEmpty();
		}

		@Test
		public void toSet() throws Exception {
				final Set<TestEnum> flags = BitMasks.toSet(TestEnum.class, 0b1101);
				assertThat(flags).containsOnly(TestEnum.ADD, TestEnum.REMOVE);
		}

		@Test
		public void toSetStrict() throws Exception {
				final Set<TestEnum> flags = BitMasks.toSet(TestEnum.class, 0b100010101, true);
				assertThat(flags).containsOnly(TestEnum.ADD, TestEnum.REMOVE, TestEnum.CREATE, TestEnum.UPDATE);
		}

		@Test
		public void toSetNonStrict() throws Exception {
				final Set<TestEnum> flags = BitMasks.toSet(TestEnum.class, 0b100010101, false);
				assertThat(flags).containsOnly(TestEnum.ADD, TestEnum.REMOVE, TestEnum.CREATE, TestEnum.UPDATE);
		}

		@Test
		public void toBitMask() throws Exception {
				final long bitMask = BitMasks.toBitMask(TestEnum.ADD);
				assertThat(bitMask).isEqualTo(1L);
		}

		@Test
		public void toBitMask2() throws Exception {
				final long bitMask = BitMasks.toBitMask(TestEnum.ADD, TestEnum.ADD);
				assertThat(bitMask).isEqualTo(1L);
		}

		@Test
		public void toBitMask3() throws Exception {
				final long bitMask = BitMasks.toBitMask(TestEnum.ADD, TestEnum.ADD, TestEnum.REMOVE);
				assertThat(bitMask).isEqualTo(5L);
		}

		@Test
		public void toBitMask4() throws Exception {
				final long bitMask = BitMasks.toBitMask(TestEnum.ADD, TestEnum.ADD, TestEnum.REMOVE, TestEnum.CREATE);
				assertThat(bitMask).isEqualTo(21L);
		}

		@Test
		public void toBitMask5() throws Exception {
				final long bitMask =
						BitMasks.toBitMask(TestEnum.ADD, TestEnum.ADD, TestEnum.REMOVE, TestEnum.CREATE, TestEnum.CREATE);
				assertThat(bitMask).isEqualTo(21L);
		}

		@Test
		public void toBitMask6() throws Exception {
				final long bitMask = BitMasks.toBitMask(TestEnum.ADD,
				                                        TestEnum.ADD,
				                                        TestEnum.REMOVE,
				                                        TestEnum.CREATE,
				                                        TestEnum.CREATE,
				                                        TestEnum.UPDATE);
				assertThat(bitMask).isEqualTo(277L);
		}

		@Test
		public void toBitMask7() throws Exception {
				final long bitMask = BitMasks.toBitMask(TestEnum.ADD,
				                                        TestEnum.ADD,
				                                        TestEnum.REMOVE,
				                                        TestEnum.CREATE,
				                                        TestEnum.CREATE,
				                                        TestEnum.UPDATE,
				                                        TestEnum.REMOVE);
				assertThat(bitMask).isEqualTo(277L);
		}

		@Test
		public void toBitMaskIterable() throws Exception {
				final long bitMask = BitMasks.toBitMask(Arrays.asList(TestEnum.ADD,
				                                                      TestEnum.ADD,
				                                                      TestEnum.REMOVE,
				                                                      TestEnum.CREATE,
				                                                      TestEnum.CREATE,
				                                                      TestEnum.UPDATE,
				                                                      TestEnum.REMOVE));
				assertThat(bitMask).isEqualTo(277L);
		}



		private enum TestEnum implements BitMaskFlag {
				ADD(0),
				REMOVE(2),
				CREATE(4),
				UPDATE(8);

				private final int field;

				TestEnum(final int field) {
						this.field = field;
				}

				@Override
				public int field() {
						return field;
				}
		}
}
