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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by pitt on 16.02.17.
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

		private static void lockReadWriteInterruptiblyInterruptedRun(
				final Guard guard, final InterruptableConsumer<InterruptableRunnable> impl
		) throws InterruptedException, BrokenBarrierException {


				final Occupy occupy = new Occupy(guard);
				occupy.submit();
				occupy.start.await();

				AtomicBoolean  interrupted = new AtomicBoolean();
				CountDownLatch start       = new CountDownLatch(1);
				CountDownLatch started     = new CountDownLatch(1);
				CountDownLatch stopped     = new CountDownLatch(1);
				CountDownLatch stopping    = new CountDownLatch(1);
				Future<?> f = service.submit(() -> {
						try {
								start.await();
						} catch (InterruptedException e) {
								return;
						}
						try {
								started.countDown();
								impl.accept(() -> stopping.await());
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

		private static void lockReadWriteInterruptiblyInterruptedSupply(
				final Guard guard, final InterruptableFunction<Boolean, InterruptableSupplier<Boolean>> impl
		) throws InterruptedException, BrokenBarrierException {


				final Occupy occupy = new Occupy(guard);
				occupy.submit();
				occupy.start.await();

				AtomicBoolean  interrupted = new AtomicBoolean();
				CountDownLatch start       = new CountDownLatch(1);
				CountDownLatch started     = new CountDownLatch(1);
				CountDownLatch stopped     = new CountDownLatch(1);
				CountDownLatch stopping    = new CountDownLatch(1);


				Future<?> f = service.submit(() -> {
						try {
								start.await();
						} catch (InterruptedException e) {
								return;
						}
						try {
								started.countDown();
								impl.apply(() -> {
										stopping.await();
										return true;
								});
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


		private static void tryLockReadWriteSuccessSupply(final Function<Supplier<Boolean>, Optional<Boolean>> impl) {
				assertThat(impl.apply(() -> true).isPresent()).isTrue();
		}

		private static void tryLockReadWriteSuccessRun(final Predicate<Runnable> impl) {
				assertThat(impl.test(() -> {})).isTrue();
		}

		private static void lockReadWriteInterruptiblySuccessRun(InterruptableConsumer<InterruptableRunnable> impl) {

				final AtomicBoolean failed    = new AtomicBoolean();
				final AtomicBoolean completed = new AtomicBoolean();

				try {
						impl.accept(() -> completed.set(true));
				} catch (InterruptedException e) {
						failed.set(true);
				}

				assertThat(failed.get()).isFalse();
				assertThat(completed.get()).isTrue();
		}

		private static void lockReadWriteInterruptiblySuccessSupply(
				InterruptableFunction<Boolean, InterruptableSupplier<Boolean>> impl
		) {

				final AtomicBoolean failed    = new AtomicBoolean();
				final AtomicBoolean completed = new AtomicBoolean();
				final AtomicBoolean result    = new AtomicBoolean();

				try {
						boolean tmp = impl.apply(() -> {
								completed.set(true);
								return true;
						});

						result.set(tmp);
				} catch (InterruptedException e) {
						failed.set(true);
				}

				assertThat(failed.get()).isFalse();
				assertThat(completed.get()).isTrue();
				assertThat(result.get()).isTrue();
		}

		private static void tryLockReadWriteInterruptiblySuccessRun(final Predicate<Runnable> impl) {
				assertThat(impl.test(() -> {})).isTrue();
		}

		private static void tryLockReadWriteFailRun(final Guard guard, final Predicate<Runnable> impl)
				throws InterruptedException, BrokenBarrierException {

				final Occupy occupy = new Occupy(guard);
				occupy.submit();

				occupy.start.await();

				assertThat(impl.test(() -> {
						occupy.failed.set(true);
				})).isFalse();

				occupy.stop.countDown();
				occupy.stopped.await();
				assertThat(occupy.failed.get()).isFalse();
		}

		private static void tryLockReadWriteFailSupply(
				final Guard guard, final Function<Supplier<Boolean>, Optional<Boolean>> impl
		) throws InterruptedException, BrokenBarrierException {
				final Occupy occupy = new Occupy(guard);
				occupy.submit();
				occupy.start.await();

				assertThat(impl.apply(() -> {
						occupy.failed.set(true);
						return true;
				}).isPresent()).isFalse();

				occupy.stop.countDown();
				occupy.stopped.await();
				assertThat(occupy.failed.get()).isFalse();
		}

		@Test
		public void tryLockReadWriteSuccessRun() {
				Guard guard = Guards.newReadWrite();
				tryLockReadWriteSuccessRun(guard::tryRead);
				tryLockReadWriteSuccessRun(guard::tryWrite);
		}

		@Test
		public void tryLockReadWriteFailRun() throws BrokenBarrierException, InterruptedException {
				Guard guard = Guards.newReadWrite();
				tryLockReadWriteFailRun(guard, guard::tryRead);
				tryLockReadWriteFailRun(guard, guard::tryWrite);
		}

		@Test
		public void tryLockReadWriteSuccessSupply() {
				Guard guard = Guards.newReadWrite();
				tryLockReadWriteSuccessSupply(guard::tryRead);
				tryLockReadWriteSuccessSupply(guard::tryWrite);
		}

		@Test
		public void tryLockReadWriteFailSupply() throws BrokenBarrierException, InterruptedException {
				Guard guard = Guards.newReadWrite();
				tryLockReadWriteFailSupply(guard, guard::tryRead);
				tryLockReadWriteFailSupply(guard, guard::tryWrite);
		}

		@Test
		public void lockReadWriteInterruptiblySuccessRun() {
				Guard guard = Guards.newReadWrite(new ReentrantReadWriteLock());
				lockReadWriteInterruptiblySuccessRun(guard::readInterruptibly);
				lockReadWriteInterruptiblySuccessRun(guard::writeInterruptibly);
		}

		@Test
		public void lockReadWriteInterruptiblyInterruptedRun() throws BrokenBarrierException, InterruptedException {
				Guard guard = Guards.newReadWrite(new ReentrantReadWriteLock());
				lockReadWriteInterruptiblyInterruptedRun(guard, guard::readInterruptibly);
				lockReadWriteInterruptiblyInterruptedRun(guard, guard::writeInterruptibly);
		}

		@Test
		public void lockReadWriteInterruptiblySuccessSupply() {
				Guard guard = Guards.newReadWrite(new ReentrantReadWriteLock());
				lockReadWriteInterruptiblySuccessSupply(guard::readInterruptibly);
				lockReadWriteInterruptiblySuccessSupply(guard::writeInterruptibly);
		}

		@Test
		public void lockReadWriteInterruptiblyInterruptedSupply() throws BrokenBarrierException, InterruptedException {
				Guard guard = Guards.newReadWrite(new ReentrantReadWriteLock());
				lockReadWriteInterruptiblyInterruptedSupply(guard, guard::readInterruptibly);
				lockReadWriteInterruptiblyInterruptedSupply(guard, guard::writeInterruptibly);
		}

		@FunctionalInterface
		private interface InterruptableConsumer<T> {
				void accept(T t) throws InterruptedException;
		}


		@FunctionalInterface
		private interface InterruptableFunction<R, T> {
				R apply(T t) throws InterruptedException;
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
