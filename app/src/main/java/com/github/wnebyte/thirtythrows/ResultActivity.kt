package com.github.wnebyte.thirtythrows

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

private const val TAG = "ResultActivity"

private const val EXTRA_KEYS =
    "com.github.wnebyte.thirtythrows.keys"

private const val EXTRA_VALUES =
    "com.github.wnebyte.thirtythrows.values"

class ResultActivity : AppCompatActivity() {

    private val colTwo by lazy {
        listOf<TextView>(
            findViewById(R.id.table_row_1_col_2),
            findViewById(R.id.table_row_2_col_2),
            findViewById(R.id.table_row_3_col_2),
            findViewById(R.id.table_row_4_col_2),
            findViewById(R.id.table_row_5_col_2),
            findViewById(R.id.table_row_6_col_2),
            findViewById(R.id.table_row_7_col_2),
            findViewById(R.id.table_row_8_col_2),
            findViewById(R.id.table_row_9_col_2),
            findViewById(R.id.table_row_10_col_2)
        )
    }

    private val colThree by lazy {
        listOf<TextView>(
            findViewById(R.id.table_row_1_col_3),
            findViewById(R.id.table_row_2_col_3),
            findViewById(R.id.table_row_3_col_3),
            findViewById(R.id.table_row_4_col_3),
            findViewById(R.id.table_row_5_col_3),
            findViewById(R.id.table_row_6_col_3),
            findViewById(R.id.table_row_7_col_3),
            findViewById(R.id.table_row_8_col_3),
            findViewById(R.id.table_row_9_col_3),
            findViewById(R.id.table_row_10_col_3)
        )
    }

    /**
     * OnCreate sets the content view and retrieves the extras from the intent, and initializes
     * the view.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val keys = intent.getIntArrayExtra(EXTRA_KEYS)
        val values = intent.getIntArrayExtra(EXTRA_VALUES)

        if (keys != null && values != null)
        {
            this.initView(keys, values)
        }
    }

    private fun initView(keys: IntArray, values: IntArray) {
        for (i in keys.indices) {
            colTwo[i].text = keys[i].toString()
        }

        for (i in values.indices) {
            colThree[i].text = values[i].toString()
        }

        findViewById<TextView>(R.id.table_row_11_col_3).text = values.sum().toString()
    }

    companion object {
        /**
         * Static function for creating a new Intent for this class.
         */
        fun newIntent(packageContext: Context, keys: IntArray, values: IntArray): Intent {
            return Intent(packageContext, ResultActivity::class.java).apply {
                putExtra(EXTRA_KEYS, keys)
                putExtra(EXTRA_VALUES, values)
            }
        }
    }
}