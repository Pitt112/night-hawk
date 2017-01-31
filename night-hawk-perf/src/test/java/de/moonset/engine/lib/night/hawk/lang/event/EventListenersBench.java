package de.moonset.engine.lib.night.hawk.lang.event;

import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.profile.ProfilerException;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.CopyOnWriteArraySet;

public class EventListenersBench {

		@Test
		public void runBench() throws RunnerException, ProfilerException {
				Options opt = new OptionsBuilder()
						.include(this.getClass().getCanonicalName())
						.forks(1)
						.addProfiler(StackProfiler.class)
						.build();
				new Runner(opt).run();
		}

		@Benchmark
		@Warmup(iterations = 5, time = 1)
		public void measureListeners(Context context, Blackhole blackhole) {
				final Consumer consumer = blackhole::consume;
				context.event.add(consumer);

				for (final Consumer c : context.event) {
						c.consume("Hallo");
						c.consume("Hallo1");
				}

				context.event.remove(consumer);
		}

		@Benchmark
		@Warmup(iterations = 5, time = 1)
		public void measureListeners2(Context2 context, Blackhole blackhole) {
				final Consumer consumer = blackhole::consume;
				context.event.add(consumer);

				for (final Consumer c : context.event) {
						c.consume("Hallo");
						c.consume("Hallo1");
				}

				context.event.remove(consumer);
		}

		public interface Consumer {
				void consume(String message);
		}


		@State(Scope.Thread)
		public static class Context {
				EventListeners<Consumer> event;

				@Setup
				public void setup(Blackhole blackhole) {
						event = new EventListeners<>();
						event.add(blackhole::consume);
						event.add(blackhole::consume);
						event.add(blackhole::consume);

				}

				@TearDown
				public void tearDown() {
						event = null;
				}
		}


		@State(Scope.Thread)
		public static class Context2 {
				EventListeners<Consumer> event;

				@Setup
				public void setup(Blackhole blackhole) {

						event = new EventListeners<>(new CopyOnWriteArraySet<>());
						event.add(blackhole::consume);
						event.add(blackhole::consume);
						event.add(blackhole::consume);

				}

				@TearDown
				public void tearDown() {
						event = null;
				}
		}
}
