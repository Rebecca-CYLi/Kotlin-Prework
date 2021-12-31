package com.example.simpletodo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat.getSystemService

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

//    val key_item_text = "item_text"
//    val key_item_position = "item_position"
    val edit_text_code = 20

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

        var onClickListener = object: TaskItemAdapter.OnClickListener{
            override fun onItemClicked(position: Int){
                Log.d("MainActivity", "Single click at position: " + position + "  = " + listOfTasks.get(position))

                // create the new activity
                val intent = Intent(this@MainActivity, EditActivity::class.java)
                // pass the data being edited
                intent.putExtra("item_text", listOfTasks.get(position))
                intent.putExtra("item_position", position)
                // display the activity
                startActivityForResult(intent, edit_text_code)
            }
        }

        // populate list items
        loadItems()

        // Lookup the recyclerview in activity layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener, onClickListener)

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
        }

    }//end of onCreate

    private fun hideSoftKeyboard(view: View) {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    // handle the result of the edit activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK && requestCode == edit_text_code){
            // retrieve the updated text value
            var itemText = data?.getStringExtra("item_text")

            // extract the original position of the edited item from the position key
            var position = data?.getExtras()?.getInt("item_position")

            val emptyStr = itemText.isNullOrEmpty()
            if(!emptyStr){
                // if the string is NOT empty, update the model at the right position with new item text
                listOfTasks.set(position!!, itemText!!)
                // notify the adapter
                adapter.notifyItemChanged(position)
                // persist changes
                saveItems()
                // Toast.makeText(applicationContext, "Item updated Successfully", Toast.LENGTH_SHORT).show()

                val toast = Toast.makeText(
                    applicationContext,
                    "Item updated Successfully",
                    Toast.LENGTH_SHORT
                )
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
            else{ // if string IS EMPTY, delete it

                // update the model at the right position with new item text
                if (position != null) listOfTasks.removeAt(position)
                adapter.notifyDataSetChanged()
                saveItems()
                // Toast.makeText(applicationContext, "Item returned was empty! It has been deleted from list!", Toast.LENGTH_SHORT).show()

                val toast = Toast.makeText(
                    applicationContext,
                    "Item returned was empty! \n\n" +
                            "It has been removed from list!",
                    Toast.LENGTH_LONG
                )
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }

//            // extract the original position of the edited item from the position key
//            var position = data?.getExtras()?.getInt("item_position")
//
//            // update the model at the right position with new item text
//            listOfTasks.set(position!!, itemText!!)
//            // notify the adapter
//            adapter.notifyItemChanged(position)
//            // persist changes
//            saveItems()
//            Toast.makeText(applicationContext, "Item updated Successfully", Toast.LENGTH_SHORT).show()
        }else{
            Log.w("MainActivity", "Unknown call to onActivityResult")
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