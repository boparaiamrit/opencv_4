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
class ThresholdFactory {
  static const platform = const MethodChannel('opencv_4');

  static Future<Uint8List?> threshold({
    required CVPathFrom pathFrom,
    required String pathString,
    required Uint8List imageData,
    required double thresholdValue,
    required double maxThresholdValue,
    required int thresholdType,
  }) async {
    File _file;
    Uint8List _fileAssets;

    Uint8List? result;
    switch (pathFrom) {
      case CVPathFrom.GALLERY_CAMERA:
        result = await platform.invokeMethod('threshold', {
          "pathType": 1,
          "pathString": pathString,
          "data": Uint8List(0),
          'thresholdValue': thresholdValue,
          'maxThresholdValue': maxThresholdValue,
          'thresholdType': thresholdType
        });
        break;
      case CVPathFrom.URL:
        _file = await DefaultCacheManager().getSingleFile(pathString);
        result = await platform.invokeMethod('threshold', {
          "pathType": 2,
          "pathString": '',
          "data": await _file.readAsBytes(),
          'thresholdValue': thresholdValue,
          'maxThresholdValue': maxThresholdValue,
          'thresholdType': thresholdType
        });

        break;
      case CVPathFrom.ASSETS:
        _fileAssets = await Utils.imgAssets2Uint8List(pathString);
        result = await platform.invokeMethod('threshold', {
          "pathType": 3,
          "pathString": '',
          "data": _fileAssets,
          'thresholdValue': thresholdValue,
          'maxThresholdValue': maxThresholdValue,
          'thresholdType': thresholdType
        });
        break;
      case CVPathFrom.DATA:
        result = await platform.invokeMethod(
          'threshold',
          {
            "pathType": 4,
            "pathString": '',
            "data": imageData,
            'thresholdValue': thresholdValue,
            'maxThresholdValue': maxThresholdValue,
            'thresholdType': thresholdType
          },
        );
        break;
      default:
        _fileAssets = await Utils.imgAssets2Uint8List(pathString);
        result = await platform.invokeMethod('threshold', {
          "pathType": 3,
          "pathString": '',
          "data": _fileAssets,
          'thresholdValue': thresholdValue,
          'maxThresholdValue': maxThresholdValue,
          'thresholdType': thresholdType
        });
    }

    return result;
  }
}
