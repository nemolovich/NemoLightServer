package fr.nemolovich.apps.homapp.config.route;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import spark.Spark;
import fr.nemolovich.apps.homapp.reflection.AnnotationTypeFilter;
import fr.nemolovich.apps.homapp.reflection.ClassPathScanner;
import fr.nemolovich.apps.homeapp.route.pages.FreemarkerRoute;
import freemarker.template.Configuration;

public class DeploymentConfig {

	private static final DeploymentConfig INSTANCE;

	static {
		INSTANCE = new DeploymentConfig();
	}

	public static final DeploymentConfig getInstance() {
		return INSTANCE;
	}

	private DeploymentConfig() {
	}

	public final boolean initialize(Configuration config) {
		ClassPathScanner scanner = new ClassPathScanner();
		scanner.addIncludeFilter(new AnnotationTypeFilter(RouteElement.class));

		Vector<Class<?>> classes = null;
		try {
			Field f = ClassLoader.class.getDeclaredField("classes");
			f.setAccessible(true);
			classes = (Vector<Class<?>>) f.get(DeploymentConfig.class.getClassLoader());
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e1) {
			e1.printStackTrace();
		}
		
		System.out.println(classes);

		for (Class<?> c : scanner
				.findCandidateComponents("fr.nemolovich.apps.homapp")) {;
			try {
				if (!FreemarkerRoute.class.isAssignableFrom(c)) {
					throw new ClassCastException("Class " + c.getName()
							+ " does not implement "
							+ FreemarkerRoute.class.getName());
				}
				String path = c.getAnnotation(RouteElement.class).path();
				String page = c.getAnnotation(RouteElement.class).page();
				Constructor<?> cst = c.getConstructor(String.class,
						String.class, Configuration.class);
				Spark.get((FreemarkerRoute) cst.newInstance(path, page, config));

			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
