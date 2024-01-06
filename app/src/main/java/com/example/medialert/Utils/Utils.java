package com.example.medialert.Utils;

import com.google.android.material.textfield.TextInputEditText;

public class Utils {
    public static boolean checkEditTextIsNull(TextInputEditText editText, String error) {
        if (editText.getText().toString().isEmpty()){
            editText.setError(error);
            editText.requestFocus();
            return false;
        }else {
            return true;
        }
    }
}
