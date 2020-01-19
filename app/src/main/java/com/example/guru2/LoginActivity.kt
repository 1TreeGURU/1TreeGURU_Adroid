package com.example.guru2

import android.content.Context
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private val TAG : String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.edt_login_email)
        val password = findViewById<EditText>(R.id.edt_login_pw)

        //로그인
        btn_first_login.setOnClickListener {

            if (email.text.toString().length == 0 || password.text.toString().length == 0){
                Toast.makeText(this, "email 혹은 password를 반드시 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            Toast.makeText(baseContext, "로그인 성공",
                                Toast.LENGTH_SHORT).show()
                            val user = auth.currentUser
                            updateUI(user)

                            //메인 화면으로 이동
                            val nextIntent = Intent(this, MainActivity::class.java)
                            startActivity(nextIntent)

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }

                        // ...
                    }
            }

        }

        //회원가입 버튼
//        btn_main_signup.setOnClickListener {
//            val intent = Intent(this, SignUpActivity::class.java)
//            startActivity(intent)
//        }

    }

    override fun onResume() {
        super.onResume()
        val currentUser = auth?.currentUser
        updateUI(currentUser)

    }

    override fun onStart() {
        super.onStart()
        //앱 시작 단계에서 사용자가 현재 로그인 되어 있는지 확인하고 처리 해 준다.
        val currentUser = auth?.currentUser
        updateUI(currentUser) //이건 원하는대로 사용자 설정해 주는 부분인듯 하다.
    }

    fun updateUI(cUser : FirebaseUser? = null){
        if(cUser != null) {
            //로그인 버튼과 기타 등등을 사용할 수 없게 함(일괄 묶어서 처리 하는 방법?)
            btn_first_login.isEnabled = true
//            btn_main_signup.isEnabled = true
        } else {

        }
//        edt_main_email.setText("")
//        edt_main_pw.setText("")
        hideKeyboard(edt_login_email)
        //Toast.makeText(this, "유저: "+cUser.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboard(view: View) {
        view?.apply {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}


