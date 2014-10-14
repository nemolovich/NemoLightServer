package fr.nemolovich.apps.homeapp.config.route;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fr.nemolovich.apps.homeapp.constants.RouteElementMethod;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface RouteElement {
	String path();

	String page();

	RouteElementMethod method() default RouteElementMethod.GET;
}
