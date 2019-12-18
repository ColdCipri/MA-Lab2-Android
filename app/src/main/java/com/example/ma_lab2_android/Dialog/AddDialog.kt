package com.example.ma_lab2_android.Dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import com.example.ma_lab2_android.Model.Meds
import com.example.ma_lab2_android.R
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.add_dialog.*

class AddDialog(private val activity: Activity) : Dialog(activity), View.OnClickListener{

    private lateinit var realm: Realm

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.button_add -> {
                if (add_name.text.isEmpty() or
                    add_dataExp.text.isEmpty() or
                    add_bucati.text.isEmpty() or
                    add_substBaza.text.isEmpty() or
                    add_quantitySubstBaza.text.isEmpty() or
                    add_descriere.text.isEmpty())
                {
                    AlertDialog.Builder(v.context)
                        .setTitle("Warning")
                        .setMessage("One of the input is empty!")
                        .setNegativeButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show()

                } else {

                    realm.executeTransaction { realm ->
                        val med = realm.createObject<Meds>(getNewId())
                        med.name = add_name.text.toString()
                        med.dataExp = add_dataExp.text.toString()
                        med.pieces = add_bucati.text.toString().toInt()
                        med.baseSubst = add_substBaza.text.toString()
                        med.quantityBaseSubst = add_quantitySubstBaza.text.toString()
                        med.description = add_descriere.text.toString()
                    }
                    Snackbar.make(v, "Added", Snackbar.LENGTH_LONG)
                    dismiss()
                }
            }
            R.id.button_cancel -> {
                Snackbar.make(v,"Cancel",Snackbar.LENGTH_LONG)
                dismiss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(R.layout.add_dialog)

        button_add.setOnClickListener(this)
        button_cancel.setOnClickListener(this)

        realm = Realm.getDefaultInstance()
    }

    private fun getNewId(): Int {
        val meds = realm.where<Meds>().findAll()

        var id = 0

        for (med in meds) {
            try {
                var k = med.id?.toInt()
                if (k != null && k > id) {
                    id = k
                }
            } catch (e: Exception) {

            }
        }

        return (id + 1)
    }
}