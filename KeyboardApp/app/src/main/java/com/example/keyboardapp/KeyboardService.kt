package com.example.keyboardapp

//noinspection SuspiciousImport
import android.R
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener
import android.media.AudioManager
import android.view.KeyEvent
import android.view.View


class KeyboardService : InputMethodService(), OnKeyboardActionListener {
    private var keyboardView: KeyboardService? = null
    private var keyboard: KeyboardService? = null
    private var isCap = false
    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout. ,null) as KeyboardService?
        keyboard = Keyboard(this, R.xml.qwerty)
        keyboardView!!.keyboard = keyboard
        keyboardView!!.setOnKeyboardActionListener(this)
        return keyboardView!!
    }

    override fun onPress(i: Int) {}
    override fun onRelease(i: Int) {}
    override fun onKey(i: Int, ints: IntArray) {
        val inputConnection = currentInputConnection
        playClick(i)
        when (i) {
            Keyboard.KEYCODE_DELETE -> inputConnection.deleteSurroundingText(1, 0)
            Keyboard.KEYCODE_SHIFT -> {
                isCap = !isCap
                keyboard!!.isShifted = isCap
                keyboardView!!.invalidateAllKeys()
            }

            Keyboard.KEYCODE_DONE -> inputConnection.sendKeyEvent(
                KeyEvent(
                    KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_ENTER
                )
            )

            else -> {
                var code = i.toChar()
                if (Character.isLetter(code) && isCap) code = code.uppercaseChar()
                inputConnection.commitText(code.toString(), 1)
            }
        }
    }

    private fun playClick(i: Int) {
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        when (i) {
            32 -> audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR)
            Keyboard.KEYCODE_DONE, 10 -> audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN)
            Keyboard.KEYCODE_DELETE -> audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE)
            else -> audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD)
        }
    }

    override fun onText(charSequence: CharSequence) {}
    override fun swipeLeft() {}
    override fun swipeRight() {}
    override fun swipeDown() {}
    override fun swipeUp() {}
}