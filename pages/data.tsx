import AsyncStorage, { useAsyncStorage } from '@react-native-async-storage/async-storage';
import { Alert } from 'react-native';

// 存储数据
export const storeData = async (key: string, value: any) => {
    try {
        await AsyncStorage.setItem(key, value);
        console.log('Data stored successfully');
    } catch (e) {
        console.log('Error storing data:', e);
    }
};

// 获取数据
export const getData = async (key: string) => {
    try {
        const jsonValue = await AsyncStorage.getItem(key);
        return jsonValue != null ? jsonValue : null;
    } catch (e) {
        Alert.alert('Error reading data:' + e);
        return null;
    }
};
