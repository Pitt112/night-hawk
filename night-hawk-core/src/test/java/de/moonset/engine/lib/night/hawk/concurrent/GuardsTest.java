package de.moonset.engine.lib.night.hawk.concurrent;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by pitt on 10.02.17.
 */
public class GuardsTest {

		private static ExecutorService service;

		@BeforeClass
		public static void setup() {
				service = Executors.newCachedThreadPool();
		}

		@AfterClass
		public static void teardown() {
				if (service != null) {
						service.shutdownNow();
						service = null;
				}
		}

		@Test(timeout = 2000)
		public void newReadWrite() throws Exception {
				testGuard(Guards.newReadWrite());
		}

		@Test(timeout = 2000)
		public void newReadWrite1() throws Exception {
				testGuard(Guards.newReadWrite(new ReentrantReadWriteLock()));
		}

		@Test(timeout = 7000)
		public void newSynchronized() throws Exception {
				testGuard(Guards.newSynchronized());
		}

		@Test(timeout = 7000)
		public void newSynchronized1() throws Exception {
				testGuard(Guards.newSynchronized(new Object()));
		}

		private void testGuard(Guard guard) throws InterruptedException {

				CountDownLatch latch = new CountDownLatch(10);

				guard.write(() -> 2);

				guard.write(() -> {
						service.submit(() -> guard.read(() -> LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(500))));

						for (int i = 0; i < 10; i++) {


								service.submit(() -> guard.read(() -> {
										LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(500));
										latch.countDown();
										return 1;
								}));
						}

						LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(500));
				});

				latch.await();
		}
}
