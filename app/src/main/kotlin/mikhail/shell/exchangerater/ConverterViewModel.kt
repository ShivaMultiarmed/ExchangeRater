package mikhail.shell.exchangerater

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.subjects.PublishSubject

class ConverterViewModel(consumer : Consumer<Double>) : ViewModel(){
    private val repository = ConverterRepository.Factory.getInstance()
    private val coefficientSubject : PublishSubject<Double> = PublishSubject.create()
    private val subsription : Disposable?
    init {
        subsription = coefficientSubject.subscribe(consumer)
    }
    fun getCoefficient(currency: String) = repository.getCoefficient(currency, coefficientSubject)
    fun getCodes() = repository.getCodes()
    class Factory(val consumer: Consumer<Double>) : ViewModelProvider.Factory
    {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ConverterViewModel(consumer) as T
        }
    }
    fun unsubsribe() = subsription?.dispose()
}