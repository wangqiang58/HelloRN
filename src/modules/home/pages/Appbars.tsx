/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React, { Component,createRef,RefObject } from 'react';

import ScrollableTabView, { DefaultTabBar } from 'react-native-scrollable-tab-view';
import HomeScreen from './homepage';
import MoreScreen from './more';
import VideoScreen from './video';
import {NativeModules, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
export default class AppSkechScreen extends Component {
  
  private tabView: RefObject<ScrollableTabView> = createRef();

  handleGoBack = () => {
    NativeModules.Navigator && NativeModules.Navigator.goBack();
  }

  render(): React.ReactNodaae {
    return(
      <View style={{ flex: 1 }}>
      <ScrollableTabView
      ref = {this.tabView}
      tabBarActiveTextColor="#28C35A"
      tabBarInactiveTextColor="#000000"
      tabBarBackgroundColor="#fff"
      tabBarPosition='bottom'
      initialPage={0}
      renderTabBar={() => <DefaultTabBar />}
      viewPagerRef={null}
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
    </ScrollableTabView>

    <TouchableOpacity 
          style={styles.backButton}
          onPress={this.handleGoBack}
          activeOpacity={0.7}
        >
          <Text style={styles.backButtonText}>{'‹'}</Text>
        </TouchableOpacity> 
    </View>
    );
  }
}

const styles = StyleSheet.create({
  backButton: {
    position: 'absolute',
    left: 15,
    top: 24, // iOS 状态栏高度，可根据需要调整
    zIndex: 999,
    backgroundColor: 'rgba(0, 0, 0, 0)',
    padding: 6,
    borderRadius: 4,
  },
  backButtonText: {
    fontSize: 30,
    color: '#fff',
  }
});