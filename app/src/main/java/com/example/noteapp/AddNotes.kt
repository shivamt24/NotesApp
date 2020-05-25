package com.example.noteapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.noteapp.R
import kotlinx.android.synthetic.main.activity_add_notes.*
import kotlin.Exception

class AddNotes : AppCompatActivity() {
    var Id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)


        try {
            var bundle:Bundle = intent.extras
            Id = bundle.getInt("ID",0)
            if(Id != 0){
                etTitle.setText(bundle.getString("Name").toString())
                etDesc.setText(bundle.getString("Desc").toString())
            }
        }catch (ex:Exception){}


    }

    fun buFinish(view: View){
        var dbManager = DbManager(this)
        var values = ContentValues()
        values.put("Title",etTitle.text.toString())
        values.put("Description",etDesc.text.toString())

        if(Id==0){
            val ID = dbManager.insertVal(values)
            if(ID>0){
                Toast.makeText(this,"Note is added",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"Cannot add note",Toast.LENGTH_LONG).show()
            }
        }else{
            var selectionArg = arrayOf(Id.toString())
            val ID = dbManager.update(values,"ID=?",selectionArg)
            if(ID>0){
                Toast.makeText(this,"Note is updated",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"Cannot add note",Toast.LENGTH_LONG).show()
            }
        }

        finish()
    }
}
