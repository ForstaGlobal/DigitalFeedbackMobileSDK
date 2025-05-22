import React from 'react';
import {NavigationContainer} from '@react-navigation/native';
import HomeView from './views/homeView';
import {createNativeStackNavigator} from '@react-navigation/native-stack';

const Stack = createNativeStackNavigator();

function App(): React.JSX.Element {

  return (
      <NavigationContainer>
          <Stack.Navigator>
              <Stack.Screen name="Home" component={HomeView} options={{ title: 'Digital Feedback' }} />
          </Stack.Navigator>
      </NavigationContainer>
  );
}

export default App;
