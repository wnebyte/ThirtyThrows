package com.github.wnebyte.thirtythrows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.github.wnebyte.thirtythrows.controller.ButtonBarFragment
import com.github.wnebyte.thirtythrows.controller.DieListFragment
import com.github.wnebyte.thirtythrows.model.DieListViewModel

private const val TAG = "MainActivity"

private const val MODEL = "model"

class MainActivity : AppCompatActivity(),
        ButtonBarFragment.ThrowButtonCallback,
        DieListFragment.EndOfRoundCallback {

    /** Fragment's and MainActivity's shared view model. */
    private val model: DieListViewModel by lazy {
        ViewModelProviders.of(this).get(DieListViewModel::class.java)
    }

    /** OnSaveInstanceState saves the state of the shared view model. */
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.d(TAG, "onSaveInstanceState() called")
        saveInstanceState(savedInstanceState)
    }

    /** OnCreates sets the contentView, restores the state of the shared view model,
     * and initiates the two fragments. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate() called")
        restoreSavedInstanceState(savedInstanceState)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_list_container)

        if (currentFragment == null) {
            val fragment = DieListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_list_container, fragment)
                .commit()
        }

        val buttonFragment =
                supportFragmentManager.findFragmentById(R.id.fragment_button_container)

        if (buttonFragment == null) {
            val fragment = ButtonBarFragment.newInstance()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_button_container, fragment)
                    .commit()
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    /**
     * Callback function called by controller.DieListFragment when appropriate, which in turns calls
     * controller.ButtonBarFragment, and asks it to remove the currently selected spinner item
     * and store it in the shared view model.
     */
    override fun onEndOfRound() {
        val fragment =
                supportFragmentManager.findFragmentById(R.id.fragment_button_container) as ButtonBarFragment
        fragment.removeSelectedSpinnerItem()
    }

    /**
     * callback function for the DieListFragment.
     */
    override fun throwButtonClicked() {
        val fragment =
                supportFragmentManager.findFragmentById(R.id.fragment_list_container) as DieListFragment
        val done = fragment.throwDice()
        if (done) {
            model.isFinished = true
            this.launchResultActivity()
        }
    }

    /**
     * launches the ResultActivity with an intent, and provides the intent with extras for the
     * model.ScoringSystems, and scores for each round of play.
     */
    private fun launchResultActivity() {
        val keys = IntArray(model.score.keys.size) { i-> model.score.keys.toList()[i].number }
        val values = IntArray(model.score.values.size) { i-> model.score.values.toList()[i] }
        val intent = ResultActivity.newIntent(this, keys, values)
        startActivity(intent)
        recreate()
    }

    /**
     * Saves the model data to the Bundle object.
     */
    private fun saveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putParcelable(MODEL, model)
    }

    /**
     * Restores the model data from the Bundle? object.
     */
    private fun restoreSavedInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null)
        {
            val savedModel = savedInstanceState.getParcelable<DieListViewModel>(MODEL)

            if (savedModel != null && !savedModel.isFinished)
            {
                model.restore(savedModel)
            }
        }
    }

}