package com.example.ma_lab2_android.Adapter

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ma_lab2_android.Dialog.EditDialog
import com.example.ma_lab2_android.Model.Meds
import com.example.ma_lab2_android.Network.NetworkAPIAdapter
import com.example.ma_lab2_android.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.list_layout.view.*

class MedsAdapter(var realm: Realm, var context: Context): RecyclerView.Adapter<MedsAdapter.MedsViewHolder>(){
    class MedsViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedsAdapter.MedsViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)

        return MedsViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedsViewHolder, position: Int){
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        val meds = realm.where<Meds>().findAll()

        holder.view.id_textview.text = meds[position]?.id.toString()
        holder.view.name_textview.text = meds[position]?.name
        holder.view.dataexp_textview.text = meds[position]?.dataExp
        holder.view.pieces_textview.text = meds[position]?.pieces.toString()
        holder.view.substBaza_textview.text = meds[position]?.baseSubst
        holder.view.quantity_textview.text = meds[position]?.quantityBaseSubst
        holder.view.description_textview.text = meds[position]?.description

        holder.view.update_button.setOnClickListener {
            val id = meds[position]?.id
            val intent = Intent(it.context, EditDialog::class.java).apply{
                putExtra("id", id)
            }

            it.context.startActivity(intent)
        }

        holder.view.remove_button.setOnClickListener { it ->
            val id = meds[position]?.id

            if (networkInfo != null && networkInfo.isConnected) {
                realm.executeTransaction { realm ->
                    val med = realm.where<Meds>().equalTo("id", id).findFirst()!!
                    med.deleteFromRealm()
                }

                val networkApiAdapter = NetworkAPIAdapter.instance
                if (id != null) {
                    networkApiAdapter.delete(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe( {}, {}, {
                            Log.d("DELETE FINISHED", "item deleted")
                        })
                }
            } else
                Toast.makeText(context, "No internet connection",Toast.LENGTH_SHORT).show()

        }
    }
    override fun getItemCount() = realm.where<Meds>().findAll().size
}