package com.nenno.dennoearningapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;



public class failureDialog {
 Context context;
 Dialog dialogoo;
 TextView txt_send, msg, title, txt_cancel;
 EditText getEmail;
 LinearLayout send, cancel;

 public failureDialog(Context context){
  this.context = context;
 }
 @SuppressLint("SetTextI18n")
 public void showFailureMsg(){
  dialogoo = new Dialog(context);
  dialogoo.setContentView(R.layout.reset_pass_dialog);
  dialogoo.setCancelable(false);
  dialogoo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
  getEmail = dialogoo.findViewById(R.id.getEmail);
  send = dialogoo.findViewById(R.id.btn_send);
  msg = dialogoo.findViewById(R.id.txt_msg);
  title = dialogoo.findViewById(R.id.titulo);
  cancel = dialogoo.findViewById(R.id.btn_cancel);
  txt_send = dialogoo.findViewById(R.id.txt_send);
  txt_cancel = dialogoo.findViewById(R.id.txt_cancel);
  getEmail.setVisibility(View.GONE);
  title.setText(context.getResources().getString(R.string.e_mail_not_send));
  msg.setText(context.getResources().getString(R.string.reset_pass_email_not_send) + ".");
  msg.setTextSize((int)(18));
  title.setTextSize((int)(20));
  cancel.setVisibility(View.GONE);
  GradientTxt(msg, txt_cancel, txt_send, title);
  txt_send.setText(context.getResources().getString(R.string.ok));
  send.setOnClickListener(v -> {
   dialogoo.dismiss();
  });
  dialogoo.show();
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
 }}