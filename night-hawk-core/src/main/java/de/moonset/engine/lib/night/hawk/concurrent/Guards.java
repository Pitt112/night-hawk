package de.moonset.engine.lib.night.hawk.concurrent;

import com.google.common.base.Preconditions;
import de.moonset.engine.lib.night.hawk.lang.Utility;

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

		public static Guard newSynchronized() {
				return new SynchronizedGuard();
		}

		public static Guard newSynchronized(Object lock) {
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

				@Override
				public void read(Runnable code) {
						read.lock();
						try {
								code.run();
						} finally {
								read.unlock();
						}
				}

				@Override
				public <T> T read(Supplier<T> code) {
						read.lock();
						try {
								return code.get();
						} finally {
								read.unlock();
						}
				}

				@Override
				public void write(Runnable code) {
						write.lock();
						try {
								code.run();
						} finally {
								write.unlock();
						}
				}

				@Override
				public <T> T write(Supplier<T> code) {
						write.lock();
						try {
								return code.get();
						} finally {
								write.unlock();
						}
				}
		}


		private static class SynchronizedGuard implements Guard {

				private final Object lock;

				SynchronizedGuard() {
						this(new Object());
				}

				SynchronizedGuard(final Object lock) {
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
