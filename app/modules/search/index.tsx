import React, { Component, useEffect } from "react";
import {
  StyleSheet,
  Text,
  View,
  Image,
  ScrollView,
  Button,
} from "react-native";

export default class SearchPage extends Component{

    render(): React.ReactNode {

        return <View style={styles.container}>
            <Text style={styles.hello}>BU1 </Text>
            <Button
              title="前往BU2"
              onPress={() => {
               
              }}
             />
      
            <ScrollView style={styles.flatContainer}>
              <View style={styles.imgView}>
               
              </View>
            </ScrollView>
          </View>
    }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    height: 100,
  },
  hello: {
    fontSize: 20,
    textAlign: "center",
    margin: 10,
  },
  imgView: {
    width: "100%",
  },
  img: {
    width: "100%",
    height: 600,
  },
  flatContainer: {
    flex: 1,
  },
  btn: {
    width: 30,
    height: 30,
  },
});