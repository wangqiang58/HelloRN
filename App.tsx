/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React, { Component, useState } from 'react';
import {
  Image,
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  TextInput,
  useColorScheme,
  View,
} from 'react-native';

import ScrollableTabView, { ScrollableTabBar, DefaultTabBar } from 'react-native-scrollable-tab-view';
import IndexScreen from './homepage';


export default class AppScreen extends Component {

  private handleCallback = () => {
    this.props.navigation.navigate("Details",{"title":'haha'})
  };

  render(): React.ReactNode {
    return <ScrollableTabView
      tabBarActiveTextColor="#28C35A"
      tabBarInactiveTextColor="#000000"
      tabBarBackgroundColor="#fff"
      tabBarPosition='bottom'
      initialPage={0}

      renderTabBar={() => <DefaultTabBar />}
    >
      <View tabLabel='首页' style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
        <IndexScreen callback={()=>{
           this.props.navigation.navigate("Details",{"title":'haha'})
        }} />
      </View>
      <View tabLabel='活动' style={{ flex: 1, justifyContent: 'center', alignItems: 'center', backgroundColor: 'red' }}>
        <Text>Tab 2 content2</Text>
      </View>
      <View
        tabLabel='个人中心'
        style={{ flex: 1, justifyContent: 'center', alignItems: 'center', backgroundColor: 'green' }}>
        <Text>Tab 3 content3</Text>
      </View>
    </ScrollableTabView>;
  }
}

var style = StyleSheet.create({

})