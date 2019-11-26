package com.example.ma_lab2_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ma_lab2_android.Adapter.ListViewAdapter
import com.example.ma_lab2_android.DBHelper.DBHelper
import com.example.ma_lab2_android.Model.Meds

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.add_dialog.view.*
import android.content.DialogInterface
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MainActivity : AppCompatActivity() {
    internal lateinit var db: DBHelper
    internal var listMeds:List<Meds> = ArrayList<Meds> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DBHelper(this)

        refreshData()


        //Add button
        fab.setOnClickListener { view ->
            val addDialog  = LayoutInflater.from(this).inflate(R.layout.add_dialog, null)

            val builder = AlertDialog.Builder(this)
                .setView(addDialog)
                .setTitle("Add item")

            val alertDialog = builder.show()

            addDialog.button_add.setOnClickListener {
                alertDialog.dismiss()

                if ((addDialog.add_id.text.isEmpty() or
                    addDialog.add_name.text.isEmpty()) or
                    addDialog.add_dataExp.text.isEmpty() or
                    addDialog.add_bucati.text.isEmpty() or
                    addDialog.add_substBaza.text.isEmpty() or
                    addDialog.add_quantitySubstBaza.text.isEmpty() or
                    addDialog.add_descriere.text.isEmpty())
                {
                    AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("One of the input is empty!")
                        .setNegativeButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show()

                } else {

                    val id = addDialog.add_id.text.toString().toInt()
                    val name = addDialog.add_name.text.toString()
                    val dataExp = addDialog.add_dataExp.text.toString()
                    val bucati = addDialog.add_bucati.text.toString().toInt()
                    val substBaza = addDialog.add_substBaza.text.toString()
                    val cantSubstBaza = addDialog.add_quantitySubstBaza.text.toString()
                    val descriere = addDialog.add_descriere.text.toString()

                    val med = Meds(id, name, dataExp, bucati, substBaza, cantSubstBaza, descriere)
                    db.addMed(med)
                    refreshData()
                }
            }

            addDialog.button_cancel.setOnClickListener{
                alertDialog.dismiss()
            }
        }

        //Update button
        update_button.setOnClickListener{
            val med = Meds(
                id_edittext.text.toString().toInt(),
                name_edittext.text.toString(),
                dataExp_edittext.text.toString(),
                bucati_edittext.text.toString().toInt(),
                substBaza_edittext.text.toString(),
                cantitateSubstBaza_edittext.text.toString(),
                descriere_edittext.text.toString()
            )
            db.updateMed(med)
            refreshData()
        }

        //Delete button
        remove_button.setOnClickListener{
            val med = Meds(
                id_edittext.text.toString().toInt(),
                name_edittext.text.toString(),
                dataExp_edittext.text.toString(),
                bucati_edittext.text.toString().toInt(),
                substBaza_edittext.text.toString(),
                cantitateSubstBaza_edittext.text.toString(),
                descriere_edittext.text.toString()
            )
            db.deleteMed(med)
            refreshData()
        }

        //cancel
        cancel_button.setOnClickListener{
            refreshData()
        }


    }

    private fun refreshData() {
        listMeds = db.allMeds
        val adapter = ListViewAdapter(this@MainActivity,listMeds,id_edittext, name_edittext, dataExp_edittext, bucati_edittext, substBaza_edittext, cantitateSubstBaza_edittext, descriere_edittext)
        meds_listview.adapter = adapter
        adapter.resetLayout()
    }

    override fun onBackPressed() {
        val adapter = ListViewAdapter(this@MainActivity,listMeds,id_edittext, name_edittext, dataExp_edittext, bucati_edittext, substBaza_edittext, cantitateSubstBaza_edittext, descriere_edittext)
        if (adapter.getView() == View.VISIBLE.toString())
            adapter.resetLayout()
    }
}
