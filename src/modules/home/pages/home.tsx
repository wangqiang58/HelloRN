import React, { Component } from 'react';
import {
  View,
  FlatList,
  StyleSheet,
  Text,
  TouchableOpacity,
  Alert,
} from 'react-native';
import { getMovices, getNewsList } from '../../../api/AppApi';
import { Actions } from 'react-native-router-flux';


interface ItemProps {
  title: string,
  content: string,
  pic?: string,
  callback: () => void
}

const Item = (props:any) => {
  return (
    <View style={styles.item}>
      <TouchableOpacity onPress={() => {
          props.callback()
      }
      }>
        <Text style={[{ color: 'black', fontSize: 16 }]}>{props.title}</Text>
        <Text style={[{ color: 'black', fontSize: 12 }]}>{props.description}</Text>
        <Text style={[{ color: 'black', fontSize: 10 }]}> {props.ctime}</Text>
      </TouchableOpacity>
    </View >
  );
}

export default class HomeScreen extends Component<ItemProps> {

  state = {
    newslist: null
  }

  componentDidMount(): void {
    getNewsList().then((response) => {
      this.setState({
        newslist: response.data.newslist
      })
    }).catch((error) => {
      console.warn(error)
    })
  }

  constructor(props: ItemProps) {
    super(props)
  }

  render(): React.ReactNode {
    
    const { newslist } = this.state

    return (
      <FlatList
        data={newslist}
        renderItem={(info) => {
          return <Item title={info.item.title}
           description={info.item.description}
           ctime = {info.item.ctime}
           callback={()=>{
            Actions.push('Details')
           }}></Item>
        }}
        keyExtractor={item => item.id}
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
});