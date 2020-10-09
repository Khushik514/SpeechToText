package com.example.speechtotext;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Environment;
import android.security.keystore.KeyGenParameterSpec;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int SPEAK_REQUEST = 10;
    EditText txt_value;
    ImageButton btn_voice_intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_value = (EditText) findViewById(R.id.txtValue);
        btn_voice_intent=(ImageButton) findViewById(R.id.btnVoiceIntent);
        btn_voice_intent.setOnClickListener(MainActivity.this);
        PackageManager packageManager = this.getPackageManager();
        List<ResolveInfo> ListOfInformation = packageManager.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0
        );
        if (ListOfInformation.size() > 0) {
            Toast.makeText(MainActivity.this, "Your device does support speech recognition!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Your device does not support speech recognition!", Toast.LENGTH_SHORT).show();
        }
    }
    private void ListenToTheUsersVoice() {
        Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Talk to me");
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        startActivityForResult(voiceIntent, SPEAK_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEAK_REQUEST && resultCode == RESULT_OK) {
            ArrayList<String> voiceWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            float[] confidLevels = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);
            int index = 0;
            for (String userWord : voiceWords) {
                if (confidLevels != null && index < confidLevels.length) {
                    txt_value.setText(userWord);

                }
            }

            File f = new File("a.txt");

            File dataa = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator);
            File file = new File(dataa, "speechtotext.txt");

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                try {
                    // highScore = Integer.parseInt(br.readLine());
                    // br.close();
                    System.out.println("DONE");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.toString());
                }
            } catch (FileNotFoundException e) {
                try {
                    file.createNewFile();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    System.out.println(e.toString());

                }
                e.printStackTrace();
                System.out.println(e.toString());

            }

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write(txt_value.getText().toString());
                System.out.println(txt_value.getText().toString());
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.toString());
            }

            try {
                FileWriter writer = new FileWriter(f);
                writer.append(txt_value.getText().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
                /*try {
                    FileWriter writer = new FileWriter("speech.txt");
                    //BufferedWriter bw=new BufferedWriter(writer);
                    writer.write(txt_value.getText().toString());
                } catch (IOException e) {

            }
            writeFileOnInternalStorage(txt_value.getContext(),"speech.txt",txt_value.getText().toString());

        }
    }
    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody){
        File file = new File(mcoContext.getFilesDir(),"mydir");
        if(!file.exists()){
            file.mkdir();
        }

        try{
            File gpxfile = new File(file, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();

        }catch (Exception e){
            e.printStackTrace();

        }
    }*/
        }
    }
    @Override
    public void onClick(View view) {
        ListenToTheUsersVoice();
    }
}


