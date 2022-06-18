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

class RedThresholdFactory {
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
                1 -> result.success(redThresholdS(pathData, minThresholdValue, algorithm))
                2 -> result.success(redThresholdB(data, minThresholdValue, algorithm))
                3 -> result.success(redThresholdB(data, minThresholdValue, algorithm))
                4 -> result.success(redThresholdB(data, minThresholdValue, algorithm))
            }
        }

        //Module: Miscellaneous Image Transformations
        private fun redThresholdS(
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

                var algorithmInt = Imgproc.COLOR_BGR2HSV
                if (algorithm != "hsv") algorithmInt = Imgproc.COLOR_BGR2HLS

                Imgproc.cvtColor(
                    srcImage,
                    thresholdImage,
                    algorithmInt
                )

                val mask1 = Mat()
                val mask2 = Mat()
                val finalMask = Mat()

                Core.inRange(
                    thresholdImage,
                    Scalar(0.0, minThresholdValue, 70.0),
                    Scalar(10.0, 255.0, 255.0),
                    mask1
                )
                Core.inRange(
                    thresholdImage,
                    Scalar(160.0, minThresholdValue, 70.0),
                    Scalar(180.0, 255.0, 255.0),
                    mask2
                )

                Core.add(mask1, mask2, finalMask)

                val matOfByte = MatOfByte()
                Imgcodecs.imencode(".jpg", finalMask, matOfByte)
                byteArray = matOfByte.toArray()
            } catch (e: java.lang.Exception) {
                println("OpenCV Error: $e")
            }

            return byteArray
        }

        //Module: Miscellaneous Image Transformations
        private fun redThresholdB(
            data: ByteArray,
            minThresholdValue: Double,
            algorithm: String
        ): ByteArray {
            var byteArray = ByteArray(0)

            try {
                val srcImage = Imgcodecs.imdecode(MatOfByte(*data), Imgcodecs.IMREAD_UNCHANGED)

                val thresholdImage = Mat()

                var algorithmInt = Imgproc.COLOR_BGR2HSV
                if (algorithm != "hsv") algorithmInt = Imgproc.COLOR_BGR2HLS

                Imgproc.cvtColor(
                    srcImage,
                    thresholdImage,
                    algorithmInt
                )

                val mask1 = Mat()
                val mask2 = Mat()
                val finalMask = Mat()

                Core.inRange(
                    thresholdImage,
                    Scalar(0.0, minThresholdValue, 70.0),
                    Scalar(10.0, 255.0, 255.0),
                    mask1
                )
                Core.inRange(
                    thresholdImage,
                    Scalar(160.0, minThresholdValue, 70.0),
                    Scalar(180.0, 255.0, 255.0),
                    mask2
                )

                Core.add(mask1, mask2, finalMask)

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