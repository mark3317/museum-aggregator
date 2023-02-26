package ru.iu3.fclient;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;

import ru.iu3.fclient.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'fclient' library on application startup.
    static {
        System.loadLibrary("mbedcrypto");
        System.loadLibrary("fclient");

    }

    private ActivityMainBinding binding;
    ActivityResultLauncher activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityResultLauncher  = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback() {
                    @Override
                    public void onActivityResult(Object res) {
                        if ( res instanceof ActivityResult) {
                            ActivityResult result = (ActivityResult) res;
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();
                                // обработка результата
                                String pin = data.getStringExtra("pin");
                                Toast.makeText(MainActivity.this, pin, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

//        TextView text = findViewById(R.id.sample_text);

//        int res = initRng();
//        text.setText("initRng = " + Integer.toString(res) + '\n');

//        text.append("v = ");
//        byte[] v = randomBytes(16);
//        for (int i = 0; i < v.length; i++) {
//            if (i == 15) {
//                text.append(Integer.toString(v[i]) + '\n');
//            } else {
//                text.append(Integer.toString(v[i]) + "; ");
//            }
//        }

//        text.append("Текст до шифрования: " + stringFromJNI() + '\n');
//        byte[] byteStr = stringFromJNI().getBytes();
//        byte[] encoded = encrypt(v, byteStr);
//        String encodedStr = new String(encoded);
//        text.append("Текст после шифрования: " + encodedStr + '\n');
//        byte[] decoded = decrypt(v, encoded);
//        String decodedStr = new String(decoded);
//        text.append("Текст после дешифрования: " + decodedStr + '\n');

    }

    public void onButtonClick(View v)
    {
        Intent it = new Intent(this, PinpadActivity.class);
        //startActivity(it);
        activityResultLauncher.launch(it);
    }

    public static byte[] stringToHex(String s) {
        byte[] hex;
        try {
            hex = Hex.decodeHex(s.toCharArray());
        } catch (DecoderException ex) {
            hex = null;
        }
        return hex;
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
