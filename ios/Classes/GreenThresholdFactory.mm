//
//
//  Created by fgsoruco.
//

#import "GreenThresholdFactory.h"

@implementation GreenThresholdFactory

+ (void)processWhitPathType:(int)pathType pathString:(NSString *)pathString data:(FlutterStandardTypedData *)data minThresholdValue: (double)minThresholdValue algorithm: (NSString *)algorithm result: (FlutterResult) result{
    
    result(greenThresholdB(data, minThresholdValue, algorithm));
}

FlutterStandardTypedData * greenThresholdB(FlutterStandardTypedData * data, double minThresholdValue, NSString* algorithm) {
    CGColorSpaceRef colorSpace;
    const char * suffix;
    std::vector<uint8_t> fileData;
    FlutterStandardTypedData* resultado;
    cv::Mat src;
    
    
    UInt8* valor1 = (UInt8*) data.data.bytes;
    
    int size = data.elementCount;
    
    
    CFDataRef file_data_ref = CFDataCreateWithBytesNoCopy(NULL, valor1,
                                                          size,
                                                          kCFAllocatorNull);
    
    CGDataProviderRef image_provider = CGDataProviderCreateWithCFData(file_data_ref);
    
    CGImageRef image = nullptr;
    
    
    image = CGImageCreateWithJPEGDataProvider(image_provider, NULL, true,
                                              kCGRenderingIntentDefault);
    suffix = (char*)".jpg";
    
    colorSpace = CGImageGetColorSpace(image);
    CGFloat cols = CGImageGetWidth(image);
    CGFloat rows = CGImageGetHeight(image);
    
    src = cv::Mat(rows, cols, CV_8UC4); // 8 bits per component, 4 channels (color channels + alpha)
    CGContextRef contextRef = CGBitmapContextCreate(src.data,                 // Pointer to  data
                                                    cols,                       // Width of bitmap
                                                    rows,                       // Height of bitmap
                                                    8,                          // Bits per component
                                                    src.step[0],              // Bytes per row
                                                    colorSpace,                 // Colorspace
                                                    kCGImageAlphaNoneSkipLast |
                                                    kCGBitmapByteOrderDefault); // Bitmap info flags
    CGContextDrawImage(contextRef, CGRectMake(0, 0, cols, rows), image);
    CGContextRelease(contextRef);
    CFRelease(image);
    CFRelease(image_provider);
    CFRelease(file_data_ref);


    cv::Mat hlsImage;

    cv::cvtColor(src, hlsImage, [algorithm  isEqual: @"hsv"] ? cv::COLOR_RGB2HSV : cv::COLOR_RGB2HLS);

    cv::Mat dst;

    cv::inRange(hlsImage, cv::Scalar(30, minThresholdValue, 100, 0), cv::Scalar(90, 255, 255, 255), dst);
    
    NSData *data2 = [NSData dataWithBytes:dst.data length:dst.elemSize()*dst.total()];
    
    if (dst.elemSize() == 1) {
        colorSpace = CGColorSpaceCreateDeviceGray();
    } else {
        colorSpace = CGColorSpaceCreateDeviceRGB();
    }
    
    CGDataProviderRef provider = CGDataProviderCreateWithCFData((__bridge CFDataRef)data2);
    // Creating CGImage from cv::Mat
    CGImageRef imageRef = CGImageCreate(dst.cols,                                 //width
                                        dst.rows,                                 //height
                                        8,                                          //bits per component
                                        8 * dst.elemSize(),                       //bits per pixel
                                        dst.step[0],                            //bytesPerRow
                                        colorSpace,                                 //colorspace
                                        kCGImageAlphaNone|kCGBitmapByteOrderDefault,// bitmap info
                                        provider,                                   //CGDataProviderRef
                                        NULL,                                       //decode
                                        false,                                      //should interpolate
                                        kCGRenderingIntentDefault                   //intent
                                        );
    
    // Getting UIImage from CGImage
    UIImage *finalImage = [UIImage imageWithCGImage:imageRef];
    CGImageRelease(imageRef);
    CGDataProviderRelease(provider);
    CGColorSpaceRelease(colorSpace);
    
    NSData* imgConvert;
    
    imgConvert = UIImageJPEGRepresentation(finalImage, 1);
    
    
    resultado = [FlutterStandardTypedData typedDataWithBytes: imgConvert];
    
    
    return resultado;
}


@end


