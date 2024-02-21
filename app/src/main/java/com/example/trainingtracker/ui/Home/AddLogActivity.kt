package com.example.trainingtracker.ui.Home

import android.health.connect.datatypes.units.Mass
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.trainingtracker.CardStorage
import com.example.trainingtracker.ExerciseCard
import com.example.trainingtracker.ExerciseLog
import com.example.trainingtracker.LogStorage
import com.example.trainingtracker.R
import java.time.LocalDateTime

class AddLogActivity : AppCompatActivity(){
    val cardItem = intent.getSerializableExtra("EXTRA_CARD_ITEM") as ExerciseCard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //layout
        setContentView(R.layout.activity_add_log)

        val kgEditText : EditText = findViewById(R.id.kgEnterText)
        val repEditText : EditText = findViewById(R.id.repEnterText)
        val logButton : Button = findViewById(R.id.logButton)

        val massString = kgEditText.text.toString()
        val mass = massString.toFloatOrNull()

        val repString = repEditText.text.toString()
        val rep = repString.toIntOrNull()

        logButton.setOnClickListener{
            val dateTime = LocalDateTime.now()
            val set = null
            val log = ExerciseLog(dateTime = dateTime, exerciseCard = cardItem, mass = mass, set = set, rep = rep) // Create a new card
            LogStorage.addLog(this, log)
            finish()
        }
    }

}