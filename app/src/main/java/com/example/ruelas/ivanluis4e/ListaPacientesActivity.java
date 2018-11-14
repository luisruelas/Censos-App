package com.example.ruelas.ivanluis4e;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListaPacientesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, PopupWindow.OnDismissListener {
    private ListView lvpacientes;
    Dbpacientes db;
    Button btpopupeliminar, btpopupcancelar;
    PacientesAdapter pa;
    LinearLayout lllvpacientes,lllistapacientes,llalpha;
    ArrayList<Paciente> al;
    PopupWindow pwdelete;
    View viewpopupwindow;
    String advertenciaeliminarpaciente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listapacientes);
        android.support.v7.app.ActionBar ab=getSupportActionBar();
        ab.setTitle("Mis Pacientes");
        //ACTUALIZAR LA PUTA LISTA, la parte de abajo parece suficiente
        //pacientes=new Paciente[]{new Paciente("Luis"), new Paciente("Esteban")};
        llalpha= (LinearLayout) findViewById(R.id.llalpha);
        advertenciaeliminarpaciente=getResources().getString(R.string.advertenciaEliminarPaciente);
        lllistapacientes= (LinearLayout) findViewById(R.id.lllistadepacientes);
        Dbpacientes dbpac = new Dbpacientes(this);
        al = dbpac.getPacientes();
        lvpacientes = (ListView) findViewById(R.id.lvpacientes);
        lllvpacientes = (LinearLayout) findViewById(R.id.lllvpacientes);
        addNopatientText();

        LayoutInflater li= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        viewpopupwindow=li.inflate(R.layout.popupwindow, lllistapacientes, false);
        TextView tvadv= (TextView) viewpopupwindow.findViewById(R.id.tvadvertenciatext);
        tvadv.setText(advertenciaeliminarpaciente);
        pwdelete=new PopupWindow(viewpopupwindow, RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        pwdelete.setFocusable(true);
        pwdelete.setOutsideTouchable(true);
        pwdelete.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pwdelete.setOnDismissListener(this);

        btpopupeliminar = (Button) viewpopupwindow.findViewById(R.id.btpopupeliminar);
        btpopupcancelar = (Button) viewpopupwindow.findViewById(R.id.btpopupcancelar);
        db=new Dbpacientes(this);
    }
    public void addNopatientText(){
        if (al.size()==0) {
            Float fontSize = getResources().getDimension(R.dimen.fontlabel) / getResources().getDisplayMetrics().density;
            lvpacientes.setVisibility(View.GONE);
            TextView nopac = new TextView(this);
            nopac.setTextSize(Dimension.SP, fontSize);
            nopac.setText("NO HAY PACIENTES");
            lllvpacientes.addView(nopac);
        }
        else{
            lvpacientes.setVisibility(View.VISIBLE);
            pa = new PacientesAdapter(this, 1);
            lvpacientes.setAdapter(pa);
            lvpacientes.setOnItemClickListener(this);
        }
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Paciente estepaciente = (Paciente) pa.getItem(i);
        long id = (long) estepaciente.getid();
        Intent intent = new Intent(this, Activity_Infopac.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
       super.onBackPressed();
    }

    @Override
    public void onDismiss() {
        llalpha.setVisibility(View.INVISIBLE);
    }

    private class PacientesAdapter extends ArrayAdapter implements Filterable {
        Context context;
        private Paciente pc;
        ArrayAdapter arrayAdapter;
        //ArrayList<Paciente> pacientes;
        Dbpacientes db;

        @Override
        public int getCount() {
            return al.size();
        }

        public PacientesAdapter(Context context, int resource) {
            super(context, resource);
            this.context = context;
            this.arrayAdapter = this;
            db = new Dbpacientes(context);
            //pacientes = db.getPacientes();

        }

        @Override
        public Object getItem(int position) {
            return al.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder vh = new ViewHolder();
            if (convertView == null) {
                LayoutInflater i = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = i.inflate(R.layout.rowpaciente, parent, false);
                vh.nombre = (TextView) convertView.findViewById(R.id.tvnombre);
                vh.apellido = (TextView) convertView.findViewById(R.id.tvapellido);
                vh.bttrash = (Button) convertView.findViewById(R.id.bteliminar);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            pc = al.get(position);
            if (pc.getApellido().trim().equals("")) {
                vh.apellido.setText("ANÓNIMO");
            } else {
                if (pc.getApellido().length() >= 13) {
                    String apellido = pc.getApellido().substring(0, 12) + "...";
                    vh.apellido.setText(apellido);
                } else {
                    vh.apellido.setText(pc.getApellido());
                }
            }
            if (pc.getNombre().trim().equals("")) {
                vh.nombre.setText("ANONIMO");
            } else {
                if (pc.getNombre().length() >= 13) {
                    String nombre = pc.getNombre().substring(0, 12) + "...";
                    vh.nombre.setText(nombre);
                } else {
                    vh.nombre.setText(pc.getNombre());
                }
            }
            ButtonHolder bh = new ButtonHolder();
            bh.pac = pc;
            bh.pos = position;
            vh.bttrash.setTag(bh);
            vh.bttrash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ButtonHolder bh = (ButtonHolder) view.getTag();
                    btpopupeliminar.setTag(bh);
                    btpopupcancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pwdelete.dismiss();
                        }
                    });
                    btpopupeliminar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ButtonHolder bh = (ButtonHolder) view.getTag();
                            db.borrarpcaienteconduplicados(bh.pac.getid());
                            al.remove(bh.pos);
                            arrayAdapter.notifyDataSetChanged();
                            addNopatientText();
                            pwdelete.dismiss();
                            Toast.makeText(context,"Paciente eliminado",Toast.LENGTH_SHORT).show();
                        }
                    });
                    pwdelete.showAtLocation(lllvpacientes, Gravity.CENTER, 0,(int)lllistapacientes.getY());
                    llalpha.setVisibility(View.VISIBLE);
                }
            });
            return convertView;
        }

        private class ViewHolder {
            public TextView nombre;
            public TextView apellido;
            public Button bttrash;
        }
        private class ButtonHolder {
            public Paciente pac;
            public int pos;
        }

    }
}