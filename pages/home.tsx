import React, { Component } from 'react';
import {
  SafeAreaView,
  View,
  FlatList,
  StyleSheet,
  Text,
  StatusBar,
  TouchableOpacity,
  Alert,
} from 'react-native';
import { NavigationScreenProp } from 'react-navigation';


const data = [
  {
    id: '1',
    title: '完全跨平台2',
  },
  {
    id: '2',
    title: '支持水平布局模式',
  },
  {
    id: '3',
    title: '行组件显示或隐藏时可配置回调事件',
  },
  {
    id: '4',
    title: '支持单独的头部组件',
  },
  {
    id: '5',
    title: '支持单独的尾部组件',
  },
  {
    id: '6',
    title: '支持自定义行间分隔线',
  },
  {
    id: '7',
    title: '支持上拉加载212。',
  },
  {
    id: '8',
    title: '支持上拉加载212。',
  },
  {
    id: '9',
    title: '支持上拉加载212。',
  },
  {
    id: '10',
    title: '支持上拉加载212。',
  },
  {
    id: '11',
    title: '支持自定义行间分隔线2',
  },
];

interface ItemProps {
  title: string,
  content: string,
  callback: () => void
}

interface ItemState {
  title:string
}

const Item: React.FC<ItemProps> = ({ title, content, callback }) => {
  return (
    <View>
      <TouchableOpacity onPress={() => {
        callback()
      }
      }>
        <Text style={styles.item}>{title}</Text>
      </TouchableOpacity>
    </View >
  );
}

export default class HomeScreen extends Component<ItemProps, ItemState> {


  constructor(props: ItemProps) {
    super(props)
    this.state = {
      title: this.props.title
    }
  }

  handleItemClick() {
    //Alert.alert('ss' + {this.state})
    this.props.callback()
  }

  render(): React.ReactNode {
    const { title, content, callback } = this.props;
  
    return (
      <FlatList
        data={data}
        renderItem={({ item }) => <Item title={item.title}
          content={content} callback={()=>{
            this.props.callback()
          }} />}
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