import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import * as React from 'react';
import { Component } from 'react';
import HomeScreen from './screen/homeScreen';
import SurveyWebViewScreen from './screen/surveyWebViewScreen';

const Stack = createNativeStackNavigator();

export default class App extends Component {
    public render() {
        return (
            <NavigationContainer>
                <Stack.Navigator>
                    <Stack.Screen name="Home" component={HomeScreen} options={{ title: 'Digital Feedback' }} />
                    <Stack.Screen name="Survey" component={SurveyWebViewScreen} />
                </Stack.Navigator>
            </NavigationContainer>
        );
    }
}
