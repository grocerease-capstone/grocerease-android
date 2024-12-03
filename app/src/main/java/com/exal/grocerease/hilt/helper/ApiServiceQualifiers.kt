package com.exal.grocerease.hilt.helper

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RegularApiService

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MlApiService