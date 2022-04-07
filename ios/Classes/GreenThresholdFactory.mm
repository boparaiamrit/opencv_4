#import "GreenThresholdFactory.h"

FlutterStandardTypedData *greenThresholdB(FlutterStandardTypedData *data, double minThresholdValue);

@implementation GreenThresholdFactory

+ (void)processWhitPathType:(int)pathType pathString:(NSString *)pathString data:(FlutterStandardTypedData *)data minThresholdValue:(double)minThresholdValue result:(FlutterResult)result {

    switch (pathType) {
        case 1:
            break;
        case 2:
            result(greenThresholdB(data, minThresholdValue));
            break;
        case 3:
            result(greenThresholdB(data, minThresholdValue));
            break;
        case 4:
            result(greenThresholdB(data, minThresholdValue));
            break;

        default:
            break;
    }
}

FlutterStandardTypedData *greenThresholdB(FlutterStandardTypedData *data, double minThresholdValue) {
    CGColorSpaceRef colorSpace;
    std::vector<uint8_t> fileData;

    FlutterStandardTypedData *resultAdo;

    cv::Mat src;

    UInt8 *valor1 = (UInt8 *) data.data.bytes;

    int size = data.elementCount;

    CFDataRef fileDataRef = CFDataCreateWithBytesNoCopy(NULL, valor1, size, kCFAllocatorNull);

    CGDataProviderRef imageProvider = CGDataProviderCreateWithCFData(fileDataRef);

    CGImageRef image = nullptr;

    image = CGImageCreateWithJPEGDataProvider(imageProvider, NULL, true, kCGRenderingIntentDefault);

    colorSpace = CGImageGetColorSpace(image);
    CGFloat cols = CGImageGetWidth(image);
    CGFloat rows = CGImageGetHeight(image);

    src = cv::Mat(static_cast<int>(rows), static_cast<int>(cols), CV_8UC4);
    CGContextRef contextRef = CGBitmapContextCreate(src.data,
            static_cast<size_t>(cols),
            static_cast<size_t>(rows),
            8,
            src.step[0],
            colorSpace,
            kCGImageAlphaNoneSkipLast | kCGBitmapByteOrderDefault);
    CGContextDrawImage(contextRef, CGRectMake(0, 0, cols, rows), image);
    CGContextRelease(contextRef);
    CFRelease(image);
    CFRelease(imageProvider);
    CFRelease(fileDataRef);

    if (src.empty()) {
        resultAdo = [FlutterStandardTypedData typedDataWithBytes:data.data];
    } else {
        cv::Mat bgrImage;
        cv::Mat hlsImage;

        cv::cvtColor(src, bgrImage, cv::COLOR_BGRA2BGR);
        cv::cvtColor(bgrImage, hlsImage, cv::COLOR_BGR2HLS);

        cv::Mat dst;

        cv::inRange(hlsImage, cv::Scalar(40, minThresholdValue, 100), cv::Scalar(90, 255, 255), dst);


        NSData *data2 = [NSData dataWithBytes:dst.data length:dst.elemSize() * dst.total()];

        if (dst.elemSize() == 1) {
            colorSpace = CGColorSpaceCreateDeviceGray();
        } else {
            colorSpace = CGColorSpaceCreateDeviceRGB();
        }

        CGDataProviderRef provider = CGDataProviderCreateWithCFData((__bridge CFDataRef) data2);

        CGImageRef imageRef = CGImageCreate(static_cast<size_t>(dst.cols),
                static_cast<size_t>(dst.rows),
                8,
                8 * dst.elemSize(),
                dst.step[0],
                colorSpace,
                kCGImageAlphaNone | kCGBitmapByteOrderDefault,
                provider,
                NULL,
                false,
                kCGRenderingIntentDefault
        );

        // Getting UIImage from CGImage
        UIImage *finalImage = [UIImage imageWithCGImage:imageRef];
        CGImageRelease(imageRef);
        CGDataProviderRelease(provider);
        CGColorSpaceRelease(colorSpace);

        NSData *imgConvert;

        imgConvert = UIImageJPEGRepresentation(finalImage, 1);

        resultAdo = [FlutterStandardTypedData typedDataWithBytes:imgConvert];
    }

    return resultAdo;
}


@end

