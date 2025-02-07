/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React, { Component } from 'react';

import ScrollableTabView, { DefaultTabBar } from 'react-native-scrollable-tab-view';
import HomeScreen from './homepage';
import MoreScreen from './more';
import VideoScreen from './video';
export default class AppSkechScreen extends Component {

  render(): React.ReactNode {
    
    return <ScrollableTabView
      tabBarActiveTextColor="#28C35A"
      tabBarInactiveTextColor="#000000"
      tabBarBackgroundColor="#fff"
      tabBarPosition='bottom'
      initialPage={0}
      renderTabBar={() => <DefaultTabBar />}
    >
      <HomeScreen 
        tabLabel='首页'
        navigation={this.props.navigation}
      />
      <VideoScreen
         tabLabel='视频'
      />
      <MoreScreen
        tabLabel='个人中心'
      />
    </ScrollableTabView>;
  }
}