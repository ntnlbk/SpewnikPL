package com.libbib.spewnikpl.domain

import javax.inject.Inject

class TransposeSongUseCase @Inject constructor() {
    operator fun invoke(songTextBeforeTranspose: String, transposeNumber: Int): String {
        var transposedString = ""
        val songTextBeforeTransposeLength = songTextBeforeTranspose.length
        var charNumber = 0
        while (charNumber < songTextBeforeTransposeLength) {
            var threeActualChars = ""
            var twoActualChars = ""
            if (charNumber + 2 < songTextBeforeTransposeLength)
                threeActualChars = songTextBeforeTranspose.substring(charNumber, charNumber + 3)
            if (charNumber + 1 < songTextBeforeTransposeLength)
                twoActualChars = songTextBeforeTranspose.substring(charNumber, charNumber + 2)
            val actualChar = songTextBeforeTranspose[charNumber].toString()
            when {
                majorChords7List.contains(threeActualChars) -> {
                    val charIndex = majorChords7List.indexOf(threeActualChars)
                    transposedString += majorChords7List[
                        calculateIndex(charIndex, transposeNumber)
                    ]
                    charNumber += 3
                }

                minorChords7List.contains(threeActualChars) -> {
                    val charIndex = minorChords7List.indexOf(threeActualChars)
                    transposedString += minorChords7List[
                        calculateIndex(charIndex, transposeNumber)
                    ]
                    charNumber += 3
                }

                majorChords7List.contains(twoActualChars) -> {
                    val charIndex = majorChords7List.indexOf(twoActualChars)
                    transposedString += majorChords7List[
                        calculateIndex(charIndex, transposeNumber)
                    ]
                    charNumber += 2
                }

                minorChords7List.contains(twoActualChars) -> {
                    val charIndex = minorChords7List.indexOf(twoActualChars)
                    transposedString += minorChords7List[
                        calculateIndex(charIndex, transposeNumber)
                    ]
                    charNumber += 2
                }

                majorChordsList.contains(twoActualChars) -> {
                    val charIndex = majorChordsList.indexOf(twoActualChars)
                    transposedString += majorChordsList[
                        calculateIndex(charIndex, transposeNumber)
                    ]
                    charNumber += 2
                }

                minorChordsList.contains(twoActualChars) -> {
                    val charIndex = minorChordsList.indexOf(twoActualChars)
                    transposedString += minorChordsList[
                        calculateIndex(charIndex, transposeNumber)
                    ]
                    charNumber += 2
                }

                majorChordsList.contains(actualChar) -> {
                    val charIndex = majorChordsList.indexOf(actualChar)
                    transposedString += majorChordsList[
                        calculateIndex(charIndex, transposeNumber)
                    ]
                    charNumber += 1
                }

                minorChordsList.contains(actualChar) -> {
                    val charIndex = minorChordsList.indexOf(actualChar)
                    transposedString += minorChordsList[
                        calculateIndex(charIndex, transposeNumber)
                    ]
                    charNumber += 1
                }

                else -> {
                    transposedString += actualChar
                    charNumber += 1
                }
            }
        }
        return transposedString
    }

    private fun calculateIndex(charIndex: Int, transposeNumber: Int) =
        if (charIndex + transposeNumber > 11)
            charIndex + transposeNumber - 12
        else if (charIndex + transposeNumber < 0)
            charIndex + transposeNumber + 12
        else
            charIndex + transposeNumber


    companion object {
        private val majorChordsList =
            listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "B", "H")
        private val minorChordsList =
            listOf("c", "c#", "d", "d#", "e", "f", "f#", "g", "g#", "a", "b", "h")
        private val majorChords7List =
            listOf("C7", "C#7", "D7", "D#7", "E7", "F7", "F#7", "G7", "G#7", "A7", "B7", "H7")
        private val minorChords7List =
            listOf("c7", "c#7", "d7", "d#7", "e7", "f7", "f#7", "g7", "g#7", "a7", "b7", "h7")
    }
}