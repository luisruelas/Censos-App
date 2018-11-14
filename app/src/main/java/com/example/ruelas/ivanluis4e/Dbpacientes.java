package com.example.ruelas.ivanluis4e;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Ruelas on 25/10/2016.
 */
public class Dbpacientes extends SQLiteOpenHelper {
    Context context;
    private static final String dbname = "dbpacientes";
    private static final String tablename = "tablapacientes";
    private static final String[] COLS = new String[]{"_id", "nombre", "sexo", "peso", "talla", "temperatura", "perdidassensibles", "fecha", "apellido", "duplicado"};

    public Dbpacientes(Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + tablename + " (" + COLS[0] + " INTEGER PRIMARY KEY, "
                + COLS[1] + " TEXT, "
                + COLS[2] + " INTEGER, "
                + COLS[3] + " FLOAT, "
                + COLS[4] + "  FLOAT, "
                + COLS[5] + " FLOAT, "
                + COLS[6] + " FLOAT, "
                + COLS[7] + " TEXT, "
                + COLS[8] + " TEXT, "
                + COLS[9] + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
    public void borrarpcaienteconduplicados(int id){
        getWritableDatabase().delete(tablename, COLS[0] + "=? OR "+COLS[9]+"=?", new String[]{Integer.toString(id),Integer.toString(id)});
    }
    public void borrarPacientebyId(int id) {
        getWritableDatabase().delete(tablename, COLS[0] + "=?", new String[]{Integer.toString(id)});
    }

    public void actualizarpacienteaoriginal(Paciente paciente, int idpacienteoriginal) {
        //crear columna masactual con int booleano, en getpacientes solo dar los que sean true
        ContentValues cv = new ContentValues();
        cv.put(COLS[1],paciente.getNombre());
        int s;
        if(paciente.getSexo()){s=1;}else{s=0;}
        cv.put(COLS[2], Integer.toString(s));
        cv.put(COLS[3], paciente.getPeso());
        cv.put(COLS[4], paciente.getTalla());
        cv.put(COLS[5],paciente.getTemperatura());
        cv.put(COLS[6], paciente.getPerdidassen());
        cv.put(COLS[7],paciente.getFecha());
        cv.put(COLS[8], paciente.getApellido());
        String[]args=new String[]{Integer.toString(idpacienteoriginal)};
        getWritableDatabase().update(tablename, cv, COLS[0]+"=?",args);
        args=new String[]{Integer.toString(paciente.getid())};
        getWritableDatabase().delete(tablename,COLS[0]+"=?", args);
    }
    public long insertarpaciente(Paciente p, boolean duplicado) {
        ContentValues cv = new ContentValues();
        String[] colname = new String[]{COLS[1]};
        String[] namearg = new String[]{p.getNombre(), p.getApellido()};
        if (!duplicado) {
            Cursor c = getReadableDatabase().query(tablename, colname, COLS[1] + "=? AND " + COLS[8] + "=?", namearg, null, null, null);
            int i = 1;
            if (c.getCount() != 0) {
                while (true) {
                    namearg[0] = namearg[0] + "(" + i + ")";
                    c = getReadableDatabase().query(tablename, colname, COLS[1] + "=? AND " + COLS[8] + "=?", namearg, null, null, null);
                    if (c.getCount() == 0) {
                        cv.put(COLS[1], p.getNombre() + "(" + i + ")");
                        break;
                    }
                    i++;
                }
            } else {
                cv.put(COLS[1], p.getNombre());
            }
        } else {
            cv.put(COLS[1], p.getNombre());
        }
        int sexoint = 1;
        if (p.getSexo()) {
            sexoint = 1;
        } else {
            sexoint = 0;
        }
        cv.put(COLS[2], sexoint);
        cv.put(COLS[3], p.getPeso());
        cv.put(COLS[4], p.getTalla());
        cv.put(COLS[5], p.getTemperatura());
        cv.put(COLS[6], p.getPerdidassen());
        cv.put(COLS[7], p.getFecha());
        cv.put(COLS[8], p.getApellido());
        if (duplicado) {
            cv.put(COLS[9], p.getid());
        }
        long l = getWritableDatabase().insert(tablename, null, cv);
        return l;
    }

    public int cuantosduplicados(long idoriginal) {
        SQLiteDatabase db = getReadableDatabase();
        String[] colid = new String[]{COLS[9]};
        Cursor c = db.query(tablename, colid, colid[0] + "=?", new String[]{Long.toString(idoriginal)}, null, null, null);
        if (c.getCount() != 0)
            return c.getCount();
        else
            return 0;
    }

    public ArrayList<Paciente> getduplicados(long id) {
        ArrayList<Paciente> duplicados = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] coliddup = new String[]{COLS[0], COLS[9]};
        Cursor c = db.query(tablename, coliddup, COLS[0] + "=? OR " + COLS[9] + "=?", new String[]{Long.toString(id), Long.toString(id)}, null, null, null);
            while (c.moveToNext()) {
                duplicados.add(getpacientebyId(c.getInt(c.getColumnIndex(COLS[0]))));
            }
        return duplicados;
    }

