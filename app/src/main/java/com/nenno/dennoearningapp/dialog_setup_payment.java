package com.nenno.dennoearningapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class dialog_setup_payment {
    Context context;
    Dialog dialogo_Setup;
    TextView titulo_metodo, txt_pix_type;
    EditText name, pixKey;
    LinearLayout save, cancel;
    FirebaseAuth firebaseAuth;
    Spinner spinner;
    ImageView icone;
    FirebaseDatabase userInfo = FirebaseDatabase.getInstance();
    DatabaseReference add = userInfo.getReference("userInfo");
    String userKey;
    private HashMap<String, Object> mapa = new HashMap<>();

    public dialog_setup_payment(Context context){
        this.context = context;
    }
    public void builddg(boolean pixbr, boolean paypalwl){
        dialogo_Setup = new Dialog(context);
        dialogo_Setup.setContentView(R.layout.dialog_setup_pix);
        dialogo_Setup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        FirebaseApp.initializeApp(context);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        userKey = user.getUid();
        titulo_metodo = dialogo_Setup.findViewById(R.id.titulo_metodo);
        name = dialogo_Setup.findViewById(R.id.person_name);
        pixKey = dialogo_Setup.findViewById(R.id.pix_key);
        txt_pix_type = dialogo_Setup.findViewById(R.id.txt_pix_type);
        icone = dialogo_Setup.findViewById(R.id.imageView6);
        save = dialogo_Setup.findViewById(R.id.btn_send);
        cancel = dialogo_Setup.findViewById(R.id.btn_cancel);
        spinner = dialogo_Setup.findViewById(R.id.spinner);
        mapa = new HashMap<>();
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(context, R.array.PixKeyTypes, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        if (pixbr){
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String a = parent.getItemAtPosition(position).toString();
                    mapa.put("Pix key type", a);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else {
            txt_pix_type.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            pixKey.setHint(R.string.enter_paypal_email);
            titulo_metodo.setText(R.string.setup_paypal);
            icone.setImageResource(R.drawable.ic_paypal);
        }
        save.setOnClickListener(v -> {
         if (pixbr){
             if (pixKey.getText().toString().length() < 9 || name.getText().toString().length() < 6){
                 mySuperErrorToast(context.getString(R.string.setup_error));
             }else {
                 pixSave();
             }
         }else {
             if (paypalwl){
                 if (!pixKey.getText().toString().contains("@") || name.getText().toString().length() < 6){
                     mySuperErrorToast(context.getString(R.string.setup_pp_Error));
                 }else {
                     paypalSave();
                 }
             }
         }
        });

        cancel.setOnClickListener(v -> dialogo_Setup.hide());
        dialogo_Setup.setCancelable(false);
        dialogo_Setup.show();
    }
    private void mySuperToast(final String bconteudo) {
        Context context = dialogo_Setup.getContext();
        LayoutInflater inflater = dialogo_Setup.getLayoutInflater();
        @SuppressLint("InflateParams") View toastL = inflater.inflate(R.layout.custom_toast, null);
        LinearLayout lin1 = toastL.findViewById(R.id.toast_bg);
        lin1.setBackgroundResource(R.drawable.abcd);
        TextView content = toastL.findViewById(R.id.conteudo);
        content.setText(bconteudo);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastL);
        toast.show();
    }
    private void mySuperErrorToast(final String sconteudo) {
        Context context = dialogo_Setup.getContext();
        LayoutInflater inflater = dialogo_Setup.getLayoutInflater();
        @SuppressLint("InflateParams") View toastL = inflater.inflate(R.layout.custom_toast, null);
        LinearLayout lin1 = toastL.findViewById(R.id.toast_bg);
        lin1.setBackgroundResource(R.drawable.toast_error);
        TextView content = toastL.findViewById(R.id.conteudo);
        content.setText(sconteudo);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastL);
        toast.show();
    }
    public void pixSave(){
        mapa.put("PIX key", pixKey.getText().toString().trim());
        mapa.put("Real name PIX", name.getText().toString().trim());
        add.child(userKey).updateChildren(mapa);
        mapa.clear();
        mySuperToast(context.getString(R.string.setup_success));
        dialogo_Setup.hide();
    }
    public void paypalSave(){
        mapa.put("Paypal email", pixKey.getText().toString().trim());
        mapa.put("Real name PayPal", name.getText().toString().trim());
        add.child(userKey).updateChildren(mapa);
        mapa.clear();
        mySuperToast(context.getString(R.string.setup_success));
        dialogo_Setup.hide();
    }
}
