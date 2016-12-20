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
				assertThat(dispatcher.delegate().testBoolean());
				assertThat(count.get()).isEqualTo(2);
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
				final EventReceiver listener = new EventReceiver(null);
				assertThat(dispatcher.addListener(listener)).isTrue();
				assertThat(dispatcher.addListener(listener)).isFalse();
				assertThat(dispatcher.removeListener(listener)).isTrue();
				assertThat(dispatcher.removeListener(listener)).isFalse();
		}

		private interface EventListener {
				boolean testBoolean();

				void testVoid();

				Object testObject();
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
