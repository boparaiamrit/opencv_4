package ar.fgsoruco.opencv4.factory.miscellaneous

import io.flutter.plugin.common.MethodChannel
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.FileInputStream
import java.io.InputStream

class HoughCirclesFactory {
    companion object {
        fun process(pathType: Int, pathData: String, data: ByteArray, dp: Double, minDist: Double, param1: Double, param2: Double, minRadius: Int, maxRadius: Int, result: MethodChannel.Result) {
            when (pathType) {
                1 -> result.success(thresholdS(pathData, dp, minDist, param1, param2, minRadius, maxRadius))
                2 -> result.success(thresholdB(data, dp, minDist, param1, param2, minRadius, maxRadius))
                3 -> result.success(thresholdB(data, dp, minDist, param1, param2, minRadius, maxRadius))
                4 -> result.success(thresholdB(data, dp, minDist, param1, param2, minRadius, maxRadius))
            }
        }

        //Module: Miscellaneous Image Transformations
        private fun thresholdS(pathData: String, dp: Double, minDist: Double, param1: Double, param2: Double, minRadius: Int, maxRadius: Int): ByteArray? {
            val inputStream: InputStream = FileInputStream(pathData.replace("file://", ""))
            val data: ByteArray = inputStream.readBytes()

            try {
                val byteArray: ByteArray

                val srcGray = Mat()
                val circles = Mat()
                // Decode image from input byte array
                val filename = pathData.replace("file://", "")
                val src = Imgcodecs.imread(filename)

//                // Convert the image to Gray
//                Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_BGR2GRAY)

                // HoughCircles
                Imgproc.HoughCircles(src, circles, Imgproc.HOUGH_GRADIENT, dp, minDist, param1, param2, minRadius, maxRadius)

                if (circles.cols() > 0) {
                    for (x in 1..circles.cols()) {
                        val circleVex = circles.get(0, x) ?: break
                        val point = Point(circleVex[0], circleVex[1])

                        Imgproc.circle(src, point, circleVex[2].toInt(), Scalar(255.0, 0.0, 0.0), 1)
                    }
                }

                // instantiating an empty MatOfByte class
                val matOfByte = MatOfByte()
                // Converting the Mat object to MatOfByte
                Imgcodecs.imencode(".jpg", src, matOfByte)
                byteArray = matOfByte.toArray()
                return byteArray
            } catch (e: java.lang.Exception) {
                println("OpenCV Error: $e")
                return data
            }

        }

        //Module: Miscellaneous Image Transformations
        private fun thresholdB(data: ByteArray, dp: Double, minDist: Double, param1: Double, param2: Double, minRadius: Int, maxRadius: Int): ByteArray? {
            try {
                val byteArray: ByteArray

                val srcGray = Mat()
                val circles = Mat()

                // Decode image from input byte array
                val src = Imgcodecs.imdecode(MatOfByte(*data), Imgcodecs.IMREAD_UNCHANGED)

//                // Convert the image to Gray
//                Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_BGR2GRAY)

                // HoughCircles
                Imgproc.HoughCircles(src, circles, Imgproc.HOUGH_GRADIENT, dp, minDist, param1, param2, minRadius, maxRadius)

                if (circles.cols() > 0) {
                    for (x in 1..circles.cols()) {
                        val circleVex = circles.get(0, x) ?: break

                        val point = Point(circleVex[0], circleVex[1])

                        Imgproc.circle(src, point, circleVex[2].toInt(), Scalar(255.0, 0.0, 0.0), 1)
                    }
                }

                // instantiating an empty MatOfByte class
                val matOfByte = MatOfByte()
                // Converting the Mat object to MatOfByte
                Imgcodecs.imencode(".jpg", src, matOfByte)
                byteArray = matOfByte.toArray()
                return byteArray
            } catch (e: java.lang.Exception) {
                println("OpenCV Error: $e")
                return data
            }
        }
    }
}