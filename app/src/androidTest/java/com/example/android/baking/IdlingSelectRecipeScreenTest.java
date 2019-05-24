package com.example.android.baking;

import android.os.SystemClock;

import androidx.fragment.app.FragmentTransaction;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.android.baking.fragments.SelectRecipeFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
public class IdlingSelectRecipeScreenTest {

    @Rule
    public ActivityTestRule<RecipeActivity> mRecipeActivityTestRule
            = new ActivityTestRule<>(RecipeActivity.class);

    private IdlingResource mIdlingResource;
    private IdlingRegistry mIdlingRegistry;
    private SelectRecipeFragment mTestFragment;

    @Before
    public void registerIdlingResource(){
        mTestFragment = startRecipeFragment();
        mIdlingResource = mTestFragment.getIdlingResource();
        mIdlingRegistry = IdlingRegistry.getInstance();
        mIdlingRegistry.register(mIdlingResource);
    }

    @Test
    public void clickRecyclerViewItem_OpensStepsActivity(){
        
        /* Perform click on recycler view position 0 after loading from network */
        onView(withId(R.id.recipe_recyclerview)).perform(actionOnItemAtPosition(0, click()));
    }

    @After
    public void unregisterIdlingResource(){
        if (mIdlingResource!=null){
            mIdlingRegistry.unregister(mIdlingResource);
        }
    }

    private SelectRecipeFragment startRecipeFragment(){
        FragmentTransaction ft = mRecipeActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
        SelectRecipeFragment selectRecipeFragment = new SelectRecipeFragment();
        ft.add(selectRecipeFragment, RecipeActivity.SELECT_RECIPE_FRAGMENT_KEY);
        ft.commit();
        return selectRecipeFragment;
    }

}
