import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import java.lang.Exception
import javax.swing.ImageIcon
import javax.swing.JFileChooser
import javax.swing.JFrame
import kotlin.math.min

const val chunkSize = 2048

class BQRCodeApp : JFrame()
{
    private var thisApp : BQRCodeApp? = null
    private var g : gui? = null
    private var bytes : ByteArray? = null
    private var ind: Int = 0

    init
    {
        thisApp = this
        g = gui()
        contentPane = g!!.panel1
        title = "BQRCodeApp2"
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(700, 700)
        isResizable = false
        setLocationRelativeTo(null)
        isVisible = true
        g!!.openbutton.addActionListener {
            val fc = JFileChooser()
            val returnVal = fc.showOpenDialog(thisApp)
            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                val file = fc.selectedFile
                ind = 0
                bytes = file.readBytes()
                setFileChunkToQRCode(ind)
            }
        }
        g!!.prevbutton.addActionListener {
            ind--
            if(ind<0)   ind=0
            setFileChunkToQRCode(ind)
        }
        g!!.nextbutton.addActionListener {
            ind++
            val maxind = bytes!!.size/chunkSize
            if(ind>maxind)   ind=maxind
            setFileChunkToQRCode(ind)
        }
    }
    private fun setFileChunkToQRCode(chunk: Int)
    {
        if(bytes == null) return
        try
        {

            val startInd = chunk * chunkSize
            val endInd = min((chunk + 1) * chunkSize, bytes!!.size)
            val chunkBytes = bytes!!.copyOfRange(startInd, endInd)

            val contents = base64Encode(chunkBytes)
            val writer = QRCodeWriter()

            val bitMatrix = writer.encode(contents, BarcodeFormat.QR_CODE, 600, 600)
            val image = MatrixToImageWriter.toBufferedImage(bitMatrix)
            val ic : ImageIcon = ImageIcon(image)

            g!!.label.icon = ic
            g!!.label.text = ""
        } catch (e: Exception) {
            println(e)
        }
    }

/* バイト配列をBASE64エンコードします。
            * @param bytes エンコード対象のバイト配列
 * @return エンコード後の文字列
 */
    private fun base64Encode(bytes: ByteArray): String
    {

        // バイト配列をビットパターンに変換します。
        val bitPattern = StringBuffer()
        for (i in bytes.indices)
        {
            var b = bytes[i].toInt()
            if (b < 0)
            {
                b += 256
            }
            var tmp = Integer.toBinaryString(b)
            while (tmp.length < 8)
            {
                tmp = "0$tmp"
            }
            bitPattern.append(tmp)
        }

        // ビットパターンのビット数が6の倍数にするため、末尾に0を追加します。
        while (bitPattern.length % 6 != 0)
        {
            bitPattern.append("0")
        }

        // 変換表
        val table = arrayOf(
            "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z", "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v",
            "w", "x", "y", "z", "0", "1", "2", "3",
            "4", "5", "6", "7", "8", "9", "+", "/"
        )

        // 変換表を利用して、ビットパターンを4ビットずつ文字に変換します。
        val encoded = StringBuffer()
        var i = 0
        while (i < bitPattern.length)
        {
            val tmp = bitPattern.substring(i, i + 6)
            val index = tmp.toInt(2)
            encoded.append(table[index])
            i += 6
        }

        // 変換後の文字数を4の倍数にするため、末尾に=を追加します。
        while (encoded.length % 4 != 0)
        {
            encoded.append("=")
        }
        return encoded.toString()
    }
}

fun main(args: Array<String>)
{
    val b = BQRCodeApp()
}
