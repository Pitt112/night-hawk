package de.moonset.engine.lib.night.hawk.lang.event;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by pitt on 20.12.16.
 */
public class EventListenersTest {
		@Test
		public void add() throws Exception {
				final EventListeners<EventReceiver> listeners = new EventListeners<>();
				final EventReceiver                 listener  = new TestClass();

				assertThat(listeners.add(listener)).isTrue();
				assertThat(listeners.add(listener)).isFalse();

				assertThat(listeners.size()).isEqualTo(1);
				assertThat(listeners).containsOnly(listener);
		}

		@Test
		public void remove() throws Exception {
				final EventListeners<EventReceiver> listeners = new EventListeners<>();
				final EventReceiver                 listener  = new TestClass();

				assertThat(listeners.remove(listener)).isFalse();

				assertThat(listeners.add(listener)).isTrue();
				assertThat(listeners.add(listener)).isFalse();

				assertThat(listeners.size()).isEqualTo(1);
				assertThat(listeners).containsOnly(listener);

				assertThat(listeners.remove(listener)).isTrue();
				assertThat(listeners.remove(listener)).isFalse();
				assertThat(listeners.size()).isEqualTo(0);
				assertThat(listeners).isEmpty();

		}

		@Test
		public void size() throws Exception {

		}

		@Test
		public void iterator() throws Exception {

		}

		private interface EventReceiver {

		}


		private static class TestClass implements EventReceiver {}
}
