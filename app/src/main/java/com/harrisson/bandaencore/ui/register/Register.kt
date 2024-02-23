package com.harrisson.bandaencore.ui.register

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.harrisson.bandaencore.databinding.ActivityRegisterBinding
import com.harrisson.bandaencore.ui.Login.Login


class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        auth = Firebase.auth
        binding.btnRegister.setOnClickListener { view ->

            var alerta: String = ""
            val i = Intent(Intent.ACTION_VIEW)

            var email = binding.editEmail.text.toString()
            var password = binding.editPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                alerta = "Preencha todos os campos"
                var color: Int = Color.RED
                createSnackBar(view, alerta, color)
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { register ->
                        if (register.isSuccessful) {
                            alerta = "Usuário criado com sucesso\nFaça login para continuar"
                            var color: Int = Color.rgb(13, 142, 9)
                            createSnackBarLoginSuccesful(view, alerta, color)
                            binding.editEmail.setText("")
                            binding.editPassword.setText("")

                        }
                    }.addOnFailureListener { exception ->
                        val errorMessage = when (exception) {
                            is FirebaseAuthWeakPasswordException -> "Digite uma senha com no mínimo 6 caracteres"
                            is FirebaseAuthInvalidCredentialsException -> "Digite um email válido"
                            is FirebaseAuthUserCollisionException -> "Email já cadastrado"
                            is FirebaseNetworkException -> "Sem conexão com a internet"
                            else -> "Erro ao cadastrar usuário"
                        }
                        createSnackBar(view, errorMessage, Color.RED)
                    }
            }
        }
    }

    private fun createSnackBarLoginSuccesful(view: View, alerta: String, color: Int = Color.RED) {
        val snackbar =
            Snackbar.make(view, alerta, Snackbar.LENGTH_INDEFINITE).setAction("Fazer login") {
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

    private fun createSnackBar(view: View, alerta: String, color: Int = Color.RED) {
        val snackbar = Snackbar.make(view, alerta, Snackbar.LENGTH_SHORT)
        val view: View = snackbar.getView()
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
        view.layoutParams = params
        snackbar.setBackgroundTint(color)
        snackbar.setActionTextColor(Color.WHITE)
        snackbar.setTextColor(Color.WHITE)
        snackbar.show()
    }


}