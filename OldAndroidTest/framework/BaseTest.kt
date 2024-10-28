package com.mikolove.allmightworkout.framework

import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Rule

abstract class BaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    abstract fun injectTest()
}