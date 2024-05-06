/**
 * @format
 */

import { AppRegistry } from 'react-native';
import DetailPage from './src/pages/detai';
import AppScreen from './src/pages/Appbars';
import LoginScreen from './src/pages/login';
import SplashScreen from './src/pages/splash';
import { Router,Scene } from 'react-native-router-flux';

const App = () => {
        return (<Router>
          <Scene key="root">
            <Scene key="SplashScreen" component={SplashScreen} hideNavBar={true}/>
            <Scene key="LoginScreen" component={LoginScreen} hideNavBar={true}/>
            <Scene key="AppScreen" component={AppScreen} hideNavBar={true}/>
            <Scene key="Details" component={DetailPage} hideBackImage={false} title={'新闻列表'}/>
          </Scene>
        </Router>)
    
};


AppRegistry.registerComponent("home", () => {
    return App
});
