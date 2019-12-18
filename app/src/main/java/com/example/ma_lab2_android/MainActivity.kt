package com.example.ma_lab2_android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ma_lab2_android.Model.Meds

import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ma_lab2_android.Adapter.MedsAdapter
import com.example.ma_lab2_android.Dialog.AddDialog
import com.example.ma_lab2_android.Network.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import org.jetbrains.anko.doAsync


class MainActivity : AppCompatActivity() {


    private lateinit var realm: Realm
    private lateinit var adapter: MedsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realm = Realm.getDefaultInstance()


        //Add button
        fab.setOnClickListener {
            val dialog = AddDialog(this)
            dialog.show()
        }


        adapter = MedsAdapter(realm, baseContext)
        meds_recyclerView.layoutManager = LinearLayoutManager(this)
        meds_recyclerView.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.buttons, menu)
        return true
    }

    @SuppressLint("CheckResult")
    fun synchronize() {
        var realm = Realm.getDefaultInstance()
        val networkApiAdapter = NetworkAPIAdapter.instance

        val serverMeds = networkApiAdapter.fetchAll()
        val localMeds = realm.where<Meds>().findAll()


        Log.d("dksandnklads", serverMeds.toString())
        Log.d("----------------------", localMeds.toString())

        for (localMed in localMeds) {
            val id = localMed.id

            Log.d("Cwori", localMed.toString())
            if (id != null) {
                if (id == 0){
                    Log.d("Id is 0", id.toString())
                    networkApiAdapter.insert(localMed)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe( {}, {}, {
                            Log.d("INSERT FINISHED", "insert succesfull")
                        })
                }else {
                    networkApiAdapter.update(id, localMed)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe( {}, {}, {
                            Log.d("UPDATE FINISHED", "update succesfull")
                        })
                }
            }
        }

        realm.executeTransaction { realm ->
            realm.deleteAll()
        }

        for (serverMed in serverMeds) {
            realm.executeTransaction { realm ->
                val med = realm.createObject<Meds>(serverMed.id)
                med.name = serverMed.name
                med.dataExp = serverMed.dataExp
                med.pieces = serverMed.pieces
                med.baseSubst = serverMed.baseSubst
                med.quantityBaseSubst = serverMed.quantityBaseSubst
                med.description = serverMed.description
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.btn_refresh -> {
            doAsync {
                synchronize()
                runOnUiThread {
                    adapter.notifyDataSetChanged()
                }

            }
            Toast.makeText(this.baseContext, "Synchronized", Toast.LENGTH_LONG).show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
