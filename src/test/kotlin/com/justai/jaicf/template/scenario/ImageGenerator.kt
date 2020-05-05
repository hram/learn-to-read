package com.justai.jaicf.template.scenario

import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class ImageGenerator(private val width: Int, private val height: Int) {

    private var img: BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

    private var g2d: Graphics2D

    private var currentFontSize = 120F

    private var font: Font

    init {
        g2d = img.createGraphics()
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY)
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE)
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)
        font = Font.createFonts(File("RobotoMono-Regular.ttf"))[0]
        font = font.deriveFont(Font.PLAIN, currentFontSize)
        g2d.font = font
    }

    fun drawString(text: String): String {
        var fontSize = currentFontSize
        when (text.length) {
            1 -> fontSize = 200F
            2, 3 -> fontSize = 180F
            4 -> fontSize = 160F
            5 -> fontSize = 120F
            6 -> fontSize = 100F
        }

        if (fontSize != currentFontSize) {
            font = font.deriveFont(Font.PLAIN, fontSize)
            g2d.font = font
            currentFontSize = fontSize
        }

        g2d.background = Color.WHITE
        g2d.clearRect(0, 0, width, height)
        val stringWidth = g2d.fontMetrics.stringWidth(text)
        val letterWidth = stringWidth / text.length
        var x = (width - g2d.fontMetrics.stringWidth(text)) / 2
        val y = (height - g2d.fontMetrics.height) / 2 + g2d.fontMetrics.ascent
        text.forEach {
            when (it) {
                'А', 'О', 'У', 'Э', 'И', 'Ы', 'Е', 'Ё', 'Я', 'Ю' -> {
                    g2d.color = Color.RED
                }
                else -> {
                    g2d.color = Color.BLUE
                }
            }
            g2d.drawString(it.toString(), x, y)
            x += letterWidth
        }

        val md5 = text.md5()
        val file = File("img/$md5.png")
        val parent = file.parentFile
        if (!parent.exists() && !parent.mkdirs()) {
            throw IllegalStateException()
        }

        ImageIO.write(img, "png", file)
        return md5
    }
}