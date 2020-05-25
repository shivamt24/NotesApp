package com.example.noteapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*
import java.lang.reflect.Constructor

class MainActivity : AppCompatActivity() {

    var listNotes = ArrayList<Notes>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //listNotes.add(Notes(1,"meet prof","meet the professor to discuss the future prospectus"))
        //listNotes.add(Notes(2,"meet prof","meet the professor to discuss the future prospectus"))
        //listNotes.add(Notes(3,"meet prof","meet the professor to discuss the future prospectus"))

        loadQuery("%")


    }

    override fun onResume() {
        super.onResume()
        loadQuery("%")
    }

    fun loadQuery(title:String){
        var dbManager = DbManager(this)
        val projections = arrayOf("ID","Title","Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.query(projections,"Title Like ?", selectionArgs,"Title")
        listNotes.clear()
        if(cursor.moveToFirst()){
            do{
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Desc = cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(Notes(ID,Title,Desc))
            }while (cursor.moveToNext())
        }
        var myNotesAdapter = MyNotesAdapter(this,listNotes)
        lvNotes.adapter = myNotesAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu,menu)
        val sv = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext,query,Toast.LENGTH_LONG).show()
                if(query == "*")
                    loadQuery("%")
                else
                    loadQuery("%"+query+"%")
                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item != null) {
            when (item.itemId) {
                R.id.addNote -> {
                    //add addNote page
                    var intent = Intent(this,AddNotes::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class MyNotesAdapter:BaseAdapter{
        var listNotes = ArrayList<Notes>()
        var context:Context?=null
        constructor(context: Context,listNotes:ArrayList<Notes>):super(){
            this.listNotes = listNotes
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var myView = layoutInflater.inflate(R.layout.ticket,null)
            var myNote = listNotes[position]
            myView.tvTitle.text = myNote.NodeName
            myView.tvDes.text = myNote.NodeDesc
            myView.ivDelete.setOnClickListener(View.OnClickListener {
                var db = DbManager(this.context!!)
                val selectionArgs = arrayOf(myNote.NodeId.toString())
                db.delete("ID=?",selectionArgs)
                loadQuery("%")
            })
            myView.ivEdit.setOnClickListener({
                goToUpdate(myNote)
            })
            return myView
        }

        override fun getItem(position: Int): Any {
            return listNotes[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listNotes.size
        }
    }

    fun goToUpdate(note:Notes){
        var intent = Intent(this,AddNotes::class.java)
        intent.putExtra("ID",note.NodeId)
        intent.putExtra("Name",note.NodeName)
        intent.putExtra("Desc",note.NodeDesc)
        startActivity(intent)

    }
}
