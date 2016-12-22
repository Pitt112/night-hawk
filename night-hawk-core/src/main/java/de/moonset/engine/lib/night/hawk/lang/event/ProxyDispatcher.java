package de.moonset.engine.lib.night.hawk.lang.event;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by pitt on 17.12.16.
 */
public final class ProxyDispatcher<E> implements EventDispatcher<E> {

		private static final Logger LOGGER = LoggerFactory.getLogger(ProxyDispatcher.class);
		private static final Marker EVENT  = MarkerFactory.getMarker("EVENT");

		private final E                 delegateProxy;
		private final EventListeners<E> listeners;

		private ProxyDispatcher(Class<E> eventListenerType) {
				this.listeners = new EventListeners<>(8);
				this.delegateProxy = createProxy(eventListenerType);
		}

		@NotNull
		public static <T> EventDispatcher<T> create(Class<T> eventListenerType) {
				return new ProxyDispatcher<>(eventListenerType);
		}

		private E createProxy(final Class<E> eventListenerType) {
				final Object proxy = Proxy.newProxyInstance(eventListenerType.getClassLoader(),
				                                            new Class<?>[] {eventListenerType},
				                                            new DispatchHandler<>(listeners));
				return eventListenerType.cast(proxy);
		}

		@Override
		public E delegate() {
				return delegateProxy;
		}

		@Override
		public boolean addListener(final E listener) {
				return listeners.add(listener);
		}

		@Override
		public boolean removeListener(final E listener) {
				return listeners.remove(listener);
		}


		private static class DispatchHandler<E> implements InvocationHandler {

				private final EventListeners<E> listeners;

				DispatchHandler(final EventListeners<E> listeners) {
						this.listeners = listeners;
				}

				@Nullable
				private static Throwable packThrowables(@Nullable Throwable existing, @NotNull Throwable thrown) {

						if (existing != null) {
								existing.addSuppressed(thrown);
								return existing;
						}

						return thrown;
				}

				@Override
				public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {

						final Class<?> returnType = method.getReturnType();
						try {
								if (returnType == Boolean.TYPE) {
										return invokeBoolean(method, args);
								} else if (returnType == Void.TYPE) {
										return invokeVoid(method, args);
								}
						} catch (InvocationTargetException ite) {
								LOGGER.error(EVENT, "cannot dispatch method {}", method.getName(), ite);

								throw ite.getCause();
						}

						throw new UnsupportedOperationException(
								"cannot invoke method with return type of '" + returnType.getSimpleName() + "'");
				}

				private Object invokeVoid(final Method method, final Object[] args)
						throws InvocationTargetException, IllegalAccessException {

						Throwable throwable = null;

						for (E listener : listeners) {

								try {
										method.invoke(listener, args);
								} catch (InvocationTargetException e) {
										LOGGER.error(EVENT,
										             "cannot dispatch {}::{}(...) on {}",
										             listener.getClass().getSimpleName(),
										             method.getName(),
										             listener,
										             e);

										throwable = packThrowables(throwable, e.getCause());
								}
						}

						if (throwable != null) {
								throw new InvocationTargetException(throwable);
						}

						return null;
				}

				private Object invokeBoolean(final Method method, final Object[] args)
						throws InvocationTargetException, IllegalAccessException {

						boolean result = listeners.size() > 0;

						Throwable throwable = null;
						for (E listener : listeners) {

								try {
										result &= (boolean) method.invoke(listener, args);
								} catch (InvocationTargetException e) {
										LOGGER.error(EVENT,
										             "cannot dispatch {}::{}(...) on {}",
										             listener.getClass().getSimpleName(),
										             method.getName(),
										             listener,
										             e);
										result = false;

										throwable = packThrowables(throwable, e.getCause());
								}
						}

						if (throwable != null) {
								throw new InvocationTargetException(throwable);
						}

						return result;
				}
		}
}
