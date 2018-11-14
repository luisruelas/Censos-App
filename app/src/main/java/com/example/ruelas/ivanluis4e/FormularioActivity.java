package com.example.ruelas.ivanluis4e;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Dimension;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class FormularioActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etnombre, etpeso, ettalla, etperdidassen, etsondas, etdrenajes, ettemperatura, ethoras, etapellido;
    TextView tvrernombre, tvresppeso, tvrestalla, tvresperdidasins, tvsodio, tvpotasio, tvglucosa;
    TextView tvsondas, tvdrenajes, tvfechaampm;
    EditText etfechadia, etfechames, etfechaaño, etfechahora, etfechamin;
    Boolean sexo = true;
    Button btsc;
    Button btpeso;
    ImageView ivmas;
    Button btguardar, btmostrar, btmasc, btfem;
    LinearLayout llperdidassen;
    Dbpacientes db;
    Bundle b;
    Integer id;
    final String[]campos=new String[]{"nombre","apellido","peso","perdidassen","temperatura","horas","talla"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        android.support.v7.app.ActionBar ab=getSupportActionBar();
        ab.setTitle("Formulario");
        if(savedInstanceState!=null){
            setTexts(savedInstanceState);
        }
        b = getIntent().getExtras();
        Float fontSize = getResources().getDimension(R.dimen.fontlabel) / getResources().getDisplayMetrics().density;
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 100, 0, 0);
        db = new Dbpacientes(this);
        Calendar c = new GregorianCalendar();
        //settexts
        etfechaaño = (EditText) findViewById(R.id.etfechaaño);
        etfechames = (EditText) findViewById(R.id.etfechames);
        etfechadia = (EditText) findViewById(R.id.etfechadia);
        etfechahora = (EditText) findViewById(R.id.etfechahora);
        etfechamin = (EditText) findViewById(R.id.etfechaminutos);
        tvfechaampm = (TextView) findViewById(R.id.etfechaampm);

        etfechaaño.setText(Integer.toString(c.get(Calendar.YEAR)));
        etfechames.setText(Integer.toString(c.get(Calendar.MONTH) + 1));
        etfechadia.setText(Integer.toString(c.get(Calendar.DATE)));
        etfechahora.setText(Integer.toString(c.get(Calendar.HOUR)));
        etfechamin.setText(Integer.toString(c.get(Calendar.MINUTE)));
        String ampm = "pm";
        if (c.get(Calendar.AM_PM) == 0) {
            ampm = "am";
        }
        tvfechaampm.setText(ampm);

        llperdidassen = (LinearLayout) findViewById(R.id.llperdidassensibles);
        ethoras = (EditText) findViewById(R.id.ethoras);
        etapellido = (EditText) findViewById(R.id.tvapellido);
        etnombre = (EditText) findViewById(R.id.etnombre);
        etpeso = (EditText) findViewById(R.id.etpeso);
        ettalla = (EditText) findViewById(R.id.ettalla);
        etperdidassen = (EditText) findViewById(R.id.etperdidassensibles);
        ettemperatura = (EditText) findViewById(R.id.ettemperatura);
        tvsondas = new TextView(this);
        tvsondas.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tvsondas.setLayoutParams(lp);
        tvsondas.setGravity(Gravity.CENTER_HORIZONTAL);
        tvsondas.setTextSize(Dimension.SP, fontSize);
        tvsondas.setText("Sondas");
        tvdrenajes = new TextView(this);
        tvdrenajes.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tvdrenajes.setLayoutParams(lp);
        tvdrenajes.setGravity(Gravity.CENTER_HORIZONTAL);
        tvdrenajes.setTextSize(Dimension.SP, fontSize);
        tvdrenajes.setText("Drenajes");
        etsondas = new EditText(this);
        etsondas.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etsondas.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
        etsondas.setBackground(ContextCompat.getDrawable(this, R.drawable.setres));
        etsondas.setLayoutParams(lp);
        etsondas.setGravity(Gravity.CENTER_HORIZONTAL);
        etsondas.setTextSize(Dimension.SP, fontSize);
        etdrenajes = new EditText(this);
        etdrenajes.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etdrenajes.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
        etdrenajes.setBackground(ContextCompat.getDrawable(this, R.drawable.setres));
        etdrenajes.setLayoutParams(lp);
        etdrenajes.setTextSize(Dimension.SP, fontSize);
        etdrenajes.setGravity(Gravity.CENTER_HORIZONTAL);
        ivmas = (ImageView) findViewById(R.id.ivmas);
        ivmas.setOnClickListener(this);
        btmasc = (Button) findViewById(R.id.btmasc);
        btmasc.setOnClickListener(this);
        btmasc.setBackground(ContextCompat.getDrawable(this, R.drawable.layerlistmasculinopres));
        btfem = (Button) findViewById(R.id.btfem);
        btfem.setOnClickListener(this);
        btsc = (Button) findViewById(R.id.btsc);
        btsc.setOnClickListener(this);
        btpeso = (Button) findViewById(R.id.btpeso);
        btpeso.setOnClickListener(this);
        btguardar = (Button) findViewById(R.id.btguardar);
        btguardar.setOnClickListener(this);
        btguardar.setClickable(true);
        btmostrar = (Button) findViewById(R.id.btmostrar);
        btmostrar.setOnClickListener(this);
        if (b != null) {
            id = Longtoint(b.getLong("id"));
            Paciente p = db.getpacientebyId(id);
            etnombre.setText(p.getNombre());
            etnombre.setEnabled(false);
            etapellido.setText(p.getApellido());
            etapellido.setEnabled(false);
        }
    }

    private void setTexts(Bundle savedInstanceState) {
        setTextaux(etnombre,savedInstanceState,campos[0]);
        setTextaux(etapellido,savedInstanceState,campos[1]);
        setTextaux(etpeso,savedInstanceState,campos[2]);
        setTextaux(etperdidassen,savedInstanceState,campos[3]);
        setTextaux(ettemperatura,savedInstanceState,campos[4]);
        setTextaux(ethoras,savedInstanceState,campos[5]);
        setTextaux(ettalla,savedInstanceState,campos[6]);
    }
    private void setTextaux(EditText et, Bundle b, String key){
        String s= b.getString(key);
        if(s!=null) {
            et.setText(s);
        }
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btguardar || view.getId() == R.id.btmostrar) {
            if (revisarcampos()) {
                String nombre = "";
                String fecha = "";
                String apellido = "";
                Float peso = 0f, talla = 0f, temperatura = 0f, perdidassen = 0f;
                if (!etpeso.getText().toString().trim().equals("")) {
                    peso = Float.parseFloat(etpeso.getText().toString());
                } else {
                    peso = 0f;
                }
                if (!ettalla.getText().toString().trim().equals("")) {
                    talla = Float.parseFloat(ettalla.getText().toString());
                } else {
                    talla = 0f;
                }
                if (!ettemperatura.getText().toString().trim().equals("")) {
                    temperatura = Float.parseFloat(ettemperatura.getText().toString());
                } else {
                    temperatura = 0f;
                }
                if (!etperdidassen.getText().toString().trim().equals("")) {
                    perdidassen = Float.parseFloat(etperdidassen.getText().toString());
                } else {
                    perdidassen = 0f;
                }
                if (!etnombre.getText().toString().trim().equals("")) {
                    nombre = etnombre.getText().toString();
                } else {
                    nombre = "";
                }
                if (!etapellido.getText().toString().trim().equals("")) {
                    apellido = etapellido.getText().toString();
                } else {
                    apellido = "";
                }
                int minuto = Integer.parseInt(etfechamin.getText().toString());
                int hora = Integer.parseInt(etfechahora.getText().toString());
                int dia = Integer.parseInt(etfechadia.getText().toString());
                int mes = Integer.parseInt(etfechames.getText().toString());
                int año = Integer.parseInt(etfechaaño.getText().toString());
                String ampm = tvfechaampm.getText().toString();
                fecha = parsefecha(minuto, hora, dia, mes, año, ampm);
                if (b != null) {
                    Paciente p = new Paciente(id, nombre, sexo, peso, talla, temperatura, perdidassen, fecha, apellido);
                    db.insertarpaciente(p, true);
                    if (view.getId() == R.id.btmostrar) {
                        startActivity(prepare(id, true));
                    } else {
                        startActivity(prepare(id, false));
                        finish();
                    }
                } else {
                    Paciente p = new Paciente(0, nombre, sexo, peso, talla, temperatura, perdidassen, fecha, apellido);
                    long id = db.insertarpaciente(p, false);
                    if (view.getId() == R.id.btmostrar) {
                        startActivity(prepare(id, true));
                    } else {
                        startActivity(prepare(id, false));
                        finish();
                    }
                }
            }
        }
        switch (view.getId()) {
            case R.id.ivmas:
                if (llperdidassen.getChildCount() == 0) {
                    llperdidassen.addView(tvsondas);
                    llperdidassen.addView(etsondas);
                    llperdidassen.addView(tvdrenajes);
                    llperdidassen.addView(etdrenajes);
                } else {
                    llperdidassen.removeView(tvsondas);
                    llperdidassen.removeView(etsondas);
                    llperdidassen.removeView(tvdrenajes);
                    llperdidassen.removeView(etdrenajes);
                }
                break;
            case R.id.btmasc:
                if (!sexo) {
                    btmasc.setBackground(ContextCompat.getDrawable(this, R.drawable.layerlistmasculinopres));
                    btfem.setBackground(ContextCompat.getDrawable(this, R.drawable.layerlistfemenino));
                    sexo = !sexo;
                }
                break;
            case R.id.btfem:
                if (sexo) {
                    sexo = !sexo;
                    btfem.setBackground(ContextCompat.getDrawable(this, R.drawable.layerlistfemeninopres));
                    btmasc.setBackground(ContextCompat.getDrawable(this, R.drawable.layerlistmasculino));
                }
                break;
        }

    }

    private boolean revisarcampos() {
        return true;
    }

    private Intent prepare(long id, boolean mostrar) {
        Intent i = new Intent(this, Activity_Infopac.class);
        i.putExtra("id", id);
        i.putExtra("mostrar", mostrar);
        return i;
    }

    private String parsefecha(int minuto, int hora, int dia, int mes, int año, String ampm) {
        String sminuto;
        String shora;
        String sdia;
        String smes;
        String saño;
        if (Integer.toString(minuto).length() == 1)
            sminuto = "0" + minuto;
        else {
            sminuto = Integer.toString(minuto);
        }
        if (Integer.toString(hora).length() == 1)
            shora = "0" + hora;
        else {
            shora = Integer.toString(hora);
        }
        if (Integer.toString(dia).length() == 1)
            sdia = "0" + dia;
        else {
            sdia = Integer.toString(dia);
        }
        if (Integer.toString(mes).length() == 1)
            smes = "0" + mes;
        else {
            smes = Integer.toString(mes);
        }
        if (Integer.toString(año).length() != 4)
            saño = "2000";
        else {
            saño = Integer.toString(año);
        }
        String fecha = sminuto + shora + sdia + smes + saño + ampm;
        return fecha;
    }

    public static Integer Longtoint(Long l) {
        if (l < Integer.MAX_VALUE && l > Integer.MIN_VALUE) {
            String converter = Long.toString(l);
            return Integer.parseInt(converter);
        } else return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString(campos[0], etnombre.getText().toString());
        outState.putString(campos[1], etapellido.getText().toString());
        outState.putString(campos[2], etpeso.getText().toString());
        outState.putString(campos[3], etperdidassen.getText().toString());
        outState.putString(campos[4], ettemperatura.getText().toString());
        outState.putString(campos[5],ethoras.getText().toString());
        outState.putString(campos[5],ettalla.getText().toString());
        super.onSaveInstanceState(outState, outPersistentState);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
