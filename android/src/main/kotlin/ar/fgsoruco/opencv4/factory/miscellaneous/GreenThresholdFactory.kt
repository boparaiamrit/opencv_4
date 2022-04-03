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
            result: MethodChannel.Result
        ) {
            when (pathType) {
                1 -> result.success(greenThresholdS(pathData, minThresholdValue))
                2 -> result.success(greenThresholdB(data, minThresholdValue))
                3 -> result.success(greenThresholdB(data, minThresholdValue))
                4 -> result.success(greenThresholdB(data, minThresholdValue))
            }
        }

        //Module: Miscellaneous Image Transformations
        private fun greenThresholdS(
            pathData: String,
            minThresholdValue: Double
        ): ByteArray {
            var byteArray = ByteArray(0)

            val inputStream: InputStream = FileInputStream(pathData.replace("file://", ""))
            val data: ByteArray = inputStream.readBytes()

            try {
                val srcImage = Imgcodecs.imdecode(MatOfByte(*data), Imgcodecs.IMREAD_UNCHANGED)

                val hlsImage = Mat()
                Imgproc.cvtColor(srcImage, hlsImage, Imgproc.COLOR_BGR2HLS)

                val finalMask = Mat()

                Core.inRange(
                    hlsImage,
                    Scalar(40.0, minThresholdValue, 100.0),
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
            data: ByteArray,
            minThresholdValue: Double
        ): ByteArray {
            var byteArray = ByteArray(0)

            try {
                val srcImage = Imgcodecs.imdecode(MatOfByte(*data), Imgcodecs.IMREAD_UNCHANGED)

                val hlsImage = Mat()
                Imgproc.cvtColor(srcImage, hlsImage, Imgproc.COLOR_BGR2HLS)

                val finalMask = Mat()

                Core.inRange(
                    hlsImage,
                    Scalar(40.0, minThresholdValue, 100.0),
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