package com.example.memorina

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val openedCards = mutableListOf<ImageView>()
    private var matchedPairs = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layout = findViewById<LinearLayout>(R.id.mainLayout)
        val cardImages = List(16) { it / 2 + 1 }.shuffled()

        for (i in 0..3) {
            val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
            for (j in 0..3) {
                val card = ImageView(this).apply {
                    setImageResource(R.drawable.back)
                    layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    tag = cardImages[i * 4 + j]
                    setOnClickListener { onCardClick(this) }
                }
                row.addView(card)
            }
            layout.addView(row)
        }
    }

    private fun onCardClick(card: ImageView) {
        if (openedCards.size == 2 || openedCards.contains(card)) return
        card.setImageResource(getImageResource(card.tag as Int))
        openedCards.add(card)
        if (openedCards.size == 2) checkCards()
    }

    private fun checkCards() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            if (openedCards[0].tag == openedCards[1].tag) {
                openedCards.forEach { it.visibility = View.INVISIBLE }
                matchedPairs++
                if (matchedPairs == 8) {
                    Toast.makeText(this@MainActivity, "Вы выиграли!", Toast.LENGTH_LONG).show()
                }
            } else {
                openedCards.forEach { it.setImageResource(R.drawable.back) }
            }
            openedCards.clear()
        }
    }

    private fun getImageResource(tag: Int): Int {
        return resources.getIdentifier("card_$tag", "drawable", packageName)
    }
}