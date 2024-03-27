import { Component, ReactNode, version } from "react";
import { Alert, View } from "react-native";
import BannerScreen from "../banner";
import HomeScreen from "./home";

interface IndexScreenProps {
    callback: () => void
}
export default class IndexScreen extends Component<IndexScreenProps>  {

    private handleClick = () => {
        this.props.callback()
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