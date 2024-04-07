import { Component, ReactNode, version } from "react";
import { Alert, View } from "react-native";
import BannerScreen from "./banner";
import HomeScreen from "./home";
import { DeviceEventEmitter } from 'react-native';
import { StatusBar } from "react-native";


interface IndexScreenProps {
    callback: () => void
}
export default class IndexScreen extends Component<IndexScreenProps>  {

    handleEvent = (message) => {
        // 处理接收到的消息
        console.warn('Received message from native:', message);
      }

    private handleClick = () => {
        this.props.callback()
    }

    componentDidMount() {
        DeviceEventEmitter.addListener('eventName', this.handleEvent);
      }
      
      componentWillUnmount() {
        DeviceEventEmitter.removeListener('eventName', this.handleEvent);
      }

    render(): ReactNode {
        return (
            <View style={{
                flexDirection: "column",
                flex: 1
            }}>
  
                <BannerScreen>
                </BannerScreen>
                <HomeScreen title="首页2" content="首页内容" callback= {this.handleClick}>
                </HomeScreen>
            </View>

        )
    }
}