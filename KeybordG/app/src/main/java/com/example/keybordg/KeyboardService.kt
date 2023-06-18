import android.content.ClipboardManager
import android.content.Context
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.compose.ui.input.key.KeyEvent
import com.example.keybordg.R

class KeyboardService : InputMethodService() {

    private lateinit var clipboardManager: ClipboardManager

    override fun onCreate() {
        super.onCreate()
        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    // ...

    private fun handleClipboardAction() {
        val clipData = clipboardManager.primaryClip
        if (clipData != null && clipData.itemCount > 0) {
            val text = clipData.getItemAt(0).coerceToText(this)
            // Обработка текста из буфера обмена
        }
    }

    // ...

    override fun onCreateInputView(): View {
        val inputView = layoutInflater.inflate(R.layout.keyboard_layout, null)

        val editText = inputView.findViewById(R.id.editText) as EditText
        val inputConnection = editText.onCreateInputConnection(EditorInfo())
        // Регистрация InputConnection
        editText.setRawInputType(EditorInfo.TYPE_NULL)
        editText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                // Обработка введенного текста
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        // Регистрация ClipboardManager
        editText.setOnLongClickListener {
            handleClipboardAction()
            true
        }

        return inputView
    }
}
