import React from "react";
import { AppRegistry } from "react-native";
import { bu_search } from "./app.json";
import SearchPage from './src/modules/search'
import { Router,Scene } from 'react-native-router-flux';

// 整个App 的骨架，基础包 更新要严格控制

const App = () => {
    return (<Router>
      <Scene key="root">
        <Scene key="SearchPage" component={SearchPage} hideNavBar={true}/>
      </Scene>
    </Router>)

};

AppRegistry.registerComponent("search", () => App);