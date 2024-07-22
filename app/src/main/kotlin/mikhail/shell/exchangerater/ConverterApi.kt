package mikhail.shell.exchangerater

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ConverterApi {
    @GET("pair/RUB/{currency}")
    fun getCoefficient(@Path("currency") currency : String) : Call<ConversionData>
    @GET("codes/")
    fun getCodes() : Call<SupportedCodes>
}