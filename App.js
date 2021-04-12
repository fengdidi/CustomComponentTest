/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React,{ useEffect, useRef,useState } from 'react';
import type {Node} from 'react';
import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
  Button
} from 'react-native';

import {
  Colors,
  DebugInstructions,
  Header,
  LearnMoreLinks,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';
import { Dimensions, UIManager, findNodeHandle } from 'react-native';
import GinCamera from './src/GinCamera';


const App: () => Node = () => {

  const [filter, setFilter] = useState('NONE');
  const [overlay, setOverlay] = useState('NONE');

  let windowHeight = Dimensions.get('window').height;
  let windowWidth = Dimensions.get('window').width;

  const cameraRef = useRef(null)


  const record = () => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(cameraRef.current),
      UIManager.GinCamera.Commands.takeAVideo,
      [5000]
   );

  }

  return (
    <View style={styles.container}>
      <GinCamera ref={cameraRef} style={{ height: windowHeight,width: windowWidth }}
        filter={filter}
        overlay={overlay}
      />
      <View style={styles.controlOverlay}>
        <View style={styles.buttonContainer}>
          <Button style={styles.button}
            onPress={()=>{
              setFilter("BLACK_AND_WHITE")
            }}
            title="BLACK_AND_WHITE filter">
          </Button>
        </View>
        <View style={styles.buttonContainer}>
          <Button style={styles.button}
            onPress={()=>{
              setFilter("TEMPERATURE")
            }}
            title="TEMPERATURE filter">
          </Button>
        </View>
        <View style={styles.buttonContainer}>
          <Button style={styles.button}
            onPress={()=>{
              setFilter("NONE")
            }}
            title="No filter">
          </Button>
        </View>
        <View style={styles.buttonContainer}>
          <Button style={styles.button}
            onPress={()=>{
              setOverlay("snap")
            }}
            title="猫">
          </Button>
        </View>
        <View style={styles.buttonContainer}>
          <Button style={styles.button}
            onPress={()=>{
              setOverlay("glasses5")
            }}
            title="メガネ">
          </Button>
        </View>
        <View style={styles.buttonContainer}>
          <Button style={styles.button}
            onPress={()=>{
              setOverlay("none")
            }}
            title="No Overlay">
          </Button>
        </View>
        <View style={styles.buttonContainer}>
          <Button style={styles.button}
            onPress={record}
            title="録画 ">
          </Button>
        </View>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    width: '100%',
    height: '100%'
  },
  controlOverlay: {
    position: 'absolute',
    left: 0,
    top: 0,
    width: Dimensions.get('window').width,
    height: Dimensions.get('window').height,
    display: 'flex',
    justifyContent: 'flex-start',
    alignItems: 'flex-start',
  },
  buttonContainer: {
    width: Dimensions.get('window').width/2,
    marginBottom: 5
  }
});

export default App;
