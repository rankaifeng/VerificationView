package js.com.verificationview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.verification.library.VerificationCodeView;

public class MainActivity extends AppCompatActivity {
    private Button btnSub;
    private EditText editCode;
    private VerificationCodeView codeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSub = findViewById(R.id.btn_sub);
        editCode = findViewById(R.id.edit_code);
        codeView = findViewById(R.id.code_view);
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = codeView.checkResult(editCode.getText().toString());
                if (b) {
                    Toast.makeText(MainActivity.this, "正确", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "错误", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}

