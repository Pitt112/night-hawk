package de.moonset.engine.lib.night.hawk.concurrent;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by pitt on 19.02.17.
 */
public class GuardsTimeoutTest {

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

		@Test
		public void tryReadWriteTimeoutRunSuccess() throws InterruptedException {
				final Guard guard = Guards.newReadWrite();
				tryReadWriteTimeoutRunSuccess(guard::tryRead);
				tryReadWriteTimeoutRunSuccess(guard::tryWrite);
		}

		@Test
		public void tryReadWriteTimeoutSupplySuccess() throws InterruptedException {

				final Guard guard = Guards.newReadWrite();
				tryReadWriteTimeoutSupplySuccess(guard::tryRead);
				tryReadWriteTimeoutSupplySuccess(guard::tryWrite);
		}

		@Test
		public void tryReadWriteTimeoutSupplyTimeout() throws InterruptedException, BrokenBarrierException {
				final Guard guard = Guards.newReadWrite();
				tryReadWriteTimeoutSupplyTimeout(guard, guard::tryRead);
				tryReadWriteTimeoutSupplyTimeout(guard, guard::tryWrite);
		}

		@Test
		public void tryReadWriteTimeoutRunTimeout() throws InterruptedException, BrokenBarrierException {
				final Guard guard = Guards.newReadWrite();
				tryReadWriteTimeoutRunTimeout(guard, guard::tryRead);
				tryReadWriteTimeoutRunTimeout(guard, guard::tryWrite);
		}


		@Test
		public void tryReadWriteTimeoutRunInterrupted() throws InterruptedException, BrokenBarrierException {
				final Guard guard = Guards.newReadWrite();
				tryReadWriteTimeoutRunInterrupted(guard, guard::tryRead);
				tryReadWriteTimeoutRunInterrupted(guard, guard::tryWrite);
		}

		@Test
		public void tryReadWriteTimeoutSupplyInterrupted() throws InterruptedException, BrokenBarrierException {
				final Guard guard = Guards.newReadWrite();
				tryReadWriteTimeoutSupplyInterrupted(guard, guard::tryRead);
				tryReadWriteTimeoutSupplyInterrupted(guard, guard::tryWrite);
		}

		private void tryReadWriteTimeoutRunInterrupted(final Guard guard, final TryRunImpl impl)
				throws BrokenBarrierException, InterruptedException {

				final Occupy occupy = new Occupy(guard);
				occupy.submit();
				occupy.start.await();

				AtomicBoolean  interrupted = new AtomicBoolean();
				AtomicBoolean  success     = new AtomicBoolean();
				CountDownLatch start       = new CountDownLatch(1);
				CountDownLatch started     = new CountDownLatch(1);
				CountDownLatch stopped     = new CountDownLatch(1);

				Future<?> f = service.submit(() -> {
						try {
								start.await();
						} catch (InterruptedException e) {
								return;
						}
						try {
								started.countDown();
								impl.run(() -> {
										Thread.sleep(1000);
										success.set(true);
								}, 1, TimeUnit.DAYS);
						} catch (InterruptedException e) {
								interrupted.set(true);
						} finally {
								stopped.countDown();
						}
				});

				start.countDown();
				started.await();
				f.cancel(true);
				stopped.await();
				occupy.stop.countDown();
				occupy.stopped.await();

				assertThat(interrupted.get()).isTrue();
		}

		private void tryReadWriteTimeoutSupplyInterrupted(final Guard guard, final TrySupplyImpl impl)
				throws BrokenBarrierException, InterruptedException {

				final Occupy occupy = new Occupy(guard);
				occupy.submit();
				occupy.start.await();

				AtomicBoolean  interrupted = new AtomicBoolean();
				CountDownLatch start       = new CountDownLatch(1);
				CountDownLatch started     = new CountDownLatch(1);
				CountDownLatch stopped     = new CountDownLatch(1);

				Future<?> f = service.submit(() -> {
						try {
								start.await();
						} catch (InterruptedException e) {
								return;
						}
						try {
								started.countDown();
								impl.apply(() -> {
										Thread.sleep(1000);
										return true;
								}, 1, TimeUnit.DAYS);
						} catch (InterruptedException e) {
								interrupted.set(true);
						} finally {
								stopped.countDown();
						}
				});

				start.countDown();
				started.await();
				f.cancel(true);
				stopped.await();
				occupy.stop.countDown();
				occupy.stopped.await();

				assertThat(interrupted.get()).isTrue();
		}

		private void tryReadWriteTimeoutRunSuccess(final TryRunImpl impl) throws InterruptedException {
				AtomicBoolean success = new AtomicBoolean();

				impl.run(() -> {
						success.set(true);
				}, 100, TimeUnit.MILLISECONDS);

				assertThat(success.get()).isTrue();
		}


		private void tryReadWriteTimeoutSupplySuccess(final TrySupplyImpl impl) throws InterruptedException {

				Optional<Boolean> result = impl.apply(() -> true, 100, TimeUnit.MILLISECONDS);

				assertThat(result.isPresent()).isTrue();
				assertThat(result.get()).isTrue();
		}

		private void tryReadWriteTimeoutRunTimeout(final Guard guard, final TryRunImpl impl)
				throws InterruptedException, BrokenBarrierException {

				final AtomicBoolean success = new AtomicBoolean();

				final Occupy occupy = new Occupy(guard);
				occupy.submit();
				occupy.start.await();

				boolean result = impl.run(() -> success.set(true), 200, TimeUnit.MILLISECONDS);

				assertThat(result).isFalse();
				assertThat(success.get()).isFalse();
				occupy.stop.countDown();
				occupy.stopped.await();
		}

		private void tryReadWriteTimeoutSupplyTimeout(final Guard guard, final TrySupplyImpl impl)
				throws InterruptedException, BrokenBarrierException {

				final Occupy occupy = new Occupy(guard);
				occupy.submit();
				occupy.start.await();

				Optional<Boolean> result = impl.apply(() -> true, 200, TimeUnit.MILLISECONDS);

				assertThat(result.isPresent()).isFalse();
				occupy.stop.countDown();
				occupy.stopped.await();
		}

		@FunctionalInterface
		private interface TrySupplyImpl {
				Optional<Boolean> apply(InterruptableSupplier<Boolean> op, long timeout, TimeUnit unit)
						throws InterruptedException;
		}


		@FunctionalInterface
		private interface TryRunImpl {
				boolean run(InterruptableRunnable op, long timeout, TimeUnit unit) throws InterruptedException;
		}


		private static class Occupy {

				final CyclicBarrier  start       = new CyclicBarrier(2);
				final CountDownLatch stop        = new CountDownLatch(1);
				final CountDownLatch stopped     = new CountDownLatch(1);
				final AtomicBoolean  failed      = new AtomicBoolean();
				final AtomicBoolean  interrupted = new AtomicBoolean();

				private final Guard guard;

				Occupy(final Guard guard) {
						this.guard = guard;
				}

				Future<?> submit() {
						return service.submit(() -> {
								guard.write(() -> {
										try {
												start.await();
												stop.await();
										} catch (RuntimeException | BrokenBarrierException exec) {
												failed.set(true);
										} catch (InterruptedException ignored) {
												interrupted.set(true);
										} finally {
												stopped.countDown();
										}
								});
						});
				}
		}
}
