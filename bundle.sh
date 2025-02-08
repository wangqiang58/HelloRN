# 基础bundle
yarn react-native bundle --platform android --dev false --entry-file ./common.js --bundle-output ./bundle/android/common.android.bundle --assets-dest ./bundle/android/res --config ./metro.common.config.js --reset-cache

#业务bundle
yarn react-native bundle --platform android --dev false --entry-file ./index.js --bundle-output ./bundle/android/index/index.android.bundle --assets-dest ./bundle/android/index/res --config ./metro.bu.config.js --reset-cache