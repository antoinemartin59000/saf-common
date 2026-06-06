package com.antoinemartin59000.saf.common;

import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class SubClassCollector {

    public static <T> Set<Class<? extends T>> findAllSubclasses(Class<T> superClass) {

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forJavaClassPath())
                .setExpandSuperTypes(false) // faster, we only care about direct subclasses
        );

        return reflections.getSubTypesOf(superClass);
    }

}
