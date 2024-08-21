import { Component, ReactNode } from "react"
import { FlatList, Image, Platform } from "react-native";
import { Alert, Text, View, ActivityIndicator } from "react-native"
import constantData from '../../../mock/movices.json';
import { TouchableHighlight } from "react-native-gesture-handler";
import { NativeModules } from "react-native";
import { StatusBar } from "react-native";

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
            <StatusBar
              translucent={false}
              backgroundColor='white'
               barStyle='dark-content'
             />


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
