import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import * as React from 'react';
import { Component } from 'react';
import HomeView from './views/homeView';

const Stack = createNativeStackNavigator();

export default class App extends Component {
    public render() {
        return (
            <NavigationContainer>
                <Stack.Navigator>
                    <Stack.Screen name="Home" component={HomeView} options={{ title: 'Digital Feedback' }} />
                </Stack.Navigator>
            </NavigationContainer>
        );
    }
}
