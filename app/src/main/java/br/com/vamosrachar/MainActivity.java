package br.com.vamosrachar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity
        implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {

    EditText totalEditText;                     //Valor total a ser dividido
    TextView pessoasTextView;                   //Número de pessoas a rachar a conta
    TextView resultadoTextView;                 //Valor de cada parcela
    ImageButton maisButton;                     //Aumenta o número de pessoas
    ImageButton menosButton;                    //Diminui o número de pessoas
    FloatingActionButton compartilharFButton;   //Compartilha o resultado
    FloatingActionButton lerFButton;            //Lê o resultado
    TextToSpeech ttsPlayer;                     //Player

    int numPessoas = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalEditText = findViewById(R.id.totalEditText);
        pessoasTextView = findViewById(R.id.pessoasTextView);
        resultadoTextView = findViewById(R.id.resultadoTextView);
        maisButton = findViewById(R.id.maisButton);
        menosButton = findViewById(R.id.menosButton);
        compartilharFButton = findViewById(R.id.compartilharFButton);
        lerFButton = findViewById(R.id.lerFButton);

        totalEditText.addTextChangedListener(this);
        pessoasTextView.addTextChangedListener(this);
        maisButton.setOnClickListener(this);
        menosButton.setOnClickListener(this);
        compartilharFButton.setOnClickListener(this);
        lerFButton.setOnClickListener(this);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 1122);
    } //onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
            ttsPlayer = new TextToSpeech(this, this);
        } else {
            Intent instalTTSIntent = new Intent();
            instalTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(instalTTSIntent);
        }
    } //onActivityResult

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS)
            Toast.makeText(this, "TTS Ativado", Toast.LENGTH_LONG).show();
        else if (initStatus == TextToSpeech.ERROR)
            Toast.makeText(this, "Sem TTS habilitado", Toast.LENGTH_LONG).show();

    } //onInit

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        double total = 0;
        double parcela = 0;

        try {
            total = Double.parseDouble(totalEditText.getText().toString());
        } catch (Exception exception) {
            totalEditText.setText("0.00");
        }
        parcela = total / numPessoas;
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String resultado = decimalFormat.format(parcela);
        resultadoTextView.setText(resultado);
    }

    @Override
    public void onClick(View view) {
        if (view == maisButton) {
            numPessoas += 1;
            pessoasTextView.setText(String.valueOf(numPessoas));
        }
        if (view == menosButton) {
            numPessoas -= 1;
            if (numPessoas <= 2) {
                numPessoas = 2;
            }
            pessoasTextView.setText(String.valueOf(numPessoas));
        }
        if (view == compartilharFButton) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Ficou "
                    + resultadoTextView.getText().toString() + " reais para cada");
            startActivity(intent);
        }
        if (view == lerFButton) {
            if (ttsPlayer != null) {
                ttsPlayer.speak("Ficou " + resultadoTextView.getText().toString()
                        + " reais para cada", TextToSpeech.QUEUE_FLUSH,
                        null, "ID1");
            }
        }

    }
}