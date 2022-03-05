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

///Class for process [Threshold]
class GreenThresholdFactory {
  static const platform = const MethodChannel('opencv_4');

  static Future<Uint8List?> greenThreshold({
    required CVPathFrom pathFrom,
    required String pathString,
    required Uint8List imageData,
  }) async {
    File _file;
    Uint8List _fileAssets;

    Uint8List? result;
    switch (pathFrom) {
      case CVPathFrom.GALLERY_CAMERA:
        result = await platform.invokeMethod('greenThreshold', {
          "pathType": 1,
          "pathString": pathString,
          "data": Uint8List(0),
        });
        break;
      case CVPathFrom.URL:
        _file = await DefaultCacheManager().getSingleFile(pathString);
        result = await platform.invokeMethod('greenThreshold', {
          "pathType": 2,
          "pathString": '',
          "data": await _file.readAsBytes(),
        });

        break;
      case CVPathFrom.ASSETS:
        _fileAssets = await Utils.imgAssets2Uint8List(pathString);
        result = await platform.invokeMethod('greenThreshold', {
          "pathType": 3,
          "pathString": '',
          "data": _fileAssets,
        });
        break;
      case CVPathFrom.DATA:
        result = await platform.invokeMethod('greenThreshold', {
          "pathType": 4,
          "pathString": '',
          "data": imageData,
        });
        break;
      default:
        _fileAssets = await Utils.imgAssets2Uint8List(pathString);
        result = await platform.invokeMethod('greenThreshold', {
          "pathType": 3,
          "pathString": '',
          "data": _fileAssets,
        });
    }

    return result;
  }
}
