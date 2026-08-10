package com.alan.queensland.core.di

import me.tatarka.inject.annotations.Qualifier

@Qualifier
@Target(
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPE,
)
annotation class DispatcherMain
