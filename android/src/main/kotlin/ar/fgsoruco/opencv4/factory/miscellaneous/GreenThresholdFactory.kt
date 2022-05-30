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
            minThresholdValue: Double,
            algorithm: String,
            result: MethodChannel.Result
        ) {
            when (pathType) {
                1 -> result.success(greenThresholdS(pathData, minThresholdValue, algorithm))
                2 -> result.success(greenThresholdB(data, minThresholdValue, algorithm))
                3 -> result.success(greenThresholdB(data, minThresholdValue, algorithm))
                4 -> result.success(greenThresholdB(data, minThresholdValue, algorithm))
            }
        }

        //Module: Miscellaneous Image Transformations
        private fun greenThresholdS(
            pathData: String,
            minThresholdValue: Double,
            algorithm: String
        ): ByteArray {
            var byteArray = ByteArray(0)

            val inputStream: InputStream = FileInputStream(pathData.replace("file://", ""))
            val data: ByteArray = inputStream.readBytes()

            try {
                val srcImage = Imgcodecs.imdecode(MatOfByte(*data), Imgcodecs.IMREAD_UNCHANGED)

                val thresholdImage = Mat()
                Imgproc.cvtColor(
                    srcImage,
                    thresholdImage,
                    if (algorithm == "hsv") Imgproc.COLOR_BGR2HSV else Imgproc.COLOR_BGR2HLS
                )

                val finalMask = Mat()

                Core.inRange(
                    thresholdImage,
                    Scalar(40.0, minThresholdValue, 100.0, 0.0),
                    Scalar(90.0, 255.0, 255.0, 255.0),
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
            data: ByteArray,
            minThresholdValue: Double,
            algorithm: String
        ): ByteArray {
            var byteArray = ByteArray(0)

            try {
                val srcImage = Imgcodecs.imdecode(MatOfByte(*data), Imgcodecs.IMREAD_UNCHANGED)

                val thresholdImage = Mat()
                Imgproc.cvtColor(
                    srcImage,
                    thresholdImage,
                    if (algorithm == "hsv") Imgproc.COLOR_BGR2HSV else Imgproc.COLOR_BGR2HLS
                )

                val finalMask = Mat()

                Core.inRange(
                    thresholdImage,
                    Scalar(40.0, minThresholdValue, 100.0, 0.0),
                    Scalar(90.0, 255.0, 255.0, 255.0),
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