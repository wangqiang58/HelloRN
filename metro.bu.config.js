const {getDefaultConfig, mergeConfig} = require('@react-native/metro-config');
const { hasBuildInfo, getCacheFile, isPwdFile ,writeBuildInfo } = require("./build");
const bundleBuInfo = require("./config/bundleBuInfo.json");

function postProcessModulesFilter(module) {
  if (
    module["path"].indexOf("__prelude__") >= 0 ||
    module["path"].indexOf("polyfills") >= 0
  ) {
    return false;
  }

  if (hasBuildInfo("./config/bundleCommonInfo.json", module.path)) {
    return false;
  }

  return true;
}

// 不要使用 string 会导致 bundle 体积陡增
function createModuleIdFactory() {
  // 如果是业务 模块请以 10000000 来自增命名
  const fileToIdMap = new Map();
  let nextId = 10000000;
  let isFirst = false;
  console.log('xxxxxx开始打包')

  return (path) => {
    if (Boolean(getCacheFile("./config/bundleCommonInfo.json", path))) {
      return getCacheFile("./config/bundleCommonInfo.json", path);
    }

    if (!isFirst && isPwdFile(path)) {
      nextId = bundleBuInfo[isPwdFile(path)];
      isFirst = true;
    }

    let id = fileToIdMap.get(path);
    if (typeof id !== "number") {
      id = nextId++;
      fileToIdMap.set(path, id);
    }

    console.log('path='+path+',id='+id)

    !hasBuildInfo("./config/bundleIndexInfo.json", path) &&
    writeBuildInfo(
      "./config/bundleIndexInfo.json",
      path,
      id
    );

    return id;
  };
}

/**
 * Metro configuration
 * https://facebook.github.io/metro/docs/configuration
 *
 * @type {import('metro-config').MetroConfig}
 */
const config = {
  serializer: {
    createModuleIdFactory: createModuleIdFactory, // 给 bundle 一个id 避免冲突 cli 源码中这个id 是从1 开始 自增的
    processModuleFilter: postProcessModulesFilter, // 返回false 就不会build 进去
  },
};

module.exports = mergeConfig(getDefaultConfig(__dirname), config);
