package com.example.basicstorage

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private lateinit var guessInput: EditText
    private lateinit var submitGuessButton: Button
    private lateinit var guessGrid: GridLayout

    private val maxGuesses = 5
    private var currentGuessRow = 0
    private lateinit var randomNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        guessInput = findViewById(R.id.guessInput)
        submitGuessButton = findViewById(R.id.submitGuessButton)
        guessGrid = findViewById(R.id.guessGrid)

        // Generate a random 5-digit number to guess
        randomNumber = generateRandomNumber()

        // Set up the grid for the guesses
        setupGuessGrid()

        submitGuessButton.setOnClickListener {
            submitGuess()
        }
    }

    private fun generateRandomNumber(): String {
        return Random.nextInt(10000, 99999).toString()
    }

    private fun setupGuessGrid() {
        // Configure GridLayout parameters
        guessGrid.rowCount = maxGuesses
        guessGrid.columnCount = 5

        // Create TextViews for the grid
        for (row in 0 until maxGuesses) {
            for (col in 0 until 5) {
                val textView = TextView(this).apply {
                    text = " " // Initialize with a space
                    setBackgroundColor(Color.LTGRAY) // Default color
                    textSize = 24f
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 0
                        height = GridLayout.LayoutParams.WRAP_CONTENT
                        columnSpec = GridLayout.spec(col, 1f) // Make the column flexible
                        rowSpec = GridLayout.spec(row)
                        setMargins(4, 4, 4, 4) // Optional: add some margin
                    }
                    gravity = android.view.Gravity.CENTER // Center text in TextView
                }
                guessGrid.addView(textView)
            }
        }
    }

    private fun submitGuess() {
        val guess = guessInput.text.toString()

        // Check if guess is valid (5 digits)
        if (guess.length == 5 && guess.all { it.isDigit() }) {
            if (currentGuessRow < maxGuesses) {
                // Update the grid with the guess and change colors
                for (i in 0 until 5) {
                    val textView = guessGrid.getChildAt(currentGuessRow * 5 + i) as TextView
                    textView.text = guess[i].toString()

                    // Change color based on the guess
                    textView.setBackgroundColor(getColorForDigit(guess[i], i))
                }

                // Check if the guess is correct
                if (guess == randomNumber) {
                    Toast.makeText(this, "Congratulations! You guessed the number!", Toast.LENGTH_SHORT).show()
                } else if (currentGuessRow == maxGuesses - 1) {
                    Toast.makeText(this, "Maximum guesses reached! The correct number was $randomNumber.", Toast.LENGTH_LONG).show()
                }

                currentGuessRow++
                guessInput.text.clear() // Clear the input after submission
            } else {
                Toast.makeText(this, "Maximum guesses reached", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please enter a valid 5-digit number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getColorForDigit(digit: Char, index: Int): Int {
        return when {
            digit == randomNumber[index] -> Color.GREEN // Correct digit in the correct place
            randomNumber.contains(digit) -> Color.YELLOW // Correct digit but in the wrong place
            else -> Color.RED // Incorrect digit
        }
    }
}
