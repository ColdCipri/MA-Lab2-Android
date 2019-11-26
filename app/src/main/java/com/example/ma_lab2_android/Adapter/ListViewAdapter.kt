package com.example.ma_lab2_android.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import com.example.ma_lab2_android.Model.Meds
import com.example.ma_lab2_android.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_layout.view.*


class ListViewAdapter(internal var activity: Activity,
                      internal var listMeds:List<Meds>,
                      internal var edit_id: EditText,
                      internal var edit_name: EditText,
                      internal var edit_dataExp: EditText,
                      internal var edit_bucati: EditText,
                      internal var edit_baseSubst: EditText,
                      internal var edit_quantity: EditText,
                      internal var edit_description: EditText):BaseAdapter() {
    internal var inflater: LayoutInflater
    init {
        inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val listLayout: View = inflater.inflate(R.layout.list_layout, null)

        listLayout.id_textview.text = listMeds[position].id.toString()
        listLayout.name_textview.text = listMeds[position].name.toString()
        listLayout.dataexp_textview.text = listMeds[position].dataExp.toString()
        listLayout.pieces_textview.text = listMeds[position].pieces.toString()
        listLayout.substBaza_textview.text = listMeds[position].baseSubst.toString()
        listLayout.quantity_textview.text = listMeds[position].quantityBaseSubst.toString()
        listLayout.description_textview.text = listMeds[position].description.toString()

        //Event
        listLayout.setOnClickListener{
            this.activity.updateTool_layout.visibility = View.VISIBLE
            edit_id.setText(listLayout.id_textview.text.toString())
            edit_name.setText(listLayout.name_textview.text.toString())
            edit_dataExp.setText(listLayout.dataexp_textview.text.toString())
            edit_bucati.setText(listLayout.pieces_textview.text.toString())
            edit_baseSubst.setText(listLayout.substBaza_textview.text.toString())
            edit_quantity.setText(listLayout.quantity_textview.text.toString())
            edit_description.setText(listLayout.description_textview.text.toString())
        }

        return listLayout
    }

    override fun getItem(position: Int): Any {
        return listMeds[position]
    }

    override fun getItemId(position: Int): Long {
        return listMeds[position].id.toLong()
    }

    override fun getCount(): Int {
        return listMeds.size
    }

    fun resetLayout(){
        this.activity.updateTool_layout.visibility = View.GONE
    }

    fun getView():String{
        return this.activity.updateTool_layout.visibility.toString()
    }
}