package ar.fgsoruco.opencv4.factory.miscellaneous

import io.flutter.plugin.common.MethodChannel
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.core.Scalar
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.FileInputStream
import java.io.InputStream

class GreenThresholdFactory {
    companion object {
        fun process(
            pathType: Int,
            pathData: String,
            data: ByteArray,
            result: MethodChannel.Result
        ) {
            when (pathType) {
                1 -> result.success(greenThresholdS(pathData))
                2 -> result.success(greenThresholdB(data))
                3 -> result.success(greenThresholdB(data))
                4 -> result.success(greenThresholdB(data))
            }
        }

        //Module: Miscellaneous Image Transformations
        private fun greenThresholdS(
            pathData: String
        ): ByteArray {
            var byteArray = ByteArray(0)

            val inputStream: InputStream = FileInputStream(pathData.replace("file://", ""))
            val data: ByteArray = inputStream.readBytes()

            try {
                val srcImage = Imgcodecs.imdecode(MatOfByte(*data), Imgcodecs.IMREAD_UNCHANGED)

                val hcvImage = Mat()
                Imgproc.cvtColor(srcImage, hcvImage, Imgproc.COLOR_BGR2HLS)

                val finalMask = Mat()

                Core.inRange(
                    hcvImage,
                    Scalar(40.0, 70.0, 100.0),
                    Scalar(90.0, 255.0, 255.0),
                    finalMask
                )

                val matOfByte = MatOfByte()
                Imgcodecs.imencode(".jpg", finalMask, matOfByte)
                byteArray = matOfByte.toArray()
            } catch (e: java.lang.Exception) {
                println("OpenCV Error: $e")
            }

            return byteArray
        }

        //Module: Miscellaneous Image Transformations
        private fun greenThresholdB(
            data: ByteArray
        ): ByteArray {
            var byteArray = ByteArray(0)

            try {
                val srcImage = Imgcodecs.imdecode(MatOfByte(*data), Imgcodecs.IMREAD_UNCHANGED)

                val hcvImage = Mat()
                Imgproc.cvtColor(srcImage, hcvImage, Imgproc.COLOR_BGR2HSV)

                val hcvImage = Mat()
                Imgproc.cvtColor(srcImage, hcvImage, Imgproc.COLOR_BGR2HLS)

                val finalMask = Mat()

                Core.inRange(
                    hcvImage,
                    Scalar(40.0, 70.0, 100.0),
                    Scalar(90.0, 255.0, 255.0),
                    finalMask
                )

                val matOfByte = MatOfByte()
                Imgcodecs.imencode(".jpg", finalMask, matOfByte)
                byteArray = matOfByte.toArray()
            } catch (e: java.lang.Exception) {
                println("OpenCV Error: $e")
            }

            return byteArray
        }
    }
}