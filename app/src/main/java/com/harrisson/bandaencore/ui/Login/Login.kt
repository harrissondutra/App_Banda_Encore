package com.harrisson.bandaencore.ui.Login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.harrisson.bandaencore.MainActivity
import com.harrisson.bandaencore.databinding.ActivityLoginBinding
import com.harrisson.bandaencore.ui.register.Register

class Login : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text_btn_google = binding.btnGoogle.getChildAt(0) as TextView
        text_btn_google.text = "Entrar com Google"


        binding.btnLogin.setOnClickListener {view ->
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                createSnackBarError(view, "Preencha todos os campos")
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { authentication ->
                        if (authentication.isSuccessful) {
                            createSnackBarLoginSuccesful(view, "Login realizado com sucesso", Color.rgb(13, 142, 9))
                            toHome()
                        }
                    }.addOnFailureListener { exception ->
                        val errorMessage = when (exception) {
                            is FirebaseAuthInvalidCredentialsException -> "Digite um email válido"
                            is FirebaseAuthUserCollisionException -> "Email já cadastrado"
                            is FirebaseNetworkException -> "Sem conexão com a internet"
                            else -> "Erro ao logar usuário"
                        }
                        createSnackBarError(view, errorMessage)
                    }

            }
        }
        binding.btnRegister.setOnClickListener(this)


    }
    private fun createSnackBarError(view: View, alerta: String, color: Int = Color.RED) {
        val snackbar = Snackbar.make(view, alerta, Snackbar.LENGTH_SHORT)
        val view: View = snackbar.getView()
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
        view.layoutParams = params
        snackbar.setBackgroundTint(color)
        snackbar.setTextColor(Color.WHITE)
        snackbar.show()
    }

    private fun createSnackBarLoginSuccesful(view: View, alerta: String, color: Int = Color.RED) {
        val snackbar =
            Snackbar.make(view, alerta, Snackbar.LENGTH_SHORT).setAction("Fazendo login") {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
        val view: View = snackbar.getView()
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
        view.layoutParams = params
        snackbar.setBackgroundTint(color)
        snackbar.setActionTextColor(Color.WHITE)
        snackbar.setTextColor(Color.WHITE)
        snackbar.show()
    }


    override fun onClick(v: View?) {
        if (binding.btnRegister == v) {
            val intent = Intent(this, Register::class.java)

            startActivity(intent)
        } else if (binding.btnLogin == v) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onStart() {
        super.onStart()

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            toHome()
        }
    }

    private fun toHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}