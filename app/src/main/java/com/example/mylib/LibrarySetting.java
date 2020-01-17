package com.example.mylib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LibrarySetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_setting);


//       내 서재 설정 저장 버튼 리스너
        Button btn_lib_save = (Button)findViewById(R.id.btn_lib_save);
        btn_lib_save.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), LibraryMain.class);
                        startActivity(intent);
                    }
                }
        );
    }
}


//commit test 이제 커밋 해볼게