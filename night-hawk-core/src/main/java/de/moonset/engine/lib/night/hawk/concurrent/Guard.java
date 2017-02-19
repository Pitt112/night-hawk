package de.moonset.engine.lib.night.hawk.concurrent;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Created by pitt on 10.02.17.
 */
public interface Guard extends GuardLite {

		void readInterruptibly(InterruptableRunnable op) throws InterruptedException;

		<T> T readInterruptibly(InterruptableSupplier<T> op) throws InterruptedException;

		boolean tryRead(Runnable op);

		<T> Optional<T> tryRead(Supplier<T> op);


		boolean tryRead(InterruptableRunnable op, long timeout, TimeUnit unit) throws InterruptedException;

		<T> Optional<T> tryRead(InterruptableSupplier<T> op, long timeout, TimeUnit unit) throws InterruptedException;



		void writeInterruptibly(InterruptableRunnable op) throws InterruptedException;

		<T> T writeInterruptibly(InterruptableSupplier<T> op) throws InterruptedException;

		boolean tryWrite(Runnable op);

		<T> Optional<T> tryWrite(Supplier<T> op);

		boolean tryWrite(InterruptableRunnable op, long timeout, TimeUnit unit) throws InterruptedException;

		<T> Optional<T> tryWrite(InterruptableSupplier<T> op, long timeout, TimeUnit unit) throws InterruptedException;
}
