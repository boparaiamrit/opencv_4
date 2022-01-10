/* 
 * Copyright (c) 2021 fgsoruco.
 * See LICENSE for more details.
 */
import 'dart:io';
import 'dart:typed_data';

import 'package:flutter/services.dart';
import 'package:flutter_cache_manager/flutter_cache_manager.dart';
import 'package:opencv_4/factory/path_from.dart';
import 'package:opencv_4/factory/utils.dart';

///Class for process [HoughCircles]
class HoughCirclesFactory {
  static const platform = const MethodChannel('opencv_4');

  static Future<Uint8List?> houghCircles({
    required CVPathFrom pathFrom,
    required String pathString,
    required Uint8List imageData,
    required double dp,
    required double minDist,
    required double param1,
    required double param2,
    required int minRadius,
    required int maxRadius,
  }) async {
    File _file;
    Uint8List _fileAssets;

    Uint8List? result;
    switch (pathFrom) {
      case CVPathFrom.GALLERY_CAMERA:
        result = await platform.invokeMethod('houghCircles', {
          "pathType": 1,
          "pathString": pathString,
          "data": Uint8List(0),
          "dp": dp,
          "minDist": minDist,
          "param1": param1,
          "param2": param2,
          "minRadius": minRadius,
          "maxRadius": maxRadius,
        });
        break;
      case CVPathFrom.URL:
        _file = await DefaultCacheManager().getSingleFile(pathString);
        result = await platform.invokeMethod('houghCircles', {
          "pathType": 2,
          "pathString": '',
          "data": await _file.readAsBytes(),
          "dp": dp,
          "minDist": minDist,
          "param1": param1,
          "param2": param2,
          "minRadius": minRadius,
          "maxRadius": maxRadius,
        });

        break;
      case CVPathFrom.ASSETS:
        _fileAssets = await Utils.imgAssets2Uint8List(pathString);
        result = await platform.invokeMethod('houghCircles', {
          "pathType": 3,
          "pathString": '',
          "data": _fileAssets,
          "dp": dp,
          "minDist": minDist,
          "param1": param1,
          "param2": param2,
          "minRadius": minRadius,
          "maxRadius": maxRadius,
        });
        break;
      case CVPathFrom.DATA:
        result = await platform.invokeMethod(
          'houghCircles',
          {
            "pathType": 4,
            "pathString": '',
            "data": imageData,
            "dp": dp,
            "minDist": minDist,
            "param1": param1,
            "param2": param2,
            "minRadius": minRadius,
            "maxRadius": maxRadius,
          },
        );
        break;
      default:
        _fileAssets = await Utils.imgAssets2Uint8List(pathString);
        result = await platform.invokeMethod('houghCircles', {
          "pathType": 3,
          "pathString": '',
          "data": _fileAssets,
          "dp": dp,
          "minDist": minDist,
          "param1": param1,
          "param2": param2,
          "minRadius": minRadius,
          "maxRadius": maxRadius,
        });
    }

    return result;
  }
}
