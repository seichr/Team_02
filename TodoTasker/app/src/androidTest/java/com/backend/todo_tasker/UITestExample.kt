package com.backend.todo_tasker

/*
 DISCLAIMER:
    This Testcase shows an example of a UI-Test and is only here to help guide devs!
    DO NOT DELETE
*/

/*
@RunWith(AndroidJUnit4::class)
@LargeTest
class UITestExample {
    private lateinit var stringToBetyped: String

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun initValidString() {
        // Specify a valid string.
        stringToBetyped = "Espresso"
    }

    @Test
    fun changeText_sameActivity() {
        // Type text and then press the button.
        onView(withId(R.id.editTextTextPersonName))
            .perform(typeText(stringToBetyped), closeSoftKeyboard())
        onView(withId(R.id.button)).perform(click())

        // Check that the text was changed.
        stringToBetyped = "NaEspressome"
        onView(withId(R.id.editTextTextPersonName))
            .check(matches(withText(stringToBetyped)))
    }

}*/