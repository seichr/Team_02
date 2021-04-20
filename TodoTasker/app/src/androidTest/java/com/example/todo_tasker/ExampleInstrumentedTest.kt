package com.example.todo_tasker



import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myapplication.MainActivity
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//Good ressources: https://developer.android.com/guide/components/activities/testing
//     UI Testing  https://developer.android.com/training/testing/ui-testing/espresso-testing

@MediumTest
@RunWith(AndroidJUnit4::class)
class SecondActivityTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.myapplication", appContext.packageName)
    }

    @Test
    fun Our_Example_Test() {
        val activityScenario: ActivityScenario<MainActivity> =
            ActivityScenario.launch(MainActivity::class.java)

        activityScenario.moveToState(Lifecycle.State.STARTED);
        //Activity start should load database
        /*
        Database b = //GETDB
        assert(b.count >3);
        */
        activityScenario.close();
    }
}