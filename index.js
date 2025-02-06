/**
 * @format
 */

import { Alert, AppRegistry, UIManager, StatusBar, Platform, View } from 'react-native';
import DetailPage from './src/modules/home/pages/detai';
import AppSketchScreen from './src/modules/home/pages/Appbars';
import LoginScreen from './src/modules/home/pages/login';
import SplashScreen from './src/modules/home/pages/splash';
import { Router,Scene } from 'react-native-router-flux';
import MessageQueue from 'react-native/Libraries/BatchedBridge/MessageQueue';
import BatchedBridge from 'react-native/Libraries/BatchedBridge/BatchedBridge';

const spyFunction = (msg)=>{
  console.log(msg)
}

// MessageQueue.spy(spyFunction);

const App = () => {
        return (
          <View style={{ flex: 1 }}>
          <StatusBar
            translucent={true}
            backgroundColor="transparent"
            barStyle="light-content"
          />
          <Router>
            <Scene key="root" navigationBarStyle={styles.navBar} titleStyle={styles.navTitle}>
              <Scene 
                key="AppSketchScreen" 
                component={AppSketchScreen} 
                hideNavBar={true}
                initial
              />
              <Scene 
                key="LoginScreen" 
                component={LoginScreen} 
                hideNavBar={true}
              />
              <Scene 
                key="SplashScreen" 
                component={SplashScreen} 
                hideNavBar={true}
              />
              <Scene 
                key="Details" 
                component={DetailPage} 
                hideBackImage={true} 
                hideNavBar={true}
                navigationBarStyle={styles.navBarWithTitle}
                titleStyle={styles.navTitle}
                backButtonTextStyle={styles.backButtonText}
                backButtonTintColor="#fff"
              />
            </Scene>
          </Router>
        </View>
        )
    
};

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

AppRegistry.registerComponent("home", () => {
    return App
});
