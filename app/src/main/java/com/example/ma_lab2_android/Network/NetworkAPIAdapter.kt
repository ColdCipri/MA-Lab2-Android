package com.example.ma_lab2_android.Network


import android.util.Log
import com.example.ma_lab2_android.Model.Meds
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


class NetworkAPIAdapter private constructor() {

    private object Holder {
        val INSTANCE = NetworkAPIAdapter()
    }

    companion object {
        val instance: NetworkAPIAdapter by lazy {Holder.INSTANCE}
        const val BASE_URL: String = "http://localhost:50602/"
        private const val URL_ORDERS_ALL: String = "/meds"
        private const val URL_ORDER_INDIVIDUAL: String = "/meds/{id}"
    }

    private val medService: MedsService

    init {
        val gson: Gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(NetworkAPIAdapter.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()

        medService = retrofit.create(MedsService::class.java)
    }

    fun fetchAll(): List<Meds> {
        return medService.fetchAll().execute().body()!!
    }

    fun insert(dto: Meds): Observable<Meds> {
        Log.d("INSERT", dto.toString())
        return medService.insert(dto.name!!, dto.dataExp!!, dto.pieces!!, dto.baseSubst!!, dto.quantityBaseSubst!!, dto.description!!)
    }

    fun update(id: Int, dto: Meds): Observable<ResponseBody> {
        return medService.update(id, dto.name!!, dto.dataExp!!, dto.pieces!!, dto.baseSubst!!, dto.quantityBaseSubst!!, dto.description!!)
    }

    fun delete(id: Int): Observable<ResponseBody> {
        return medService.delete(id)
    }

    interface MedsService {
        @GET(URL_ORDERS_ALL)
        fun fetchAll(): Call<List<Meds>>

        @POST(URL_ORDERS_ALL)
        @FormUrlEncoded
        fun insert(@Field("name") name: String,
                   @Field("@exp_date") dataExp: String,
                   @Field("@pieces") pieces: Int,
                   @Field("@base_subst") baseSubst: String,
                   @Field("@quantity") quantityBaseSubst: String,
                   @Field("@description") description: String
                   ): Observable<Meds>

        @PUT(URL_ORDER_INDIVIDUAL)
        @FormUrlEncoded
        fun update(@Path("id") id: Int,
                   @Field("name") name: String,
                   @Field("@exp_date") dataExp: String,
                   @Field("@pieces") pieces: Int,
                   @Field("@base_subst") baseSubst: String,
                   @Field("@quantity") quantityBaseSubst: String,
                   @Field("@description") description: String
                   ): Observable<ResponseBody>

        @DELETE(URL_ORDER_INDIVIDUAL)
        fun delete(@Path("id") id: Int): Observable<ResponseBody>
    }
}