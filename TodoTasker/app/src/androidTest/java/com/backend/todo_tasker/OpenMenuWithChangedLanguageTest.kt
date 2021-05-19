package com.backend.todo_tasker


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class OpenMenuWithChangedLanguageTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun openMenuWithChangedLanguageTest() {
        val actionMenuItemView = onView(
allOf(withId(R.id.language), withContentDescription("Change Language"),
childAtPosition(
childAtPosition(
withId(R.id.toolbar),
2),
0),
isDisplayed()))
        actionMenuItemView.perform(click())
        
        val appCompatImageButton = onView(
allOf(withContentDescription("Перейти вверх"),
childAtPosition(
allOf(withId(R.id.toolbar),
childAtPosition(
withId(R.id.tToolbar),
0)),
1),
isDisplayed()))
        appCompatImageButton.perform(click())
        
        val textView = onView(
allOf(withId(R.id.text_burgermenu_title), withText("Сделать Таскер"),
withParent(allOf(withId(R.id.layout_purple_zone),
withParent(IsInstanceOf.instanceOf(android.widget.RelativeLayout::class.java)))),
isDisplayed()))
        textView.check(matches(withText("Сделать Таскер")))
        }
    
    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
    }
