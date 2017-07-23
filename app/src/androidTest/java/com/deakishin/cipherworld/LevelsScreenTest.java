package com.deakishin.cipherworld;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.deakishin.cipherworld.gui.levelsscreen.levels.LevelsActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LevelsScreenTest {

    @Rule
    public ActivityTestRule<LevelsActivity> mActivityTestRule = new ActivityTestRule<>(LevelsActivity.class);

    @Test
    public void levelsActivityTest() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.levels_screen_next_level_imageButton), withContentDescription("Next level"), isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.single_level_lock_panel),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.levels_screen_prev_level_imageButton), withContentDescription("Previous level"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.levels_screen_level_title_textView),
                        isDisplayed()));
        textView.check(matches(withText("Level 2")));

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.levels_screen_prev_level_imageButton), withContentDescription("Previous level"), isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.levels_screen_level_title_textView),
                        isDisplayed()));
        textView2.check(matches(withText("Level 1")));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.single_level_ciphers_recyclerView),
                        childAtPosition(
                                allOf(withId(R.id.single_level_ciphers_panel),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));

        ViewInteraction frameLayout = onView(
                allOf(withId(R.id.single_level_cipher_item_panel),
                        childAtPosition(
                                allOf(withId(R.id.single_level_ciphers_recyclerView),
                                        childAtPosition(
                                                withId(R.id.single_level_ciphers_panel),
                                                0)),
                                0),
                        isDisplayed()));
        frameLayout.check(matches(isDisplayed()));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.single_level_cipher_item_button), withText("1"),
                        withParent(allOf(withId(R.id.single_level_cipher_item_panel),
                                withParent(withId(R.id.single_level_ciphers_recyclerView)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.cipher_screen_cipher_title_textView),
                        isDisplayed()));
        textView3.check(matches(withText("1-01")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
