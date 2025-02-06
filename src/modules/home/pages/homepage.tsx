import { Component, ReactNode } from "react";
import { View } from "react-native";
import BannerScreen from "./banner";
import NewsScreen from "./newscomponent";
import { DeviceEventEmitter } from 'react-native';

export default class HomeScreen extends Component  {

    handleEvent = (message:string) => {
        // 处理接收到的消息
        console.warn('Received message from native:', message);
      }

    componentDidMount() {
        DeviceEventEmitter.addListener('send_logout', this.handleEvent);
      }
      
      componentWillUnmount() {
        DeviceEventEmitter.removeAllListeners
      }

    render(): ReactNode {
        return (
            <View style={{
                flexDirection: "column",
                flex: 1
            }}>
                <BannerScreen>
                </BannerScreen>
                <NewsScreen title="首页2" content="首页内容">
                </NewsScreen>
            </View>

        )
    }
}