package com.foc.pmdm.u3.mireproductor;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp=new MediaPlayer();


    }

    static final int CodigoSeleccionVideo=666;  // Código del diálogo de seleccion de vídeo
    static final int CodigoSeleccionAudio=667;// Código del diálogo de seleccion de audio
    void selecconarElmentoConDialogo()
    {
        Intent i=new Intent(Intent.ACTION_GET_CONTENT);
        getIntent().setType("video/*");
        getIntent().addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(Intent.createChooser(i,"Seleccion un video"),CodigoSeleccionVideo);
    }

    protected void onActivityResult(int codigo,int resultado,Intent datos)
    {
        switch(codigo)
        {
            case CodigoSeleccionVideo:
                if(resultado==RESULT_OK) {

                }

                break;

            case CodigoSeleccionAudio:
                break;

            default:
                break;
        }
    }

    void obtenerElementosReproducibles()
    {
        ContentResolver cr=getContentResolver();
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; // Generico - tipo
        Cursor resultado=cr.query(uri,null,MediaStore.Audio.Media.ARTIST+"='Manolo Santana'",null,null);
        if((resultado!=null)&& (resultado.moveToFirst()!=false))
        {
            int iColTitulo=resultado.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int iColID=resultado.getColumnIndex(MediaStore.Audio.Media._ID);

            String sTitulo=resultado.getString(iColTitulo);
            long id=resultado.getLong(iColID);

            // Uri concreto del fichero de sonido de Manolo Santano
            Uri uriContenido= ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id);

            try {
                mp.setDataSource(this,uriContenido);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void setDesbloqueo()
    {
        mp.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
    }

}
