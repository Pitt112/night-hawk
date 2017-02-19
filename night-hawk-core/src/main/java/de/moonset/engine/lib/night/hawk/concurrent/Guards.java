package de.moonset.engine.lib.night.hawk.concurrent;

import com.google.common.base.Preconditions;
import de.moonset.engine.lib.night.hawk.lang.util.Utility;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * Created by pitt on 10.02.17.
 */

@Utility
public final class Guards {

		private Guards() { throw new UnsupportedOperationException(); }

		public static Guard newReadWrite() {
				return new ReadWriteLookGuard();
		}

		public static Guard newReadWrite(ReadWriteLock lock) {
				return new ReadWriteLookGuard(lock);
		}

		public static GuardLite newSynchronized() {
				return new SynchronizedGuard();
		}

		public static GuardLite newSynchronized(Object lock) {
				return new SynchronizedGuard(lock);
		}

		private static class ReadWriteLookGuard implements Guard {

				private final Lock read;
				private final Lock write;

				private ReadWriteLookGuard() {
						this(new ReentrantReadWriteLock());
				}

				private ReadWriteLookGuard(ReadWriteLock lock) {
						Preconditions.checkNotNull(lock, "lock");
						this.read = lock.readLock();
						this.write = lock.writeLock();
				}

				private static void lockedRun(Runnable op, Lock lock) {
						lock.lock();
						try {
								op.run();
						} finally {
								lock.unlock();
						}
				}

				private static <T> T lockedSupply(Supplier<T> op, Lock lock) {
						lock.lock();
						try {
								return op.get();
						} finally {
								lock.unlock();
						}
				}

				private static boolean lockedTryRun(Runnable op, Lock lock) {
						if (lock.tryLock()) {
								try {
										op.run();
								} finally {
										lock.unlock();
								}
								return true;
						}
						return false;
				}

				private static boolean lockedTryRun(InterruptableRunnable op, Lock lock, long timeout, TimeUnit unit)
						throws InterruptedException {
						if (lock.tryLock(timeout, unit)) {
								try {
										op.run();
								} finally {
										lock.unlock();
								}
								return true;
						}
						return false;
				}

				private static <T> Optional<T> lockedTrySupply(Supplier<T> op, Lock lock) {
						if (lock.tryLock()) {
								try {
										return Optional.ofNullable(op.get());
								} finally {
										lock.unlock();
								}
						}
						return Optional.empty();
				}

				private static <T> Optional<T> lockedTrySupply(InterruptableSupplier<T> op, Lock lock, long timeout, TimeUnit unit)
						throws InterruptedException {
						if (lock.tryLock(timeout, unit)) {
								try {
										return Optional.ofNullable(op.get());
								} finally {
										lock.unlock();
								}
						}
						return Optional.empty();
				}

				private static void lockedRunInterruptibly(final InterruptableRunnable op, final Lock lock) throws InterruptedException {
						lock.lockInterruptibly();
						try {
								op.run();
						} finally {
								lock.unlock();
						}
				}

				private static <T> T lockedSupplyInterruptibly(final InterruptableSupplier<T> op, final Lock lock)
						throws InterruptedException {
						lock.lockInterruptibly();
						try {
								return op.get();
						} finally {
								lock.unlock();
						}
				}

				@Override
				public void read(Runnable op) {
						lockedRun(op, read);
				}

				@Override
				public <T> T read(Supplier<T> op) {
						return lockedSupply(op, read);
				}

				@Override
				public void write(Runnable op) {
						lockedRun(op, write);
				}

				@Override
				public <T> T write(Supplier<T> code) {
						return lockedSupply(code, write);
				}

				@Override
				public boolean tryRead(final Runnable op) {
						return lockedTryRun(op, read);
				}

				@Override
				public boolean tryRead(final InterruptableRunnable op, final long timeout, final TimeUnit unit) throws InterruptedException {
						return lockedTryRun(op, read, timeout, unit);
				}

				@Override
				public <T> Optional<T> tryRead(final Supplier<T> op) {
						return lockedTrySupply(op, read);
				}

				@Override
				public boolean tryWrite(final Runnable op) {
						return lockedTryRun(op, write);
				}

				@Override
				public boolean tryWrite(final InterruptableRunnable op, final long timeout, final TimeUnit unit)
						throws InterruptedException {
						return lockedTryRun(op, write, timeout, unit);
				}

				@Override
				public <T> Optional<T> tryWrite(final Supplier<T> op) {
						return lockedTrySupply(op, write);
				}

				@Override
				public <T> Optional<T> tryRead(final InterruptableSupplier<T> op, final long timeout, final TimeUnit unit)
						throws InterruptedException {
						return lockedTrySupply(op, read, timeout, unit);
				}

				@Override
				public void readInterruptibly(final InterruptableRunnable op) throws InterruptedException {
						lockedRunInterruptibly(op, read);
				}

				@Override
				public <T> T readInterruptibly(final InterruptableSupplier<T> op) throws InterruptedException {
						return lockedSupplyInterruptibly(op, read);
				}

				@Override
				public void writeInterruptibly(final InterruptableRunnable op) throws InterruptedException {
						lockedRunInterruptibly(op, write);
				}

				@Override
				public <T> T writeInterruptibly(final InterruptableSupplier<T> op) throws InterruptedException {
						return lockedSupplyInterruptibly(op, write);
				}

				@Override
				public <T> Optional<T> tryWrite(final InterruptableSupplier<T> op, final long timeout, final TimeUnit unit)
						throws InterruptedException {
						return lockedTrySupply(op, write, timeout, unit);
				}
		}


		private static class SynchronizedGuard implements GuardLite {

				private final Object lock;

				private SynchronizedGuard() {
						this(new Object());
				}

				private SynchronizedGuard(final Object lock) {
						this.lock = Preconditions.checkNotNull(lock, "lock");
				}

				@Override
				public void read(final Runnable op) {
						synchronized(lock) {
								op.run();
						}
				}

				@Override
				public <T> T read(final Supplier<T> op) {
						final T result;
						synchronized(lock) {
								result = op.get();
						}
						return result;
				}

				@Override
				public void write(final Runnable op) {
						synchronized(lock) {
								op.run();
						}
				}

				@Override
				public <T> T write(final Supplier<T> op) {
						final T result;
						synchronized(lock) {
								result = op.get();
						}
						return result;
				}
		}
}
