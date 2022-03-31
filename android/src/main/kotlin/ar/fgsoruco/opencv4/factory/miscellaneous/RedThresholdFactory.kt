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
            result: MethodChannel.Result
        ) {
            when (pathType) {
                1 -> result.success(redThresholdS(pathData))
                2 -> result.success(redThresholdB(data))
                3 -> result.success(redThresholdB(data))
                4 -> result.success(redThresholdB(data))
            }
        }

        //Module: Miscellaneous Image Transformations
        private fun redThresholdS(
            pathData: String
        ): ByteArray {
            var byteArray = ByteArray(0)

            val inputStream: InputStream = FileInputStream(pathData.replace("file://", ""))
            val data: ByteArray = inputStream.readBytes()

            try {
                val srcImage = Imgcodecs.imdecode(MatOfByte(*data), Imgcodecs.IMREAD_UNCHANGED)

                val bgrImage = Mat()
                Imgproc.cvtColor(srcImage, bgrImage, Imgproc.COLOR_YUV2BGR_I420)

                val hlsImage = Mat()
                Imgproc.cvtColor(bgrImage, hlsImage, Imgproc.COLOR_BGR2HLS)

                val mask1 = Mat()
                val mask2 = Mat()
                val finalMask = Mat()

                Core.inRange(hlsImage, Scalar(0.0, 100.0, 100.0), Scalar(20.0, 255.0, 255.0), mask1)
                Core.inRange(
                    hlsImage,
                    Scalar(160.0, 100.0, 100.0),
                    Scalar(180.0, 255.0, 255.0),
                    mask2
                )

                Core.add(mask1, mask2, finalMask)

                val matOfByte = MatOfByte()
                Imgcodecs.imencode(".png", finalMask, matOfByte)
                byteArray = matOfByte.toArray()
            } catch (e: java.lang.Exception) {
                println("OpenCV Error: $e")
            }

            return byteArray
        }

        //Module: Miscellaneous Image Transformations
        private fun redThresholdB(
            data: ByteArray
        ): ByteArray {
            var byteArray = ByteArray(0)

            try {
                val srcImage = Imgcodecs.imdecode(MatOfByte(*data), Imgcodecs.IMREAD_UNCHANGED)

                val bgrImage = Mat()
                Imgproc.cvtColor(srcImage, bgrImage, Imgproc.COLOR_YUV2BGR_I420)

                val hlsImage = Mat()
                Imgproc.cvtColor(bgrImage, hlsImage, Imgproc.COLOR_BGR2HLS)

                val mask1 = Mat()
                val mask2 = Mat()
                val finalMask = Mat()

                Core.inRange(hlsImage, Scalar(0.0, 100.0, 100.0), Scalar(20.0, 255.0, 255.0), mask1)
                Core.inRange(
                    hlsImage,
                    Scalar(160.0, 100.0, 100.0),
                    Scalar(180.0, 255.0, 255.0),
                    mask2
                )

                Core.add(mask1, mask2, finalMask)

                val matOfByte = MatOfByte()
                Imgcodecs.imencode(".png", finalMask, matOfByte)
                byteArray = matOfByte.toArray()
            } catch (e: java.lang.Exception) {
                println("OpenCV Error: $e")
            }

            return byteArray
        }
    }
}