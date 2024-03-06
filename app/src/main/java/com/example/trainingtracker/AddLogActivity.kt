package com.example.trainingtracker

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime

class AddLogActivity : AppCompatActivity(){

    private lateinit var warmUpCheckBox : CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //layout
        setContentView(R.layout.activity_add_log)

        val cardItem = intent.getSerializableExtra("EXTRA_CARD_ITEM") as ExerciseCard

        supportActionBar?.title = cardItem.name

        var setCount : Int = 0

        val kgEditText : EditText = findViewById(R.id.kgEnterText)
        val repEditText : EditText = findViewById(R.id.repEnterText)
        val warmUpCheckBox : CheckBox = findViewById(R.id.warmUpCheck)
        val logButton : Button = findViewById(R.id.logButton)

        var set: Int?

        logButton.setOnClickListener{
            val dateTime = LocalDateTime.now()
            val massString = kgEditText.text.toString()
            val mass = massString.toFloatOrNull()
            val repString = repEditText.text.toString()
            val rep = repString.toIntOrNull()
            if (warmUpCheckBox.isChecked) {
                set = null
            } else {
                setCount += 1
                set = setCount
            }
            val log = ExerciseLog(dateTime = dateTime, exerciseCard = cardItem, mass = mass, set = set, rep = rep) // Create a new card
            LogStorage.addLog(this, log)
        }
    }

}