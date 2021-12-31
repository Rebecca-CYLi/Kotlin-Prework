package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class EditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        var editItem = findViewById<EditText>(R.id.editItem)
        var saveBtn = findViewById<Button>(R.id.saveBtn)

        // set the top title
        supportActionBar?.title = "Edit Item"

        editItem.setText(intent.getStringExtra("item_text"))
        saveBtn.setOnClickListener {

            // create intent with modified results
            val returnIntent = Intent()

            val reEditText = editItem.getText().toString()
            val nullstr = reEditText.isEmpty()

            if (!nullstr){
                // if there is input, pass the data
                returnIntent.putExtra("item_text", reEditText)
                returnIntent.putExtra("item_position", intent.extras?.getInt("item_position"))

                // set the result of the intent
                setResult(RESULT_OK, returnIntent)
                // finish activity, close screen and go back
                finish()
            }else{
                // if the string is empty
                // Toast.makeText(applicationContext, "Did you wanted to delete this task?", Toast.LENGTH_LONG).show()
                val toast = Toast.makeText(applicationContext, "Did you wanted to delete this task?", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()

                returnIntent.putExtra("item_text", reEditText)
                returnIntent.putExtra("item_position", intent.extras?.getInt("item_position"))

                // set the result of the intent
                setResult(RESULT_OK, returnIntent)
                // finish activity, close screen and go back
                finish()
            }


        }


    }
}