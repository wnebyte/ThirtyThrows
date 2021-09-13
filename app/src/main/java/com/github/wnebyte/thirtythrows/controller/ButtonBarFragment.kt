package com.github.wnebyte.thirtythrows.controller

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.github.wnebyte.thirtythrows.R
import com.github.wnebyte.thirtythrows.logic.GameLogic
import com.github.wnebyte.thirtythrows.model.DieListViewModel
import com.github.wnebyte.thirtythrows.model.ScoringSystem
import java.lang.Exception
import java.lang.IllegalStateException

/** variables for using LogCat */
private const val TAG = "ButtonBarFragment"

private const val SPINNER_TAG = "Spinner"

class ButtonBarFragment : Fragment() {

    private lateinit var throwButton: Button

    private lateinit var spinner: Spinner

    private lateinit var arrayAdapter: ArrayAdapter<ScoringSystem>

    private lateinit var game: GameLogic

    /** Fragment's and MainActivity's shared view model. */
    private val model: DieListViewModel by lazy {
        ViewModelProviders.of(this.requireActivity()).get(DieListViewModel::class.java)
    }

    /** Callback interface */
    interface ThrowButtonCallback {
        fun throwButtonClicked()
    }

    // Todo: how to release non-nullable resources?
    private var callback: ThrowButtonCallback? = null

    /**
     * OnAttach checks that the hosting activity does indeed implement the callback interface,
     * otherwise throws an IllegalStateException.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as ThrowButtonCallback
        }
        catch (e: Exception) {
            throw IllegalStateException(
                "activity needs to implement the ButtonBarFragment.ThrowButtonCallback interface"
            )
        }
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }

    /**
     * OnCreate instantiates the GameLogic field.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate() called")
        game = GameLogic(model)
    }

    /**
     * OnCreateView inflates the view, initiates the class's lateinit variables, the
     * spinner's adapter with data from the shared view model, the spinners onItemSelectedListener,
     * and the throwButtons onClickListener.
     */
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_button_bar, container, false)
        throwButton = view.findViewById(R.id.throw_button) as Button
        spinner = view.findViewById(R.id.score_system_spinner)
        arrayAdapter =
                ArrayAdapter(view.context, android.R.layout.simple_spinner_item, model.scoringSystems)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = this.onSpinnerItemSelected()
        throwButton.setOnClickListener{callback?.throwButtonClicked()}
        return view
    }

    /**
     * OnItemSelectedListener for the spinner, which when called stores the selected spinner item
     * in the shared view model.
     */
    private fun onSpinnerItemSelected() : AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position) as ScoringSystem
                model.currentScoringSystem = selectedItem
                Log.d(SPINNER_TAG, "onSpinnerItemSelected() -> +$selectedItem")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    companion object {
        /**
         * Constructs and returns a new instance of this class.
         */
        fun newInstance(): ButtonBarFragment {
            return ButtonBarFragment()
        }
    }

    /**
     * Removes the currently selected spinner item from the spinner adapter, and from the shared
     * view model's list of scoring systems, and stores the removed item in the view model.
     */
    fun removeSelectedSpinnerItem() {
        val selectedItem = spinner.selectedItem as ScoringSystem
        model.currentScoringSystem = selectedItem
        model.scoringSystems.remove(selectedItem)
        arrayAdapter.remove(selectedItem)
        arrayAdapter.notifyDataSetChanged()
        Log.d(SPINNER_TAG, "spinnerRemoveSelectedItem() -> -$selectedItem")
    }
}

