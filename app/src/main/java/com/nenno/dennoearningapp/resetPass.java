package com.nenno.dennoearningapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class resetPass {
    Context context;
    Dialog dialogo;
    TextView txt_send, msg, title, txt_cancel;
    EditText getEmail;
    LinearLayout send, cancel;
    FirebaseAuth firebaseAuth;
    successDialog successDialog;
    failureDialog failureDialog;

    public resetPass(Context context){
        this.context = context;
    }
    public void build(){
        dialogo = new Dialog(context);
        dialogo.setContentView(R.layout.reset_pass_dialog);
        dialogo.setCancelable(false);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        FirebaseApp.initializeApp(context);
        firebaseAuth = FirebaseAuth.getInstance();
        failureDialog = new failureDialog(context);
        successDialog = new successDialog(context);
        getEmail = dialogo.findViewById(R.id.getEmail);
        send = dialogo.findViewById(R.id.btn_send);
        msg = dialogo.findViewById(R.id.txt_msg);
        title = dialogo.findViewById(R.id.titulo);
        cancel = dialogo.findViewById(R.id.btn_cancel);
        txt_send = dialogo.findViewById(R.id.txt_send);
        txt_cancel = dialogo.findViewById(R.id.txt_cancel);
        GradientTxt(txt_cancel, txt_send, title, msg);
        cancel.setOnClickListener(v -> dialogo.dismiss());
        send.setOnClickListener(v -> {
            if (TextUtils.isEmpty(getEmail.getText().toString()) || (!getEmail.getText().toString().contains("@") || (getEmail.getText().toString().length() < 6))) {
                mySuperToast(context.getResources().getString(R.string.email_falso), true);
            } else {
                String theEmail = getEmail.getText().toString().trim();
               firebaseAuth.sendPasswordResetEmail(theEmail)
                       .addOnSuccessListener(unused -> {
                           dialogo.dismiss();
                           successDialog.showSuccessMsg(context.getResources().getString(R.string.e_mail_sent_with_success) ,context.getResources().getString(R.string.reset_pass_email_sent), false);
                       }) .addOnFailureListener(e -> {
                           dialogo.dismiss();
                           failureDialog.showFailureMsg();
                       });

        }
        });
        dialogo.show();
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
    private void mySuperToast(final String bconteudo, final boolean isError) {
        Context context = dialogo.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View toastL = inflater.inflate(R.layout.custom_toast, null);
        LinearLayout lin1 = toastL.findViewById(R.id.toast_bg);
        if (isError){
            lin1.setBackgroundResource(R.drawable.toast_error);
        }
        else {
            lin1.setBackgroundResource(R.drawable.success_toast_bg);
        }
        TextView content = toastL.findViewById(R.id.conteudo);
        content.setText(bconteudo);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastL);
        toast.show();
    }

}
