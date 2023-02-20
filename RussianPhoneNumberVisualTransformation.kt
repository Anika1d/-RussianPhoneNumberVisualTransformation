class RussianPhoneNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        //mask +7 (   )    -  -
        val trimmed = if (text.text.length >= 10) text.text.substring(0..9) else text.text
        var output = "+7 "
        for (i in trimmed.indices) {
            if (i == 0) output += "(${trimmed[0]}"
            if (i in 1..2) output += trimmed[i]
            if (i == 2) output += ") "
            if (i in 3..5) output += trimmed[i]
            if (i == 5) output += "-"
            if (i in 6..7) output += trimmed[i]
            if (i == 7) output += "-"
            if (i in 8..9) output += trimmed[i]
        }


        val phoneNumberTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset == 0) return "+7 ".length
                if (offset <= 2) return offset + "+7 (".length
                if (offset == 3) return offset + "+7 ()".length
                if (offset <= 5) return offset + "+7 () ".length
                if (offset <= 7) return offset + "+7 () -".length
                if (offset <= 9) return offset + "+7 () --".length
                return 18
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 4) return 0
                if (offset <= 7) return offset - 4
                if (offset <= 11) return offset - 5
                if (offset <= 14) return offset - 6
                if (offset <= 17) return offset - 7
                return 10


            }

        }

        return TransformedText(
            AnnotatedString(output),
            phoneNumberTranslator
        )

    }

}
