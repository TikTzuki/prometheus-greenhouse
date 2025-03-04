package tik.prometheus.mobile

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

abstract class ZFragment : Fragment() {
    protected fun showToast(value: CharSequence) {
        Toast.makeText(context, value, Toast.LENGTH_LONG).show()
    }

    lateinit var zContainer: ZContainer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        zContainer = if (activity != null) (requireActivity().application as ZApplication).container else ZContainer()
    }

    fun editText(defaultValue: String?, title: String, inputType: Int = InputType.TYPE_CLASS_TEXT,onOk: (dialog: DialogInterface, input: EditText) -> Unit) {
        val input = EditText(requireContext())
        input.inputType = inputType
        input.setText(defaultValue)
        val alert = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(input)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                onOk.invoke(dialog, input)
            })
            .create()
        alert.show()
    }
}