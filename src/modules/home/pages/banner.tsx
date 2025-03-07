import React, { Component, ReactNode } from "react";
import { Alert, TouchableOpacity } from "react-native";
import { StyleSheet,StatusBar } from "react-native";
import { Dimensions } from "react-native";
import { Image } from "react-native";
import { Text, View } from "react-native";
import Toast from "react-native-mix-toast";
import Duration from "react-native-mix-toast";

import Swiper from 'react-native-swiper';
import { getCatList } from "../../../api/AppApi";

let img1url = "https://cdn2.thecatapi.com/images/Yt4_Z0aDC.jpg";
let img2url = "https://cdn2.thecatapi.com/images/2mv.jpg";
let img3url = "https://cdn2.thecatapi.com/images/MTYzMDkyMg.jpg";
// 获取屏幕宽度
const { width } = Dimensions.get('window');

export default class BannerScreen extends Component {

    state = {
        bannerData: []
    }

    componentDidMount(): void {
        getCatList().then((data) => {
            console.log(`data=${data}`)
            this.setState({
                bannerData: data.slice(0, 5)
            })
        }).catch((error) => {
            console.warn(error)
        })
    }

    render(): ReactNode {
        const {bannerData} = this.state
    
        return (
            <View>
            <View style={[styles.container, styles.center]}>
                <View style={[styles.swiper_parent, styles.center]}>
                 {bannerData.length ?
                    <Swiper
                        autoplay={true}
                        autoplayTimeout={5}
                        dot={
                            <View
                                style={{
                                    backgroundColor: 'gray',
                                    width: 8,
                                    height: 8,
                                    borderRadius: 4,
                                    marginLeft: 3,
                                    marginRight: 3,
                                    marginTop: 3,
                                    marginBottom: 3
                                }}
                            />
                        }
                        activeDot={
                            <View
                                style={{
                                    backgroundColor: 'white',
                                    width: 16,
                                    height: 8,
                                    borderRadius: 4,
                                    marginLeft: 3,
                                    marginRight: 3,
                                    marginTop: 3,
                                    marginBottom: 3
                                }}
                            />
                        }
                    >
                        {this.state.bannerData.map((item, index) => {
                            return (<View key={index} style={[styles.slide, styles.center]}>
                                <Image style={styles.image} resizeMode="stretch" source={{ uri: item.url}} />
                            </View>)
                        })}
                    </Swiper> : null
    }
                </View>
            </View>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        height: 250,
        backgroundColor: '#FFFFFF'
    },
    swiper_parent: {
        width: width,
        height: 250
    },
    slide: {
        flex: 1,
        backgroundColor: 'gray',
    },
    center: {
        justifyContent: 'flex-start',
        alignItems: 'center'
    },
    image: {
        flex: 1,
        width: width,
        height:200
    }
});