package com.example.myapp

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.util.TreeIterables
import org.hamcrest.Matcher

fun findElement(@IdRes id: Int): ViewInteraction {
    return Espresso.onView(ViewMatchers.withId(id))
}

fun findElement(text: String): ViewInteraction {
    return Espresso.onView(ViewMatchers.withText(text))
}

fun fineElementHint(hint: String): ViewInteraction {
    return Espresso.onView(ViewMatchers.withHint(hint))
}

fun findSpinnerElement(text: String): ViewInteraction {
    return Espresso.onView(ViewMatchers.withText(text)).inRoot(RootMatchers.isPlatformPopup())
}

fun waitElement(text: String, waitMillis: Int = 5000): ViewInteraction {
    return EspressoExtensions.waitForView(ViewMatchers.withText(text), waitMillis)
}

fun waitElementId(@IdRes id: Int, waitMillis: Int = 5000): ViewInteraction {
    return EspressoExtensions.waitForView(ViewMatchers.withId(id), waitMillis)
}

fun ViewInteraction.tap() {
    perform(ViewActions.click())
}

fun ViewInteraction.typeText(text: String) {
    perform(ViewActions.typeText(text))
}

fun closeKeyboard() {
    Espresso.closeSoftKeyboard()
    // Give it a second for keyboard to close
    Thread.sleep(2000)
}


class EspressoExtensions {
    companion object {
        /**
         * Perform action of waiting for a certain view within a single root view
         * @param matcher Generic Matcher used to find our view
         */
        fun searchFor(matcher: Matcher<View>): ViewAction {

            return object : ViewAction {

                override fun getConstraints(): Matcher<View> {
                    return ViewMatchers.isRoot()
                }

                override fun getDescription(): String {
                    return "searching for view $matcher in the root view"
                }

                override fun perform(uiController: UiController, view: View) {

                    var tries = 0
                    val childViews: Iterable<View> = TreeIterables.breadthFirstViewTraversal(view)

                    // Look for the match in the tree of childviews
                    childViews.forEach {
                        tries++
                        if (matcher.matches(it)) {
                            // found the view
                            return
                        }
                    }

                    throw NoMatchingViewException.Builder()
                        .withRootView(view)
                        .withViewMatcher(matcher)
                        .build()
                }
            }
        }

        /**
         * Perform action of implicitly waiting for a certain view.
         * This differs from EspressoExtensions.searchFor in that,
         * upon failure to locate an element, it will fetch a new root view
         * in which to traverse searching for our @param match
         *
         * @param viewMatcher ViewMatcher used to find our view
         */
        fun waitForView(viewMatcher: Matcher<View>, waitMillis: Int = 5000, waitMillisPerTry: Long = 100): ViewInteraction {
            // Derive the max tries
            val maxTries = waitMillis / waitMillisPerTry.toInt()

            var tries = 0

            for (i in 0..maxTries)
                try {
                    // Track the amount of times we've tried
                    tries++

                    // Search the root for the view
                    Espresso.onView(ViewMatchers.isRoot()).perform(searchFor(viewMatcher))

                    // If we're here, we found our view. Now return it
                    return Espresso.onView(viewMatcher)

                } catch (e: Exception) {

                    if (tries == maxTries) {
                        throw e
                    }
                    Thread.sleep(waitMillisPerTry)
                }

            throw Exception("Error finding a view matching $viewMatcher")
        }
    }
}