    public ArrayList getPacientes() {
        ArrayList pacientes = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();
        String[] columns = COLS;
        Cursor c = db.query(tablename, columns, COLS[9] + " IS NULL", null, null, null, null);
        while (c.moveToNext()) {
            Boolean sexo;
            if (c.getInt(c.getColumnIndex(COLS[2])) == 1) {
                sexo = true;
            } else {
                sexo = false;
            }
            pacientes.add(
                    new Paciente(
                            c.getInt(c.getColumnIndex(COLS[0])),
                            c.getString(c.getColumnIndex(COLS[1])),
                            sexo,
                            c.getFloat(c.getColumnIndex(COLS[3])),
                            c.getFloat(c.getColumnIndex(COLS[4])),
                            c.getFloat(c.getColumnIndex(COLS[5])),
                            c.getFloat(c.getColumnIndex(COLS[6])),
                            c.getString(c.getColumnIndex(COLS[7])),
                            c.getString(c.getColumnIndex(COLS[8]))));
        }
        return pacientes;
    }

    public void redotable() {
        getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + tablename);
        onCreate(getWritableDatabase());
    }

    public Paciente getpacientebyId(long id) {
        Cursor c = getReadableDatabase().query(tablename, COLS, "_id=?", new String[]{Long.toString(id)}, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            boolean sexo;

            if (c.getInt(c.getColumnIndex(COLS[2])) == 1) {
                sexo = true;
            } else {
                sexo = false;
            }
            return new Paciente(c.getInt(c.getColumnIndex(COLS[0])),
                    c.getString(c.getColumnIndex(COLS[1])),
                    sexo,
                    c.getFloat(c.getColumnIndex(COLS[3])),
                    c.getFloat(c.getColumnIndex(COLS[4])),
                    c.getFloat(c.getColumnIndex(COLS[5])),
                    c.getFloat(c.getColumnIndex(COLS[6])),
                    c.getString(c.getColumnIndex(COLS[7])),
                    c.getString(c.getColumnIndex(COLS[8])));
        } else return null;
    }

    public Paciente getmasreciente(long id) {
        //este está mal, tienes que ordenarlos por la fecha que tiene el paciente, esto es un bypass
        String[] coliddup = new String[]{COLS[0], COLS[9]};
        String[] args = new String[]{Long.toString(id), Long.toString(id)};
        Cursor c = getReadableDatabase().query(tablename, coliddup, coliddup[0] + "=? OR " + coliddup[1] + "=?", args, null, null, null);
        int winnerid = 0;
        while (c.moveToNext()) {
            if (winnerid <= c.getInt(c.getColumnIndex(COLS[0]))) {
                winnerid = c.getInt(c.getColumnIndex(COLS[0]));
            }
        }
        return getpacientebyId(winnerid);
    }
}
