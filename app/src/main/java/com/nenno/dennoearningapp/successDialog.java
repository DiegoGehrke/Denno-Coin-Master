package com.nenno.dennoearningapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class successDialog {
    Context context;
    Dialog dialoga;
    TextView txt_send, msg, title, txt_cancel;
    EditText getEmail;
    LinearLayout send, cancel;

    public successDialog(Context context){
        this.context = context;
    }
    public void showSuccessMsg(String titulo, String msgText, Boolean isWithdrawActivity){
        dialoga = new Dialog(context);
        dialoga.setContentView(R.layout.reset_pass_dialog);
        dialoga.setCancelable(false);
        dialoga.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getEmail = dialoga.findViewById(R.id.getEmail);
        send = dialoga.findViewById(R.id.btn_send);
        msg = dialoga.findViewById(R.id.txt_msg);
        title = dialoga.findViewById(R.id.titulo);
        cancel = dialoga.findViewById(R.id.btn_cancel);
        txt_send = dialoga.findViewById(R.id.txt_send);
        txt_cancel = dialoga.findViewById(R.id.txt_cancel);
        GradientTxt(txt_cancel, txt_send, title, msg);
        getEmail.setVisibility(View.GONE);
        msg.setTextSize((int)(18));
        title.setTextSize((int)(20));
        title.setText(titulo);
        msg.setText(msgText);
        cancel.setVisibility(View.GONE);
        txt_send.setText(context.getResources().getString(R.string.ok));
        if (isWithdrawActivity){
            send.setOnClickListener(v -> {
                dialoga.dismiss();
                context.startActivity(new Intent(context, configs.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            });
        }
        else {
            send.setOnClickListener(v -> dialoga.dismiss());
        }
        dialoga.show();
}
    public void GradientTxt(final TextView _view0, final TextView _view1, final TextView _view2, final TextView _view3){
        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.rgb(254, 228, 188), Color.rgb(242, 206, 127),
                Shader.TileMode.CLAMP );
        _view0.getPaint().setShader( myShader );
        _view1.getPaint().setShader( myShader );
        _view2.getPaint().setShader( myShader );
        _view3.getPaint().setShader( myShader );
    }

}
