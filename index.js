/**
 * @format
 */

import { AppRegistry } from 'react-native';
import HomeScreen from './pages/home';

import { name as appName } from './app.json';
import DetailPage from './pages/detai';
import IndexScreen from './pages/homepage';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import AppScreen from './pages/App';
import LoginScreen from './pages/login';
import SplashScreen from './pages/splash';

const Stack = createStackNavigator();

const App = () => {
    return (
        <NavigationContainer>
            <Stack.Navigator initialRouteName="SplashScreen">
               <Stack.Screen name='SplashScreen' component={SplashScreen} options={{ title: null, headerShown: false }}></Stack.Screen>
                <Stack.Screen name='LoginScreen'  component={LoginScreen} options={{ title: null, headerShown: false }}></Stack.Screen>
                <Stack.Screen name="AppScreen" component={AppScreen} options={{ title: null, headerShown: false }}>
                </Stack.Screen>
                <Stack.Screen name="Index" component={IndexScreen} options={{ title: null, headerShown: false }}>
                </Stack.Screen>
                <Stack.Screen name="Details" component={DetailPage} options={{ title: '详情页', headerShown: true }}>
                </Stack.Screen>
            </Stack.Navigator>
        </NavigationContainer>
    );
};

AppRegistry.registerComponent(appName, () => {
    return App
});
