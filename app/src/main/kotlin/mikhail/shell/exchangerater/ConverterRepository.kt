package mikhail.shell.exchangerater

import com.google.gson.Gson
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ConverterRepository private constructor() {
    private val URL = "https://v6.exchangerate-api.com/v6/"
    private val KEY = "ede68b5e84a3711ed8564f88"
    private val client  = OkHttpClient()
    private val api : ConverterApi
    init{
        val factory = GsonConverterFactory.create(Gson())
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("$URL$KEY/")
            .addConverterFactory(factory)
            .build()
        api = retrofit.create(ConverterApi::class.java)
    }
    fun getCoefficient(currency: String, coefficientSubject: PublishSubject<Double>)
    {
        val request = api.getCoefficient(currency)
        request.enqueue(object : Callback<ConversionData>
        {
            override fun onResponse(call: Call<ConversionData>, response: Response<ConversionData> ) {
                val data = response.body()
                if (data != null)
                    coefficientSubject.onNext(data.conversion_rate)
            }
            override fun onFailure(call: Call<ConversionData>, t: Throwable) {
                coefficientSubject.onNext(0.0)
            }
        })
    }
    fun getCodes() : Observable<List<String>>
    {
        val codesPublisher = PublishSubject.create<List<String>>()
        val request = api.getCodes();
        request.enqueue(object : Callback<SupportedCodes>
        {
            override fun onResponse(call: Call<SupportedCodes>, response: Response<SupportedCodes>) {
                val supportedCodes =  response.body()
                var list = supportedCodes?.supported_codes?.map { subList -> subList.get(0) }
                if (list == null)
                    list = listOf()
                codesPublisher.onNext(list)
            }
            override fun onFailure(call: Call<SupportedCodes>, t: Throwable) {
                codesPublisher.onNext(listOf())
            }

        })
        return codesPublisher
    }
    companion object Factory
    {
        private var instance : ConverterRepository? = null
        fun getInstance() : ConverterRepository
        {
            if (instance == null)
                instance = ConverterRepository()
            return instance as ConverterRepository
        }
    }
}