package com.github.wnebyte.thirtythrows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import com.github.wnebyte.thirtythrows.model.Round
import com.github.wnebyte.thirtythrows.ui.game.GameFragment
import com.github.wnebyte.thirtythrows.ui.game.GameFragmentDirections
import com.github.wnebyte.thirtythrows.ui.result.ResultFragment
import com.github.wnebyte.thirtythrows.ui.result.ResultFragmentDirections

class MainActivity : AppCompatActivity(), GameFragment.Callbacks, ResultFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onLastThrow(rounds: Array<Round>) {
        val navController = findNavController(R.id.nav_host_fragment)
        val action = GameFragmentDirections.actionNavGameToNavResult(rounds)
        navController.navigate(action)
    }

    override fun onNewGame() {
        val navController = findNavController(R.id.nav_host_fragment)
        val action = ResultFragmentDirections.actionNavResultToNavGame()
        navController.navigate(action)
    }
}