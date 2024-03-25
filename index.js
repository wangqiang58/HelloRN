/**
 * @format
 */

import { AppRegistry } from 'react-native';
import HomeScreen from './home';

import { name as appName } from './app.json';
import DetailPage from './detai';
import IndexScreen from './homepage';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import AppScreen from './App';
import LoginScreen from './login';
import SplashScreen from './splash';

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
