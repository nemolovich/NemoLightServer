package fr.nemolovich.apps.nemolight.reflection;

import java.lang.annotation.Annotation;

public class AnnotationTypeFilter extends SearchFilter {

    private final Class<? extends Annotation> annotation;

    public AnnotationTypeFilter(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean filterMatches(Class<?> clazz) {
        return clazz
            .isAnnotationPresent((Class<? extends Annotation>) this.annotation);
    }

}
