package com.example.ruelas.ivanluis4e;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Activity_Infopac extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener, PopupWindow.OnDismissListener {
    TextView tvpesores, tvnombre,tvfecha;
    Boolean mostrar=false;
    String unset = "-";
    private String unsetnombre = "ANÓNIMO";
    private TextView tvtallares;
    private TextView tvtemperaturares;
    Paciente thispaciente;
    private String unidadtemp="°C";
    private String unidadtalla="cm";
    private String unidadpeso="Kg";
    LinearLayout llalpha;

    ScrollView svdetalles;
    Dbpacientes db;
    float RB, PI, PS, GXM, MGXM, RT;
    LinearLayout lldetalles, lltvdetalles;
    ImageView ivfadetalles;
    HashMap Extras;
    int dissapearduration=500;
    Boolean detallesvis, actualizar;
    DecimalFormat df=new DecimalFormat("##.##");
    Animation appear= new AlphaAnimation(0f,1f);
    Animation disappear= new AlphaAnimation(1f,0f);
    TextView tvRB, tvPI, tvPS, tvRT, tvsodio, tvpotasio, tvglucosa,tvapellido;
    Button btloadnext;
    Button btloadprevious;
    Button btactualizarregistro;
    Button bteliminar;
    Long id;
    LinearLayout llinfopacroot;
    ArrayList<Paciente> todoslospacientesconesteid;
    int posicion;
    Bundle b;
    View viewpopupwindow;
    String advertenciaeliminarregistro;
    PopupWindow pwdelete;
    private Button btpopupeliminar, btpopupcancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__infopac);
        android.support.v7.app.ActionBar ab=getSupportActionBar();
        ab.setTitle("Información del Paciente");
        llalpha= (LinearLayout) findViewById(R.id.llalpha);
        advertenciaeliminarregistro= getString(R.string.advertenciaEliminarRegistro);
        llinfopacroot= (LinearLayout) findViewById(R.id.llinfopacroot);
        LayoutInflater li= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        viewpopupwindow=li.inflate(R.layout.popupwindow, llinfopacroot, false);
        TextView tvadv= (TextView) viewpopupwindow.findViewById(R.id.tvadvertenciatext);
        tvadv.setText(advertenciaeliminarregistro);

        pwdelete=new PopupWindow(viewpopupwindow, RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        pwdelete.setFocusable(true);
        pwdelete.setOutsideTouchable(true);
        pwdelete.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pwdelete.setOnDismissListener(this);

        btpopupeliminar = (Button) viewpopupwindow.findViewById(R.id.btpopupeliminar);
        btpopupcancelar= (Button) viewpopupwindow.findViewById(R.id.btpopupcancelar);
        btpopupcancelar.setOnClickListener(this);
        btpopupeliminar.setOnClickListener(this);

        b = getIntent().getExtras();
        db = new Dbpacientes(this);
        btloadnext = (Button) findViewById(R.id.btloadnext);
        btloadnext.setOnClickListener(this);
        btloadprevious= (Button) findViewById(R.id.btloadprevious);
        btloadprevious.setOnClickListener(this);
        btloadnext.setVisibility(View.INVISIBLE);
        btloadprevious.setVisibility(View.INVISIBLE);
        bteliminar= (Button) findViewById(R.id.bteliminar);
        bteliminar.setOnClickListener(this);
        btactualizarregistro= (Button) findViewById(R.id.btactualizarregistro);
        btactualizarregistro.setOnClickListener(this);
        if (b!=null) {
            id = (long) b.get("id");
            mostrar=b.getBoolean("mostrar");
            if(id!=null) {
                this.thispaciente = db.getmasreciente(id);
                if (db.cuantosduplicados(id) != 0 && !mostrar) {
                    btloadnext.setVisibility(View.INVISIBLE);
                    btloadprevious.setVisibility(View.VISIBLE);
                }
                if(mostrar){
                    db.borrarPacientebyId(thispaciente.getid());
                    bteliminar.setVisibility(View.GONE);
                    btactualizarregistro.setVisibility(View.GONE);
                }
            }

        }
        tvfecha= (TextView) findViewById(R.id.tvfecha);
        todoslospacientesconesteid=db.getduplicados(id);
        posicion=todoslospacientesconesteid.size()-1;
        ivfadetalles= (ImageView) findViewById(R.id.ivfadetalles);
        tvRB= (TextView) findViewById(R.id.tvrequerimientobasal);

        tvRT= (TextView) findViewById(R.id.tvrequerimientototal);
        tvPI= (TextView) findViewById(R.id.tvperdidasins);
        tvPS= (TextView) findViewById(R.id.tvperdidassen);
        tvsodio= (TextView) findViewById(R.id.tvsodio);
        tvpotasio= (TextView) findViewById(R.id.tvpotasio);
        tvglucosa= (TextView) findViewById(R.id.tvglucosa);

        tvapellido= (TextView) findViewById(R.id.tvresapellidopac);
        tvpesores = (TextView) findViewById(R.id.tvrespeso);
        tvtallares = (TextView) findViewById(R.id.tvrestalla);
        tvtemperaturares = (TextView) findViewById(R.id.tvrestemp);
        tvnombre = (TextView) findViewById(R.id.tvresnombrepac);

        svdetalles= (ScrollView) findViewById(R.id.svdetalles);

        appear.setDuration(dissapearduration);
        appear.setAnimationListener(this);
        disappear.setDuration(dissapearduration);
        disappear.setAnimationListener(this);
        detallesvis=false;
        lldetalles= (LinearLayout) findViewById(R.id.lldetalles);
        lldetalles.setVisibility(View.GONE);
        lltvdetalles= (LinearLayout) findViewById(R.id.lltvdetalles);
        lltvdetalles.setOnClickListener(this);
        setTexts();
    }

    private void setTexts() {
        HashMap elect=Converter.calcularExtras();
        tvfecha.setText(thispaciente.parseFecha(thispaciente.getFecha()));
        tvPS.setText(df.format(Converter.calcularPS()));
        tvRB.setText(df.format(Converter.calcularRB()));
        tvPI.setText(df.format(Converter.calcularPI()));
        tvRT.setText(df.format(Converter.calcularRT()));
        tvglucosa.setText(df.format(elect.get("glucosa")));
        tvsodio.setText(df.format(elect.get("sodio")));
        tvpotasio.setText(df.format(elect.get("potasio")));
        String fecha=thispaciente.parseFecha(thispaciente.getFecha());
        tvfecha.setText(fecha);
        String apellido;
        if (thispaciente.getApellido().equals(""))
            apellido = unsetnombre;
        else {
            apellido = thispaciente.getApellido();
        }
        tvapellido.setText(apellido);
        String nombre;
        if (thispaciente.getNombre().equals(""))
            nombre = unsetnombre;
        else {
            nombre = thispaciente.getNombre();
        }
        tvnombre.setText(nombre);
        String peso;
        if (thispaciente.getPeso()==0)
        {peso = unset;
            tvpesores.setText(peso);
        }
        else {
            peso = Float.toString(thispaciente.getPeso());
            tvpesores.setText(peso+unidadpeso);
        }

        String talla;
        if (thispaciente.getTalla()==0) {
            talla = unset;
            tvtallares.setText(talla);
        }
        else {
            talla = Float.toString(thispaciente.getTalla());
            tvtallares.setText(talla+unidadtalla);
        }
        ;
        String temp;
        if (thispaciente.getTemperatura()==0) {
            temp = unset;
            tvtemperaturares.setText(temp);
        }
        else {
            temp = Float.toString(thispaciente.getTemperatura());
            tvtemperaturares.setText(temp+unidadtemp);
        }

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btpopupcancelar){
            pwdelete.dismiss();
        }
        if(view.getId()==R.id.btpopupeliminar){
            //si es el unico eliminarlo y salir
            if(todoslospacientesconesteid.size()==1){
                db.borrarPacientebyId(thispaciente.getid());
                startActivity(new Intent(this, ListaPacientesActivity.class));
                finish();
            }
            //sies el primero eliminarlo, actualizar el siguiente registro para tomar su id, quitarle el duplicado y refrescar el array
            else if(thispaciente.getid()==todoslospacientesconesteid.get(0).getid()){
                db.actualizarpacienteaoriginal(todoslospacientesconesteid.get(1),thispaciente.getid());
                todoslospacientesconesteid=db.getduplicados(thispaciente.getid());
                posicion--;
                btloadnext.performClick();
            }
            //si no eliminar registro y mostrar el siguiente
            else {
                //si es el ultimo registro eliminarlo e ir hacia atras
                if (thispaciente.getid()==todoslospacientesconesteid.get(todoslospacientesconesteid.size()-1).getid()) {
                    db.borrarPacientebyId(thispaciente.getid());
                    todoslospacientesconesteid = db.getduplicados(todoslospacientesconesteid.get(0).getid());
                    btloadprevious.performClick();
                }
                //si no eliminarlo e ir hacia adelante
                else{
                    db.borrarPacientebyId(thispaciente.getid());
                    todoslospacientesconesteid = db.getduplicados(todoslospacientesconesteid.get(0).getid());
                    posicion--;
                    btloadnext.performClick();
                }
            }
            pwdelete.dismiss();
            Toast.makeText(this,"Registro eliminado",Toast.LENGTH_SHORT).show();
        }
        switch (view.getId()) {
            case R.id.bteliminar:
                //mamadas de popup
                pwdelete.showAtLocation(llinfopacroot, Gravity.CENTER,0,0);
                llalpha.setVisibility(View.VISIBLE);
                break;
            case R.id.lltvdetalles:
                if (detallesvis) {
                    ivfadetalles.setVisibility(View.VISIBLE);
                    lldetalles.startAnimation(disappear);
                } else {
                    ivfadetalles.setVisibility(View.GONE);
                    lldetalles.setVisibility(View.VISIBLE);
                    lldetalles.startAnimation(appear);
                }
                detallesvis = !detallesvis;
                break;
            case R.id.btloadnext:
                if(posicion!=todoslospacientesconesteid.size()-1) {
                    posicion++;
                    thispaciente=todoslospacientesconesteid.get(posicion);
                    btloadprevious.setVisibility(View.VISIBLE);
                    setTexts();
                }
                if(posicion==todoslospacientesconesteid.size()-1){
                    btloadnext.setVisibility(View.INVISIBLE);
                }
                if(posicion==0){
                    btloadprevious.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.btloadprevious:
                if(posicion!=0) {
                    posicion--;
                    thispaciente=todoslospacientesconesteid.get(posicion);
                    if(todoslospacientesconesteid.size()-1==posicion) {
                        btloadnext.setVisibility(View.INVISIBLE);
                    }else{btloadnext.setVisibility(View.VISIBLE);}
                    setTexts();
                }
                if(posicion==0){
                    btloadprevious.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.btactualizarregistro:
                Intent i= new Intent(this, FormularioActivity.class);
                i.putExtra("id",id);
                startActivity(i);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(todoslospacientesconesteid.size()!=db.getduplicados(id).size())
        {
            overridePendingTransition(0,0);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if(detallesvis) {
            lldetalles.startAnimation(disappear);
            detallesvis=!detallesvis;
        }
        else{
            if(mostrar) {
                super.onBackPressed();
                finish();
            }
            else{
                super.onBackPressed();
                finish();
            }
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
    if (animation==disappear) {
        lldetalles.setVisibility(View.GONE);
    }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onDismiss() {
        llalpha.setVisibility(View.INVISIBLE);
    }
}
