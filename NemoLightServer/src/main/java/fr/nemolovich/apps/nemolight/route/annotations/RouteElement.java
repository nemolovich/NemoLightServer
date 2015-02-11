package fr.nemolovich.apps.nemolight.route.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface RouteElement {

	String path();

	String page();

	boolean login() default false;

	boolean secured() default true;
}
