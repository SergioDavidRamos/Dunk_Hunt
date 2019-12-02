package com.example.duckhunt.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duckhunt.R;
import com.example.duckhunt.common.Constantes;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class GameActivity extends AppCompatActivity {
    TextView tvCounterDuck, tvTimer, tvNick;
    ImageView ivDuck;
    int counter =0;
    int anchoPantalla;
    int altoPantalla;
    Random aleatorio;
    boolean gameOver = false;
    String id, nick;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        db = FirebaseFirestore.getInstance();

        initViewComponents();
        Eventos();
        initPantalla();
        moveDuck();
        initCuentaAtras();

    }

    private void initCuentaAtras() {
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                long segundosRestantes = millisUntilFinished/1000;
                tvTimer.setText(segundosRestantes+"s");
            }

            public void onFinish() {
                tvTimer.setText("0s");
                gameOver=true;
                mostrarDialogoGameOver();
                saveResultFirestore();
            }
        }.start();

    }

    private void  saveResultFirestore() {
        db.collection("users")
                .document(id)
                .update("ducks", counter);
    }

    private void mostrarDialogoGameOver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

        builder.setMessage("has conseguido cazar: "+counter+" patos").setTitle("Game over");
        builder.setPositiveButton("Reiniciar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                counter = 0;
                tvCounterDuck.setText("0");
                gameOver = false;
                initCuentaAtras();
                moveDuck();
            }
        });
        builder.setNegativeButton("Ver Ranking", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent i = new Intent(GameActivity.this, RankingActivity.class);
                startActivity(i);
            }
        });

        AlertDialog dialog = builder.create();

        // Mostrar el dialogo
        dialog.show();
    }

    private void initPantalla() {
        // 1. primero obtener el tamaño de la pantalla del dispositivo en el que estamos ejecutando la app
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        anchoPantalla = size.x;
        altoPantalla = size.y;
    }


    private void initViewComponents() {
        tvCounterDuck = findViewById(R.id.textViewCounter);
        tvTimer= findViewById(R.id.textViewTimer);
        tvNick= findViewById(R.id.textViewNick);
        ivDuck = findViewById(R.id.imageViewDuck);


        // Cambiar tipo de fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(), "pixel.ttf");
        tvCounterDuck.setTypeface(typeface);
        tvTimer.setTypeface(typeface);
        tvNick.setTypeface(typeface);

        // Extras: obtencion del nick del usuario y el setear correspondiente
        Bundle extras = getIntent().getExtras();
         nick = extras.getString(Constantes.EXTRA_NICK);
        tvNick.setText(nick);
        //2 segundo Inicializamos el objeto para generar numeros aleatorios
        id= extras.getString(Constantes.EXTRA_ID);
        aleatorio = new Random();
    }
    private void Eventos() {
        ivDuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!gameOver) {
                    counter++;
                    tvCounterDuck.setText(String.valueOf(counter));

                    ivDuck.setImageResource(R.drawable.duck_clicked);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ivDuck.setImageResource(R.drawable.duck);
                            moveDuck();
                        }
                    }, 500);
                }
            }
        });
    }

    private void moveDuck() {
        int min = 0;
        int maximoX = anchoPantalla - ivDuck.getWidth();
        int maximoY = altoPantalla - ivDuck.getWidth();

        // Generamos dos numeros aleatorios 1 para la conrdenada X o otro para la cordenada Y
        int randomX = aleatorio.nextInt(((maximoX-min)+1)+ min);
        int randomY = aleatorio.nextInt(((maximoY-min)+1)+ min);

        //Utilizamos los numeros aleatorios para mover el pato a esa posicion

        ivDuck.setX(randomX);
        ivDuck.setY(randomY);
    }
}
