package ru.iu3.fclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.nio.charset.StandardCharsets;

import ru.iu3.fclient.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'fclient' library on application startup.
    static {
        System.loadLibrary("mbedcrypto");
        System.loadLibrary("fclient");

    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text = findViewById(R.id.sample_text);

        int res = initRng();
        text.setText("initRng = " + Integer.toString(res) + '\n');

        text.append("v = ");
        byte[] v = randomBytes(16);
        for(int i = 0; i < v.length; i++){
            if (i == 15){
                text.append(Integer.toString(v[i]) + '\n');
            } else{
                text.append(Integer.toString(v[i]) + "; ");
            }
        }

        text.append("Текст до шифрования: " + stringFromJNI() + '\n');
        byte[] byteStr = stringFromJNI().getBytes();
        byte[] encoded = encrypt(v, byteStr);
        String encodedStr = new String(encoded);
        text.append("Текст после шифрования: " + encodedStr + '\n');
        byte[] decoded = decrypt(v, encoded);
        String decodedStr = new String(decoded);
        text.append("Текст после дешифрования: " + decodedStr + '\n');
    }

    /**
     * A native method that is implemented by the 'fclient' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public static native int initRng();
    public static native byte[] randomBytes(int no);
    public static native byte[] encrypt(byte[] key, byte[] data);
    public static native byte[] decrypt(byte[] key, byte[] data);
}
