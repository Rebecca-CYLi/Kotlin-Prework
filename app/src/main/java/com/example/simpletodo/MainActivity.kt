package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.text.FieldPosition

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int){
                listOfTasks.removeAt(position)
                adapter.notifyDataSetChanged()
                saveItems()
            }
        }

        // populate list items
        loadItems()

        // Lookup the recyclerview in activity layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)

        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter

        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the button and input field, so that the user can enter a task and add it to the list
        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // Get a reference to the button
        findViewById<Button>(R.id.button).setOnClickListener{
            val userInputtedTask = inputTextField.text.toString()
            val nullstr = userInputtedTask.isNullOrEmpty()

            // if user inputted text
            if (!nullstr){
                listOfTasks.add(userInputtedTask)
                adapter.notifyItemInserted(listOfTasks.size - 1)
                inputTextField.setText("")
                saveItems()
            }
            // if user did not input text, just do nothing
//            listOfTasks.add(userInputtedTask)
//            adapter.notifyItemInserted(listOfTasks.size - 1)
//            inputTextField.setText("")
//            saveItems()
        }

    }
    // save user data inputted by writing and reading from file

    // get the file we need
    fun getDatafile(): File{
        return File(filesDir, "data.txt")
    }

    // load items
    fun loadItems(){
        try{
            listOfTasks = FileUtils.readLines(getDatafile(), Charset.defaultCharset())
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

    // save items by writing them into our data file
    fun saveItems(){
        try{
            FileUtils.writeLines(getDatafile(), listOfTasks)
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

}