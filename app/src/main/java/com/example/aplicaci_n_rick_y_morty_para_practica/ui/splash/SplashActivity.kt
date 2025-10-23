package com.example.aplicaci_n_rick_y_morty_para_practica.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.aplicaci_n_rick_y_morty_para_practica.R
import com.example.aplicaci_n_rick_y_morty_para_practica.databinding.ActivitySplashBinding
import com.example.aplicaci_n_rick_y_morty_para_practica.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        startTextAnimationSequence()
    }
    private fun startTextAnimationSequence() {
        // Lista de todos los TextViews en el orden en que deben aparecer
        val textViews = listOf(
            binding.tvCSplash,
            binding.tvASplash,
            binding.tvRSplash,
            binding.tvGSplash,
            binding.tvA2Splash,
            binding.tvNSplash,
            binding.tvDSplash,
            binding.tvOSplash,
            binding.tvPunto1Splash,
            binding.tvPunto2Splash,
            binding.tvPunto3Splash
        )

        // 3. Ocultar los TextViews inicialmente (opcional, pero recomendado)
        textViews.forEach { it.alpha = 0f }

        // 4. Lanzar la corrutina usando lifecycleScope
        lifecycleScope.launch {
            val staggerDelay = 100L // Retraso entre la aparición de cada letra (100 ms)
            val initialDelay = 500L // Retraso antes de que empiece la primera letra (500 ms)
            val duration = 400L    // Duración de la animación de cada letra (400 ms)

            delay(initialDelay)

            // 5. Iterar y animar cada TextView en secuencia
            for (textView in textViews) {
                // Animación: Traslación vertical (de abajo hacia arriba) y Opacidad (de 0 a 1)
                textView.apply {
                    translationY = 50f // Empieza 50px más abajo
                    animate()
                        .alpha(1f)     // Hazlo visible
                        .translationY(0f) // Mueve a su posición final (0 desplazamiento)
                        .setDuration(duration)
                        .start()
                }
                delay(staggerDelay) // Espera antes de animar la siguiente letra
            }

            // 6. Esperar un tiempo extra después de que todas las letras hayan aparecido
            delay(1250L) // Espera 1 segundo para que el usuario lea el texto completo

            // 7. Navegar a la siguiente Activity
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        // Creamos la intención para ir a la MainActivity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        // Finalizamos esta actividad para que el usuario no pueda volver con el botón 'Atrás'
        finish()
    }
}