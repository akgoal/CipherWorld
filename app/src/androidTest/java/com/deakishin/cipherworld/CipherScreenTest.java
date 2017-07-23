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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CipherScreenTest {

    @Rule
    public ActivityTestRule<LevelsActivity> mActivityTestRule = new ActivityTestRule<>(LevelsActivity.class);

    @Test
    public void cipherScreenTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.single_level_cipher_item_button), withText("1"),
                        withParent(allOf(withId(R.id.single_level_cipher_item_panel),
                                withParent(withId(R.id.single_level_ciphers_recyclerView)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.cipher_screen_background_view),
                        withParent(allOf(withId(R.id.contentFragmentContainer),
                                withParent(withId(R.id.levels_screen_background_panel)))),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.cipher_screen_cipher_title_textView),
                        isDisplayed()));
        textView.check(matches(withText("1-01")));

        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.cipher_screen_cipher_question_panel),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                0),
                        isDisplayed()));
        relativeLayout.check(matches(isDisplayed()));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cipher_symbols_recyclerView),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cipher_screen_background_view),
                                        0),
                                2),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));

        ViewInteraction squareFrameLayout = onView(
                allOf(
                        childAtPosition(
                                withId(R.id.cipher_symbols_recyclerView), 1),
                        isDisplayed()));
        squareFrameLayout.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.cipher_keyboard_recyclerView)));
        recyclerView2.check(matches(isDisplayed()));

        ViewInteraction view = onView(
                allOf(withId(R.id.cipher_symbol_item_highlight),
                        childAtPosition(
                                allOf(withId(R.id.cipher_symbol_item_panel),
                                        childAtPosition(
                                                childAtPosition(
                                                        withId(R.id.cipher_symbols_recyclerView), 1),
                                                0)),
                                1)));
        view.check(matches(isDisplayed()));

       ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.cipher_screen_hint_letter_imageButton),
                        withParent(withId(R.id.cipher_screen_hint_control_panel))));
        appCompatImageButton.check(matches(isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction linearLayout3 = onView(
                allOf(withId(R.id.cipher_screen_hint_panel)));
        linearLayout3.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.cipher_hint_panel_cancel_textView),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cipher_screen_hint_panel),
                                        1),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("CANCEL")));

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.cipher_hint_panel_cancel_textView), withText("CANCEL"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction keyboardView = onView(
                allOf(withId(R.id.cipher_screen_keyboard_panel)));
        keyboardView.check(matches(isDisplayed()));

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.cipher_keyboard_item_view), withText("A"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.cipher_screen_cipher_question_textView),
                        childAtPosition(
                                allOf(withId(R.id.cipher_screen_cipher_question_scrollView),
                                        childAtPosition(
                                                withId(R.id.cipher_screen_cipher_question_panel),
                                                0)),
                                0)));
        textView3.check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.cipher_screen_hint_check_letters_imageButton)));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.cipher_symbol_letter_textView),
                                childAtPosition(
                                        allOf(withId(R.id.cipher_symbol_item_panel),
                                                childAtPosition(
                                                        childAtPosition(
                                                                withId(R.id.cipher_symbols_recyclerView), 1),
                                                        0)),
                                        0),
                        isDisplayed()));
        textView4.check(matches(withText("A")));

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.cipher_screen_levels_imageButton), isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction linearLayout4 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.contentFragmentContainer),
                                0),
                        0)));
        linearLayout4.check(matches(isDisplayed()));

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
