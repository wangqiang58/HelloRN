import { Component, ReactNode } from "react"
import { FlatList, Image, Platform, StyleSheet, TouchableOpacity } from "react-native";
import { Alert, Text, View, ActivityIndicator } from "react-native"
import constantData from '../../../mock/movices.json';
import { TouchableHighlight } from "react-native-gesture-handler";
import { NativeModules } from "react-native";
import { StatusBar } from "react-native";
import { Actions } from "react-native-router-flux";

interface IDetailProps  {
    readonly title: string
}

export default class DetailPage extends Component<IDetailProps> {
    
    private deviceInfoModule = NativeModules.DeviceInfoModule;


    constructor(props: IDetailProps) {
        super(props)
    }


    state = {
        isLoading: false,
        responseData: null,
        jsonData: null
    }
    
    renderItem = ({item}:any) => {
        return <TouchableHighlight onPress={()=>{
            this.deviceInfoModule.startWebActivity(item['url'])
        }}>
            <View style={{ backgroundColor: 'white', flexDirection: 'row', width: '100%', marginVertical: 1, paddingVertical: 10, marginHorizontal: 5 }}>
                <Image style={{ height: 50, width: 50, borderRadius: Platform.OS == 'android' ? 10 :15 }} source={{ uri: item['thumbnail_pic_s'] }}></Image>
                <View style={{ marginLeft: 10 }}>
                    <Text style={{ color: 'black', alignItems: 'center' }}>
                        {item.title}
                    </Text>
                    <Text style={{ color: 'black' }}>作者:{item.author_name}</Text>
                    <Text style={{ color: 'grey' }}>时间:{item.date}</Text>
                </View>
            </View>
        </TouchableHighlight>


    };

    render(): React.ReactNode {
        const { isLoading } = this.state;

        return (
            <View style={{ flex: 1 }}>
                <View style={styles.header}>
                    <TouchableOpacity 
                        style={styles.backButton}
                        onPress={() => Actions.pop()}
                    >
                        <Text style={styles.backText}>{'‹'}</Text>
                    </TouchableOpacity>
                    <Text style={styles.headerTitle}>新闻详情</Text>
                </View>

                {isLoading && <ActivityIndicator size="large" color="#0000ff" />}
                <FlatList
                    data={constantData.result.data}
                    renderItem={this.renderItem}
                    keyExtractor={(item, index) => index.toString()}
                />
            </View>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#F5F5F5',
    },
    header: {
        paddingTop:40,
        paddingBottom:20,
        flexDirection: 'row',
        alignItems: 'center',
        backgroundColor: '#FFFFFF',
        borderBottomWidth: 1,
        borderBottomColor: '#EEEEEE',
    },
    backButton: {
        padding: 15,
        position: 'absolute',
        left: 0,
        zIndex: 1,
    },
    backText: {
        fontSize: 30,
        color: '#333333',
    },
    headerTitle: {
        flex: 1,
        fontSize: 18,
        fontWeight: '500',
        color: '#333333',
        textAlign: 'center',
    },

});
