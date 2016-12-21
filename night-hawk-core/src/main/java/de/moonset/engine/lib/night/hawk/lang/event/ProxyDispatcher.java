package de.moonset.engine.lib.night.hawk.lang.event;

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
								throw ite.getCause();
						}

						throw new UnsupportedOperationException(
								"cannot invoke method with return type of '" + returnType.getSimpleName() + "'");
				}

				private Object invokeVoid(final Method method, final Object[] args) throws InvocationTargetException {

						Throwable aggregatedThrowable = null;

						for (E listener : listeners) {
								Throwable throwable = null;

								try {
										method.invoke(listener, args);
								} catch (IllegalAccessException | IllegalArgumentException e) {
										throwable = e;
								} catch (InvocationTargetException e) {
										LOGGER.error(EVENT,
										             "cannot dispatch {}::{}(...) on {}",
										             listener.getClass().getSimpleName(),
										             method.getName(),
										             listener,
										             e);
										throwable = e.getCause();
								} finally {
										if (throwable != null) {
												if (aggregatedThrowable == null) {
														aggregatedThrowable = throwable;
												} else {
														aggregatedThrowable.addSuppressed(throwable);
												}
										}
								}
						}

						if (aggregatedThrowable != null) {
								throw new InvocationTargetException(aggregatedThrowable);
						}

						return null;
				}

				private Object invokeBoolean(final Method method, final Object[] args) throws InvocationTargetException {

						Throwable aggregatedThrowable = null;

						boolean result = listeners.size() > 0;


						for (E listener : listeners) {
								Throwable throwable = null;

								try {
										result &= (boolean) method.invoke(listener, args);
								} catch (IllegalAccessException | IllegalArgumentException e) {
										result = false;
										throwable = e;
								} catch (InvocationTargetException e) {
										LOGGER.error(EVENT,
										             "cannot dispatch {}::{}(...) on {}",
										             listener.getClass().getSimpleName(),
										             method.getName(),
										             listener,
										             e);
										result = false;
										throwable = e.getCause();
								} finally {
										if (throwable != null) {
												if (aggregatedThrowable == null) {
														aggregatedThrowable = throwable;
												} else {
														aggregatedThrowable.addSuppressed(throwable);
												}
										}
								}
						}

						if (aggregatedThrowable != null) {
								throw new InvocationTargetException(aggregatedThrowable);
						}

						return result;
				}
		}
}
