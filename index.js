/**
 * @format
 */

import { Alert, AppRegistry, UIManager } from 'react-native';
import DetailPage from './src/modules/home/pages/detai';
import AppScreen from './src/modules/home/pages/Appbars';
import LoginScreen from './src/modules/home/pages/login';
import SplashScreen from './src/modules/home/pages/splash';
import { Router,Scene } from 'react-native-router-flux';
import MessageQueue from 'react-native/Libraries/BatchedBridge/MessageQueue';
import BatchedBridge from 'react-native/Libraries/BatchedBridge/BatchedBridge';

const spyFunction = (msg)=>{
  console.log(msg)
}

ErrorUtils.setGlobalHandler(()=>{
  Alert.alert('放生异常')
})
// MessageQueue.spy(spyFunction);

const App = () => {
        return (<Router>
          <Scene key="root">
          <Scene key="AppScreen" component={AppScreen} hideNavBar={true} />
          <Scene key="LoginScreen" component={LoginScreen} hideNavBar={true}/>
            <Scene key="SplashScreen" component={SplashScreen} hideNavBar={true}/>
            <Scene key="Details" component={DetailPage} hideBackImage={false} title={'新闻列表'}/>
          </Scene>
        </Router>)
    
};

const callback2 = ()=>{
   console.log("xxxxx")
   console.trace()
}

BatchedBridge.registerCallableModule('HelloRegistry',{
   print2(){
    Alert.alert('hello world')
   }
})

AppRegistry.registerComponent("home", () => {
    return App
});
