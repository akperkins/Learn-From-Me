package com.overnightApps.myapplication.app.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import com.overnightApps.myapplication.app.R;

/**
 * Created by andre on 2/24/14.
 */
public class AUtil {
    public static String extractString(EditText editText) {
        return editText.getText().toString();
    }

    public static void confirmUserSelection(Context context, DialogInterface.OnClickListener onClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you Sure?").setPositiveButton(context.getString(R.string.cofirmUserMessage),
                onClickListener)
                .setNegativeButton(context.getString(R.string.denyUserMessage), onClickListener).show();
    }
}
