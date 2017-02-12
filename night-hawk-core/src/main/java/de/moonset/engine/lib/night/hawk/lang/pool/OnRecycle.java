package de.moonset.engine.lib.night.hawk.lang.pool;

/**
 * Created by pitt on 30.01.17.
 */


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pitt
 *         <p>
 *         marks a method which should be called when an Object is being recycled
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnRecycle {}
