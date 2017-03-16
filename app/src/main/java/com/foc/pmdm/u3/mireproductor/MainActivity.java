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
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp=new MediaPlayer();

        if(savedInstanceState!=null)
        {
            int iPos=savedInstanceState.getInt("posicion");
            if(bPlaying) {
                mp.seekTo(iPos);
                mp.start();
            }

        }

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null)
        {
            int iPos=savedInstanceState.getInt("posicion");
            if(bPlaying) {
                mp.seekTo(iPos);
                mp.start();
            }

        }
    }

    boolean bPlaying=false;
    // Conexión con los estados de la aplicación

    // Aplicación en 2º plano=paused
    @Override
    public void onPause()
    {
        super.onPause();
        if(bPlaying)
            mp.pause();
    }

    // Aplicación vuelve a 1er plano=resumed

    @Override
    public void onResume(){

        super.onResume();

        if(bPlaying)
            mp.start();
    }

    // Metodos para guardar el estado de la activity

    String pathActual;
    @Override
    protected void onSaveInstanceState(Bundle estadoGuardado) {
        super.onSaveInstanceState(estadoGuardado);
        if(bPlaying)
        {
            int iPos=mp.getCurrentPosition();
            estadoGuardado.putInt("posicion",iPos);
            estadoGuardado.putString("path",pathActual);
        }
    }

    // TODO: enlazar con botones del layout
    // TODO: enlazar con los estados de reproducción (mp.start mp.stop mp.pause)
    // TODO: activar/desactivar (enable) según el estado de reproducción
    // TODO: gestionar el estado de la variable bPlaying
    Button btPlay;
    Button btPause;
    Button btStop;



    static final int CodigoSeleccionVideo=666;  // Código del diálogo de seleccion de vídeo
    static final int CodigoSeleccionAudio=667;// Código del diálogo de seleccion de audio
    // Lanza el dialogo seleccionador
    void seleccionarElmentoConDialogo()
    {
        Intent i=new Intent(Intent.ACTION_GET_CONTENT);
        getIntent().setType("video/*");
        getIntent().addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(Intent.createChooser(i,"Seleccion un video"),CodigoSeleccionVideo);
    }

    // Se llama cuando se termina la selección del contenido
    protected void onActivityResult(int codigo,int resultado,Intent datos)
    {
        switch(codigo)
        {
            case CodigoSeleccionVideo:
                if(resultado==RESULT_OK) {
                // TODO: reutilizar el código de obtejenerElementosReproducibles

                }

                break;

            case CodigoSeleccionAudio:
                break;

            default:
                break;
        }
    }

    // TODO: recibir el evento prepared (igual que en la aplicación de grabación)
    // Activa el botón play



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
            // TODO: guardar en pathActual el path del fichero
            try {
                mp.setDataSource(this,uriContenido);
                mp.prepare();
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
