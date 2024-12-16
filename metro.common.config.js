const { hasBuildInfo, writeBuildInfo, clean } = require("./build");

function createModuleIdFactory() {
  const fileToIdMap = new Map();
  let nextId = 0;
  clean("./config/bundleCommonInfo.json");

  // 如果是业务 模块请以 10000000 来自增命名
  return (path) => {
    let id = fileToIdMap.get(path);

    if (typeof id !== "number") {
      id = nextId++;
      fileToIdMap.set(path, id);

      !hasBuildInfo("./config/bundleCommonInfo.json", path) &&
        writeBuildInfo(
          "./config/bundleCommonInfo.json",
          path,
          fileToIdMap.get(path)
        );
    }

    return id;
  };
}

const {getDefaultConfig, mergeConfig} = require('@react-native/metro-config');

/**
 * Metro configuration
 * https://facebook.github.io/metro/docs/configuration
 *
 * @type {import('metro-config').MetroConfig}
 */
const config = {
  serializer: {
    createModuleIdFactory: createModuleIdFactory, // 给 bundle 一个id 避免冲突 cli 源码中这个id 是从1 开始 自增的
  },
};

module.exports = mergeConfig(getDefaultConfig(__dirname), config);


// module.exports = {
//   serializer: {
//     createModuleIdFactory: createModuleIdFactory, // 给 bundle 一个id 避免冲突 cli 源码中这个id 是从1 开始 自增的
//   },
// };