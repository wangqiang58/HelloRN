import { Component, ReactNode } from "react";
import { NativeModules } from "react-native";
import { TouchableOpacity } from "react-native";
import { Button, StyleSheet, Text, View, Alert, TextInput } from "react-native";



export default class LoginScreen extends Component {

    private deviceInfoModule = NativeModules.DeviceInfoModule;

    _toast = (msg: string) => {
        this.deviceInfoModule.showToast(msg);
    };


    private handleLogin = () => {
        // 这里可以添加登录逻辑，比如验证账号和密码
        if (this.state.username === '15210225563' && this.state.password === '123') {
            this.props.navigation.replace('AppScreen')
        } else {
            this._toast('登录失败')
        }
    };

    private handleTextPress = () => {
        this.setState({
            highlighted: this.state.username.length > 0 && this.state.password > 0
        })
    };

    constructor(props) {
        super(props)
        this.state = {
            username: '',
            password: '',
            highlighted: false
        }
    }

    render(): ReactNode {
        return (<View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', backgroundColor: '#CEE4FC' }}>

            <Text style={{ fontSize: 24, color: 'black' }}>一点一看</Text>
            <Text style={{ marginVertical: 10 }}>未注册手机登录验证后可完成注册</Text>
            <TextInput
                placeholder="请输入手机号"
                placeholderTextColor={'grey'}
                onChangeText={(ua) => {
                    this.setState({
                        username: ua
                    })
                    this.handleTextPress()
                }}
                style={{ paddingLeft: 20, borderWidth: 0, marginBottom: 10, width: 300, borderRadius: 40, backgroundColor: 'white' }}
            />
            <TextInput
                placeholder="输入短信验证码"
                placeholderTextColor={'grey'}
                onChangeText={(pwd) => {
                    this.setState({
                        password: pwd
                    })
                    this.handleTextPress()
                }}
                secureTextEntry
                style={{ paddingLeft: 20, borderWidth: 0, marginBottom: 10, width: 300, borderRadius: 40, backgroundColor: 'white' }}
            />
            <TouchableOpacity onPress={this.handleLogin}>
                <Text style={{ width: 200, backgroundColor: this.state.highlighted ? '#00CC66' : 'grey', color: 'white', height: 40, borderRadius: 20, alignContent: "center", justifyContent: 'center', textAlign: 'center', textAlignVertical: 'center' }}>登录</Text>
            </TouchableOpacity>

        </View>)

    }
}

const styles = StyleSheet.create({
    button: {
        flex: 1,
        width: '100%',
        backgroundColor: 'red'
    },
    highlighted: {
        backgroundColor: 'yellow',
    },
}
)