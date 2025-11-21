package np.com.bimalkafle.calculator

import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class CalculatorViewModel : ViewModel() {

    private val _equationText = MutableLiveData("")
    val equationText: LiveData<String> = _equationText

    private val _resultText = MutableLiveData("0")
    val resultText: LiveData<String> = _resultText

    fun onButtonClick(btn: String) {

        val current = _equationText.value ?: ""

        if (btn == "AC") {
            _equationText.value = ""
            _resultText.value = "0"
            return
        }

        if (btn == "C") {
            if (current.isNotEmpty()) {
                _equationText.value = current.dropLast(1)
            }
            return
        }

        if (btn == "=") {
            _equationText.value = _resultText.value
            return
        }

        _equationText.value = current + btn

        // Agora sim calcula
        _resultText.value = calculateResult(_equationText.value!!)
    }

    private fun calculateResult(equation: String): String {

        val rhino = Context.enter()
        rhino.optimizationLevel = -1

        return try {
            val scope: Scriptable = rhino.initStandardObjects()
            var result = rhino.evaluateString(scope, equation, "Javascript", 1, null).toString()

            if (result.endsWith(".0")) result = result.replace(".0", "")
            result
        } catch (e: Exception) {
            "0"
        } finally {
            Context.exit()
        }
    }
}