import { Component, ReactNode } from "react"
import { FlatList } from "react-native";
import { Alert, Text, View, ActivityIndicator } from "react-native"

interface IDetailProps {
    title: string
}



export default class DetailPage extends Component<IDetailProps> {

    constructor(props: IDetailProps) {
        super(props)
    }

    state = {
        isLoading: false,
        responseData: null
    }

    renderItem = ({ item}) => {
        return <Text style={{ height: 40, backgroundColor: 'red', marginVertical: 10, alignItems: 'center', alignContent: 'center', textAlign: 'center' }}>
            {item.title}
        </Text>;
    };

    render(): React.ReactNode {
        const { isLoading, responseData } = this.state;
        const { title } = this.props


        return (
            <View style={{ flex: 1 }}>
                {isLoading && <ActivityIndicator size="large" color="#0000ff" />}
                <Text>{title}</Text>
                <FlatList
                    data={responseData}
                    renderItem={this.renderItem}
                    keyExtractor={(item, index) => index.toString()}
                />
            </View>
        )
    }

    componentDidMount(): void {
        this.getMoviesFromApiAsync()
    }

    getMoviesFromApiAsync() {
        this.setState({ isLoading: true })
        fetch('https://facebook.github.io/react-native/movies.json')
            .then((response) => response.json())
            .then((data) => {
                this.setState({ responseData: data.movies })
            })
            .catch((error) => {
                console.error(error);
                Alert.alert(error)
            }).finally(() => {
                this.setState({ isLoading: false })
            })

    };
}
