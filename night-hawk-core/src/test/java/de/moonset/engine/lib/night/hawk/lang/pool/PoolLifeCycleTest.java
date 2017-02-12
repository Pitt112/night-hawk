package de.moonset.engine.lib.night.hawk.lang.pool;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by pitt on 12.02.17.
 */
public class PoolLifeCycleTest {

		private static final Logger LOGGER = LoggerFactory.getLogger(PoolLifeCycleTest.class);
		private static final Marker POOL   = MarkerFactory.getMarker("POOL");

		@Test
		public void testLifeCycle() throws Exception {
				final PoolLifeCycle<TestClass> cycle  = new PoolLifeCycle<>(TestClass.class);
				final TestClass                actual = new TestClass();

				assertThat(actual.destroyed.get()).isEqualTo(0);
				assertThat(actual.recycled.get()).isEqualTo(0);

				cycle.onDestroy(actual);
				assertThat(actual.recycled.get()).isEqualTo(1);
				assertThat(actual.destroyed.get()).isEqualTo(2);

				cycle.onRecycle(actual);
				assertThat(actual.recycled.get()).isEqualTo(3);
				assertThat(actual.destroyed.get()).isEqualTo(3);
		}

		@Test(expected = RuntimeException.class)
		public void testFail() throws Exception {

				final PoolLifeCycle<TestFailClass> cycle = new PoolLifeCycle<>(TestFailClass.class);
				final TestFailClass                obj   = new TestFailClass();

				cycle.onDestroy(obj);
		}

		public static class TestFailClass {
				@OnDestroy
				public void onFail() {
						throw new RuntimeException();
				}
		}


		public static class TestClass {

				private final AtomicInteger destroyed = new AtomicInteger();
				private final AtomicInteger recycled  = new AtomicInteger();

				@OnDestroy
				public void onDestroy() {
						LOGGER.info(POOL, "onDestroy");
						destroyed.incrementAndGet();
				}

				@OnRecycle
				public void onRecycle() {
						LOGGER.info(POOL, "onRecycle");
						recycled.incrementAndGet();
				}

				@OnRecycle
				@OnDestroy
				public void onCombined() {
						LOGGER.info(POOL, "onCombined");
						destroyed.incrementAndGet();
						recycled.incrementAndGet();
				}
		}

}
