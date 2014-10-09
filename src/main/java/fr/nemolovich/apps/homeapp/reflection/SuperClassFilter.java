package fr.nemolovich.apps.homeapp.reflection;

public class SuperClassFilter extends SearchFilter {

	private final Class<?> superClass;

	public SuperClassFilter(Class<?> superClass) {
		this.superClass = superClass;
	}

	@Override
	public boolean filterMatches(Class<?> clazz) {
		return this.superClass.isAssignableFrom(clazz);
	}

}
