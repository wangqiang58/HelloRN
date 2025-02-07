/**
 * @format
 */

import { Alert, AppRegistry, UIManager, StatusBar, Platform, View } from 'react-native';
import DetailPage from './src/modules/home/pages/detai';
import AppSketchScreen from './src/modules/home/pages/Appbars';
import LoginScreen from './src/modules/home/pages/login';
import SplashScreen from './src/modules/home/pages/splash';
import MessageQueue from 'react-native/Libraries/BatchedBridge/MessageQueue';
import BatchedBridge from 'react-native/Libraries/BatchedBridge/BatchedBridge';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';

const spyFunction = (msg)=>{
  console.log(msg)
}

// MessageQueue.spy(spyFunction);

const Stack = createStackNavigator();

const App = ()=> {
  return (
    <View style={{ flex: 1 }}>
      <StatusBar
        translucent={true}
        backgroundColor="transparent"
        barStyle="dark-content"
      />
      <NavigationContainer>
        <Stack.Navigator
          screenOptions={{
            headerShown: false,
            contentStyle: { backgroundColor: 'white' },
          }}
          initialRouteName="AppSketchScreen"
        >
          <Stack.Screen name="AppSketchScreen" component={AppSketchScreen} />
          <Stack.Screen name="LoginScreen" component={LoginScreen} />
          <Stack.Screen name="SplashScreen" component={SplashScreen} />
          <Stack.Screen name="DetailPage" component={DetailPage} />
        </Stack.Navigator>
      </NavigationContainer>
    </View>
  );
}

const styles = {
  navBar: {
    backgroundColor: 'transparent',
    borderBottomWidth: 0,
    elevation: 0,
    paddingTop: Platform.OS === 'android' ? StatusBar.currentHeight : 0,
  },
  navBarWithTitle: {
    backgroundColor: '#FFFFFF',
    borderBottomWidth: 0,
    elevation: 0,
    paddingTop: Platform.OS === 'android' ? StatusBar.currentHeight : 0,
  },
  navTitle: {
    color: '#000000',
    fontSize: 22,
    fontWeight: '500',
  },
  backButtonText: {
    color: '#FFFFFF',
  }
};


BatchedBridge.registerCallableModule('HelloRegistry',{
   print2(){
    Alert.alert('hello world')
   }
})

AppRegistry.registerComponent("home", () => App);
