package de.moonset.engine.lib.night.hawk.lang.event;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by pitt on 20.12.16.
 */
public class ProxyDispatcherTest {
		@Test
		public void delegateBoolean() throws Exception {

				final EventDispatcher<EventListener> dispatcher = ProxyDispatcher.create(EventListener.class);
				final AtomicInteger                  count      = new AtomicInteger();

				assertThat(dispatcher.addListener(new EventReceiver(count))).isTrue();
				assertThat(dispatcher.addListener(new EventReceiver(count))).isTrue();

				assertThat(count.get()).isEqualTo(0);
				assertThat(dispatcher.delegate().testBoolean()).isTrue();
				assertThat(count.get()).isEqualTo(2);
		}

		@Test
		public void delegateEmptyDispatch() throws Exception {
				final EventDispatcher<EventListener> dispatcher = ProxyDispatcher.create(EventListener.class);
				assertThat(dispatcher.delegate().testBoolean()).isFalse();
				dispatcher.delegate().testVoid();
		}

		@Test(expected = UnsupportedOperationException.class)
		public void delegateObject() throws Exception {

				final EventDispatcher<EventListener> dispatcher = ProxyDispatcher.create(EventListener.class);
				dispatcher.delegate().testObject();
		}

		@Test
		public void delegateVoid() throws Exception {

				final EventDispatcher<EventListener> dispatcher = ProxyDispatcher.create(EventListener.class);
				final AtomicInteger                  count      = new AtomicInteger();

				assertThat(dispatcher.addListener(new EventReceiver(count))).isTrue();
				assertThat(dispatcher.addListener(new EventReceiver(count))).isTrue();

				assertThat(count.get()).isEqualTo(0);
				dispatcher.delegate().testVoid();
				assertThat(count.get()).isEqualTo(2);
		}

		@Test
		public void testAddRemove() throws Exception {
				final EventDispatcher<EventListener> dispatcher = ProxyDispatcher.create(EventListener.class);
				final EventListener                  listener   = new EventReceiver(null);
				assertThat(dispatcher.addListener(listener)).isTrue();
				assertThat(dispatcher.addListener(listener)).isFalse();
				assertThat(dispatcher.removeListener(listener)).isTrue();
				assertThat(dispatcher.removeListener(listener)).isFalse();
		}

		@Test(expected = IllegalStateException.class)
		public void testThrowSingleBoolean() {
				final AtomicInteger count = new AtomicInteger();
				try {
						final EventDispatcher<EventListener> dispatcher = ProxyDispatcher.create(EventListener.class);
						final EventListener                  listener   = new EventReceiver(count);
						final EventListener                  throwing   = new ThrowingReceiver();

						dispatcher.addListener(listener);
						dispatcher.addListener(throwing);

						dispatcher.delegate().testBoolean();
				} finally {
						assertThat(count.get()).isEqualTo(1);
				}
		}

		@Test(expected = IllegalStateException.class)
		public void testThrowAggregateBoolean() {
				final AtomicInteger count = new AtomicInteger();
				try {
						final EventDispatcher<EventListener> dispatcher = ProxyDispatcher.create(EventListener.class);
						final EventListener                  listener   = new EventReceiver(count);
						final EventListener                  throwing   = new ThrowingReceiver();
						final EventListener                  throwing2  = new ThrowingReceiver();

						dispatcher.addListener(listener);
						dispatcher.addListener(throwing);
						dispatcher.addListener(throwing2);

						dispatcher.delegate().testBoolean();
				} catch (IllegalStateException ise) {
						assertThat(ise.getSuppressed()).hasSize(1);
						assertThat(count.get()).isEqualTo(1);
						throw ise;
				} finally {
						assertThat(count.get()).isEqualTo(1);
				}
		}

		@Test(expected = IllegalStateException.class)
		public void testThrowAggregateVoid() {
				final AtomicInteger count = new AtomicInteger();
				try {
						final EventDispatcher<EventListener> dispatcher = ProxyDispatcher.create(EventListener.class);
						final EventListener                  listener   = new EventReceiver(count);
						final EventListener                  throwing   = new ThrowingReceiver();
						final EventListener                  throwing2  = new ThrowingReceiver();

						dispatcher.addListener(listener);
						dispatcher.addListener(throwing);
						dispatcher.addListener(throwing2);

						dispatcher.delegate().testVoid();
				} catch (IllegalStateException ise) {
						assertThat(ise.getSuppressed()).hasSize(1);
						assertThat(count.get()).isEqualTo(1);
						throw ise;
				} finally {
						assertThat(count.get()).isEqualTo(1);
				}
		}


		@Test(expected = IllegalStateException.class)
		public void testThrowSingleVoid() {
				final AtomicInteger count = new AtomicInteger();
				try {
						final EventDispatcher<EventListener> dispatcher = ProxyDispatcher.create(EventListener.class);
						final EventListener                  listener   = new EventReceiver(count);
						final EventListener                  throwing   = new ThrowingReceiver();

						dispatcher.addListener(listener);
						dispatcher.addListener(throwing);

						dispatcher.delegate().testVoid();
				} finally {
						assertThat(count.get()).isEqualTo(1);
				}
		}


		private interface EventListener {
				boolean testBoolean();

				void testVoid();

				Object testObject();
		}


		private static class ThrowingReceiver implements EventListener {
				@Override
				public boolean testBoolean() {
						throw new IllegalStateException();
				}

				@Override
				public void testVoid() {
						throw new IllegalStateException();
				}

				@Override
				public Object testObject() {
						throw new IllegalStateException();
				}
		}


		private static class EventReceiver implements EventListener {

				private final AtomicInteger invocationCounter;

				public EventReceiver(final AtomicInteger invocationCounter) {
						this.invocationCounter = invocationCounter;
				}

				@Override
				public boolean testBoolean() {
						invocationCounter.incrementAndGet();
						return true;
				}

				@Override
				public void testVoid() {
						invocationCounter.incrementAndGet();
				}

				@Override
				public Object testObject() {
						invocationCounter.incrementAndGet();
						return new Object();
				}
		}
}
