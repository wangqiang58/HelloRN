import { Component, ReactNode } from "react";
import { AppRegistry, DeviceEventEmitter, Dimensions, StyleSheet } from "react-native";
import { ScrollView } from "react-native";
import { Alert } from "react-native";
import { TouchableOpacity } from "react-native";
import { Text, View } from "react-native";
import { NativeModules } from 'react-native';
import { storeData } from "./data";
import { NavigationContext } from "@react-navigation/native";

export default class MoreScreen extends Component {

    static contextType = NavigationContext;

    private deviceInfoModule = NativeModules.DeviceInfoModule;

    constructor(props:any) {
        super(props)
        this.state = {
            appName: "",
            appVersion: ""
        }
    }

    _logout = () => {
        this.deviceInfoModule.showToast('退出登录');
        storeData("is_login","0")
        //Actions.push("LoginScreen")
        DeviceEventEmitter.emit("send_logout","我是来自退出登录...")
        // this.getDevices()
    };

    _alert = () => {
        this.deviceInfoModule.alert('title', '我调用了原生的Alert方法');
    };

    getDevices = () => {
        this.print().then((msg)=>{
          console.log(`msg=${msg}`)
        })
        
        this.deviceInfoModule.getDeviceInfos((result: { appName: string; appVersion: string; }) => {
            // Alert.alert(`${result.appName}`)
            this.setState(
                {
                    appName: result.appName,
                    appVersion: result.appVersion
                }
            )
        }, (result: any) => {
            Alert.alert('error...')
            
        })
    }

    async print(){
      return new Promise((resolve,reject)=>{
        setTimeout(()=>{
            Alert.alert('xxxx1')
            resolve(2)
        },1000)
      })
    }

    

    render(): ReactNode {   
        return (<View style={{
            flex: 1,
            backgroundColor: '#E7F3FF',
            paddingLeft: 20, paddingRight: 20,
            justifyContent: 'flex-start',
            alignItems: 'center',
        }}>
            <View style={styles.header}>
                    <Text style={styles.headerTitle}>设置</Text>
            </View>
                
            <ScrollView style={[styles.scrollview]}>
                <Text style={styles.titleText}>清理缓存</Text>
                <View style={{ backgroundColor: '#EEEEEE', height: 1 }}></View>

                <Text style={styles.titleText}>用户注册协议</Text>
                <View style={{ backgroundColor: '#EEEEEE', height: 1 }}></View>

                <Text style={styles.titleText}>个人隐私协议</Text>
                <View style={{ backgroundColor: '#EEEEEE', height: 1 }}></View>

                <Text style={styles.titleText}>使用引导</Text>
                <View style={{ backgroundColor: '#EEEEEE', height: 1 }}></View>

                <Text style={styles.titleText}>版本更新</Text>
                <View style={{ backgroundColor: '#EEEEEE', height: 1 }}></View>

                <Text style={styles.titleText}>网络诊断</Text>
                <View style={{ backgroundColor: '#EEEEEE', height: 1 }}></View>

                <Text style={styles.titleText}>投诉</Text>
                <View style={{ backgroundColor: '#EEEEEE', height: 1 }}></View>

                <Text style={styles.titleText}>应用名称:{this.state.appName}</Text>
                <View style={{ backgroundColor: '#EEEEEE', height: 1 }}></View>

                <Text style={styles.titleText}>应用版本:{this.state.appVersion}</Text>
                <View style={{ backgroundColor: '#EEEEEE', height: 1 }}></View>


            </ScrollView>

            <TouchableOpacity onPress={this._logout} style={[styles.toastTou, { backgroundColor: 'yellow',marginBottom:10 }]}>
                <View style={{ backgroundColor: '#EEEEEE', height: 1 }}></View>
                <Text style={styles.titleText}>退出登录</Text>
            </TouchableOpacity>

        </View>)
    }
}

const { width } = Dimensions.get('window')

const styles = StyleSheet.create({
    header: {
        justifyContent: 'center',
        alignItems: 'center',
        borderBottomWidth: 1,
        borderBottomColor: '#EEEEEE',
    },
    headerTitle: {
        fontSize: 22,
        fontWeight: '500',
        color: '#333333',
        marginTop:40
    },

    scrollview: {
        backgroundColor: 'white',
        margin: 15,
        width: width - 40,
        borderRadius: 15,
    },

    center: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    toastTou: {
        width: 200,
        height: 40,
        borderRadius: 20,
        backgroundColor: 'green',
        alignItems: 'center',
        justifyContent: 'center',
        marginTop: 10
    },
    titleText: {
        fontSize: 14,
        color: 'gray',
        marginLeft: 10,
        marginTop: 10,
        marginBottom: 10
    },
});
