import { Component, ReactNode } from "react";
import { StyleSheet } from "react-native";
import { Alert } from "react-native";
import { TouchableOpacity } from "react-native";
import { Text, View } from "react-native";
import { NativeModules } from 'react-native';



export default class MoreScreen extends Component {

    private deviceInfoModule = NativeModules.DeviceInfoModule;

    constructor(props: {} | Readonly<{}>) {
        super(props)
        this.state = {
            appName: "",
            appVersion: ""
        }
    }

    _toast = () => {
        this.deviceInfoModule.showToast('我调用了原生的toast方法');
    };
    _alert = () => {
        this.deviceInfoModule.alert('title', '我调用了原生的Alert方法');
    };

    getDevices = () => {
        this.deviceInfoModule.getDeviceInfos((result: { appName: string; appVersion: string; }) => {
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

    render(): ReactNode {

        this.getDevices()
        return (<View>
            <Text style={styles.titleText}>应用名称:{this.state.appName}</Text>
            <Text style={styles.titleText}>应用版本:{this.state.appVersion}</Text>

            <TouchableOpacity onPress={this._toast} style={styles.toastTou}>
                <Text style={styles.titleText}>调用原生的toast</Text>
            </TouchableOpacity>

        </View>)
    }
}


const styles = StyleSheet.create({
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
    },
    titleText: {
        fontSize: 14,
        color: '#fff',
        marginBottom:0
    },
});
