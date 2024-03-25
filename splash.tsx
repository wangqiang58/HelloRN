import React, { Component, ReactNode } from "react";
import { Dimensions } from "react-native";
import { StyleSheet } from "react-native";
import { Image, View } from "react-native";

export default class SplashScreen extends Component {

    goMain(){
        setTimeout(() => {
            this.props.navigation.replace('AppScreen')  
        }, 4000);
        
    }

    render(): ReactNode {
        this.goMain()
        return (<View style={styles.container}>
            <Image
                style={styles.image}
                source={require('./images/common_bg_oral_eva_large.png')}>
            </Image>
        </View>)
    }
}

const { width, height } = Dimensions.get('window');


const styles = StyleSheet.create({
    container: {
      flex: 1,
      justifyContent: 'center',
      alignItems: 'center',
    },
    image: {
      width: width,
      height: height,
      resizeMode: 'cover', // 设置图片填充模式为 cover，保持宽高比并覆盖整个容器
    },
  });
  