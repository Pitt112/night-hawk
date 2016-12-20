package de.moonset.engine.lib.night.hawk.lang.event;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by pitt on 17.12.16.
 */
public final class ProxyDispatcher<E> implements EventDispatcher<E> {

		private final E                 delegateProxy;
		private final EventListeners<E> listeners;

		private ProxyDispatcher(Class<E> eventListenerType) {
				this.listeners = new EventListeners<E>(8);
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

				public DispatchHandler(final EventListeners<E> listeners) {
						this.listeners = listeners;
				}

				@Override
				public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {

						final Class<?> returnType = method.getReturnType();
						if (returnType == Boolean.TYPE) {
								return invokeBoolean(proxy, method, args);
						} else if (returnType == Void.TYPE) {
								return invokeVoid(proxy, method, args);
						}

						throw new UnsupportedOperationException(
								"cannot invoke method with return type of '" + returnType.getSimpleName() + "'");
				}

				private Object invokeVoid(final Object proxy, final Method method, final Object[] args) throws Throwable {

						Throwable aggregatedThrowable = null;

						for (E listener : listeners) {
								Throwable throwable = null;

								try {
										method.invoke(listener, args);
								} catch (IllegalAccessException | IllegalArgumentException e) {
										throwable = e;
								} catch (InvocationTargetException e) {										throwable = e.getCause();
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
								throw aggregatedThrowable;
						}

						return null;
				}

				private Object invokeBoolean(final Object proxy, final Method method, final Object[] args) throws Throwable {

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
								throw aggregatedThrowable;
						}

						return result;
				}
		}
}
