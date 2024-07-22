package mikhail.shell.exchangerater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import mikhail.shell.exchangerater.databinding.ConverterActivityBinding
import kotlin.time.Duration

class ConverterActivity : AppCompatActivity() {
    private val activity = this
    private var B : ConverterActivityBinding? = null
    private var viewModel : ConverterViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        B = DataBindingUtil.setContentView(this, R.layout.converter_activity)
        viewModel = ViewModelProvider(this, ConverterViewModel.Factory(getCoefficientConsumer())).get(ConverterViewModel::class.java)
    }
    override fun onStart()
    {
        super.onStart()
        getCodes()
    }
    private fun convert()
    {
        val currency = B?.currency?.selectedItem.toString()
        viewModel?.getCoefficient(currency)
    }
    private fun getCoefficientConsumer() =
        Consumer<Double> { coef ->
            if (coef != 0.0) {
                val amount = B?.Amount?.text.toString().toDouble()
                val result = evaluate(amount, coef)
                B?.setResult(result)
            } else
                Toast.makeText(activity, "Ошибка при конвертации", Toast.LENGTH_SHORT).show()
        }
    private fun getCodes()
    {
        var disposable : Disposable?  = null
        disposable = viewModel?.getCodes()?.subscribe { list ->
            if (list.size == 0)
                Toast.makeText(activity, "Ошибка при получении всех валют", Toast.LENGTH_SHORT).show()
            else {
                val adapter = ArrayAdapter(activity, R.layout.spinner_item, list)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                B?.currency?.adapter = adapter
                B?.ConvertBtn?.setOnClickListener { btn -> convert() }
            }
            disposable?.dispose()
        }
    }
    private fun evaluate(amount : Double, coefficient : Double) = round(amount * coefficient)
    private fun round(x: Double) = Math.round(x * 100).toDouble() / 100
    override fun onDestroy()
    {
        super.onDestroy()
        viewModel?.unsubsribe()
    }
}