package com.example.ma_lab2_android.Dialog

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ma_lab2_android.Model.Meds
import com.example.ma_lab2_android.R
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.edit_dialog.*

class EditDialog : AppCompatActivity(){

    private lateinit var realm: Realm


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_dialog)

        val id = intent.getStringExtra("id").toString().toInt()
        realm = Realm.getDefaultInstance()

        init(id)

        button_edit.setOnClickListener{
            this.saveMed(id)
        }

        button_cancel.setOnClickListener{
            super.onBackPressed()
        }
    }

    private fun saveMed(id: Int) {
        val med = realm.where<Meds>().equalTo("id", id).findFirst()
        if(med != null){
            if (edit_name.text.isEmpty() or
                edit_dataExp.text.isEmpty() or
                edit_bucati.text.isEmpty() or
                edit_substBaza.text.isEmpty() or
                edit_quantitySubstBaza.text.isEmpty() or
                edit_descriere.text.isEmpty())
            {
                AlertDialog.Builder(this)
                    .setTitle("Warning")
                    .setMessage("One of the input is empty!")
                    .setNegativeButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()

            } else {
                realm.executeTransaction {
                    med.name = edit_name.text.toString()
                    med.dataExp = edit_dataExp.text.toString()
                    med.pieces = edit_bucati.text.toString().toInt()
                    med.baseSubst = edit_substBaza.text.toString()
                    med.quantityBaseSubst = edit_quantitySubstBaza.text.toString()
                    med.description = edit_descriere.text.toString()
                }
            }
        }
    }


    fun init(id: Int) {
        title = id.toString()

        val med = realm.where<Meds>().equalTo("id", id).findFirst()

        Log.d("EDIT PAGE", med.toString())

        if (med != null) {
            edit_name.text = Editable.Factory.getInstance().newEditable(med.name)
            edit_dataExp.text = Editable.Factory.getInstance().newEditable(med.dataExp)
            edit_bucati.text = Editable.Factory.getInstance().newEditable(med.pieces.toString())
            edit_substBaza.text = Editable.Factory.getInstance().newEditable(med.baseSubst)
            edit_quantitySubstBaza.text = Editable.Factory.getInstance().newEditable(med.quantityBaseSubst)
            edit_descriere.text = Editable.Factory.getInstance().newEditable(med.description)
        }
    }
}