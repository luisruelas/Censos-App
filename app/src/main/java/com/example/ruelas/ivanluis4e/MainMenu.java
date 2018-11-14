package com.example.ruelas.ivanluis4e;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{
TextView tvmain,tvpac, tvmientras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        android.support.v7.app.ActionBar ab=getSupportActionBar();
        ab.setTitle("Calculadora de Soluciones");
        tvmain= (TextView) findViewById(R.id.tvgomain);
        tvmain.setOnClickListener(this);
        tvpac= (TextView) findViewById(R.id.tvgopacs);
        tvpac.setOnClickListener(this);
        tvmientras= (TextView) findViewById(R.id.tvchingarbd);
        tvmientras.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i= new Intent();
        switch (view.getId()){
            case R.id.tvgomain:
                i.setClass(this, FormularioActivity.class);
                startActivity(i);
                break;
            case R.id.tvgopacs:
                i.setClass(this, ListaPacientesActivity.class);
                startActivity(i);
                break;
            /*case R.id.tvchingarbd:
                Dbpacientes db= new Dbpacientes(this);
                db.redotable();
                break;*/
        }
    }
}
