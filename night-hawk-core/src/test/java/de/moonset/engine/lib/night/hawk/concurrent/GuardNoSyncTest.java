package de.moonset.engine.lib.night.hawk.concurrent;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by pitt on 21.02.17.
 */
public class GuardNoSyncTest {
		@NotNull
		private static Guard newGuard() {
				return Guards.newNoSync();
		}

		@Test
		public void readRun() throws Exception {
				AtomicBoolean success = new AtomicBoolean();

				newGuard().read(() -> success.set(true));
				assertThat(success.get()).isTrue();
		}

		@Test
		public void readSupply() throws Exception {
				assertThat(newGuard().read(() -> true)).isTrue();
		}

		@Test
		public void writeRun() throws Exception {
				AtomicBoolean success = new AtomicBoolean();

				newGuard().write(() -> success.set(true));
				assertThat(success.get()).isTrue();
		}

		@Test
		public void writeSupply() throws Exception {
				assertThat(newGuard().write(() -> true)).isTrue();
		}

		@Test
		public void readInterruptiblyRun() throws Exception {
				AtomicBoolean success = new AtomicBoolean();

				newGuard().readInterruptibly(() -> success.set(true));
				assertThat(success.get()).isTrue();
		}

		@Test
		public void readInterruptiblySupply() throws Exception {
				assertThat(newGuard().readInterruptibly(() -> true)).isTrue();
		}

		@Test
		public void writeInterruptiblyRun() throws Exception {
				AtomicBoolean success = new AtomicBoolean();

				newGuard().writeInterruptibly(() -> success.set(true));
				assertThat(success.get()).isTrue();
		}

		@Test
		public void writeInterruptiblySupply() throws Exception {
				assertThat(newGuard().writeInterruptibly(() -> true)).isTrue();
		}


		@Test
		public void tryReadRun() throws Exception {
				AtomicBoolean success = new AtomicBoolean();

				assertThat(newGuard().tryRead(() -> success.set(true)));
				assertThat(success.get()).isTrue();
		}

		@Test
		public void tryReadSupply() throws Exception {
				final Optional<Boolean> actual = newGuard().tryRead(() -> true);
				assertThat(actual.isPresent()).isTrue();
				assertThat(actual.get()).isTrue();
		}

		@Test
		public void tryWriteRun() throws Exception {
				AtomicBoolean success = new AtomicBoolean();

				assertThat(newGuard().tryWrite(() -> success.set(true)));
				assertThat(success.get()).isTrue();
		}

		@Test
		public void tryWriteSupply() throws Exception {
				final Optional<Boolean> actual = newGuard().tryWrite(() -> true);
				assertThat(actual.isPresent()).isTrue();
				assertThat(actual.get()).isTrue();
		}

		@Test
		public void tryReadRunTiimeout() throws Exception {
				AtomicBoolean success = new AtomicBoolean();

				assertThat(newGuard().tryRead(() -> success.set(true), 100, TimeUnit.MILLISECONDS));
				assertThat(success.get()).isTrue();
		}

		@Test
		public void tryReadSupplyTimeout() throws Exception {
				final Optional<Boolean> actual = newGuard().tryRead(() -> true, 100, TimeUnit.MILLISECONDS);
				assertThat(actual.isPresent()).isTrue();
				assertThat(actual.get()).isTrue();
		}

		@Test
		public void tryWriteRunTiimeout() throws Exception {
				AtomicBoolean success = new AtomicBoolean();

				assertThat(newGuard().tryWrite(() -> success.set(true), 100, TimeUnit.MILLISECONDS));
				assertThat(success.get()).isTrue();
		}

		@Test
		public void tryWriteSupplyTimeout() throws Exception {
				final Optional<Boolean> actual = newGuard().tryWrite(() -> true, 100, TimeUnit.MILLISECONDS);
				assertThat(actual.isPresent()).isTrue();
				assertThat(actual.get()).isTrue();
		}
}
