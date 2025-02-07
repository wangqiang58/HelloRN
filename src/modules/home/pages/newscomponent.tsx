import React, { Component } from 'react';
import {
  View,
  FlatList,
  StyleSheet,
  Text,
  TouchableOpacity,
  Alert,
  ActivityIndicator,
} from 'react-native';
import { getMovices, getNewsList } from '../../../api/AppApi';
import { Actions } from 'react-native-router-flux';
import { loadConfig } from 'metro-config';

interface NewsItem {
  id: string;
  title: string;
  description: string;
  ctime: string;
}

interface ItemProps {
  title: string,
  content: string,
  pic?: string,
  navigation: any // For quick fix. For better typing use proper NavigationScreenProp type
}

const Item = (props:any) => {
  return (
    <View style={styles.item}>
      <TouchableOpacity onPress={
          props.callback
      }>
        <Text style={[{ color: 'black', fontSize: 16 }]}>{props.title}</Text>
        <Text style={[{ color: 'black', fontSize: 12 }]}>{props.description}</Text>
        <Text style={[{ color: 'black', fontSize: 10 }]}> {props.ctime}</Text>
      </TouchableOpacity>
    </View >
  );
}

export default class NewsScreen extends Component<ItemProps> {

  state = {
    newslist: [] as NewsItem[],
    loading:true,
    page:1
  }

  fetchData = ()=>{
    getNewsList(this.state.page).then((response) => {
      this.setState({
        newslist:[...this.state.newslist,...response.data.newslist],
        loading: false
      })
    }).catch((error) => {
      this.setState({loading: false})
      console.warn(error)
    })
  }

  componentDidMount(): void {
    this.fetchData()
  }

  constructor(props: ItemProps) {
    super(props)
  }

  handleRefresh =()=>{
    this.setState({newslist:[],page:1,loading:true})
    this.fetchData()
  }

  handleLoadMore = () => {
    if (!this.state.loading) {
      this.setState({page:this.state.page+1,loading:true})
      this.fetchData()
    }
  };

  render(): React.ReactNode {
    return (
      <FlatList
        data={this.state.newslist}
        renderItem={(info) => {
          return <Item title={info.item.title}
           description={info.item.description}
           ctime = {info.item.ctime}
           callback={()=>{
            this.props.navigation.navigate('DetailPage')
           }}></Item>
        }}
        keyExtractor={(item,index) => `${item.id}-${index}`}
        onEndReached={this.handleLoadMore}
        onEndReachedThreshold={0.1}
        refreshing={this.state.loading}
        onRefresh={this.handleRefresh}      
        ListFooterComponent={()=>{
          return this.state.loading ? (
            <View style={[styles.footer, {height: 100}]}>
              <ActivityIndicator size="small" color="#0000ff" />
              <Text style={{marginTop: 10,color:'black'}}>加载中...</Text>
            </View>
          ) : null;
        }}
      />
    )
  }
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  item: {
    backgroundColor: '#f9c2ff',
    padding: 20,
    borderRadius: 10,
    marginVertical: 8,
    marginHorizontal: 16,
  },
  title: {
    fontSize: 32,
  },
  footer: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 20,
  }
});