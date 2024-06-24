import React from 'react';
import { Image, Text, TouchableHighlight, View } from 'react-native';

interface IRadioViewProps {
    code: string;
    selected: boolean;
    onRadioPressCode: (code: string) => void;
    text: string;
    checkbox: boolean;
    testId: string;
}
const RadioView = (props: IRadioViewProps) => {
    const radioEmpty = require('../../img/outline_radio_button_unchecked_black.png');
    const radioFilled = require('../../img/outline_radio_button_checked_black.png');

    const checkboxEmpty = require('../../img/ic_check_box_outline_blank_black.png');
    const checkboxFilled = require('../../img/ic_check_box_black.png');

    const checkImg = props.selected ? checkboxFilled : checkboxEmpty;

    const radioImg = props.selected ? radioFilled : radioEmpty;
    const onPress = () => {
        props.onRadioPressCode(props.code);
    };

    return (
        <TouchableHighlight testID={props.testId} underlayColor={'#00000022'} onPress={onPress}>
            <View style={{ flexDirection: 'row', alignItems: 'center', marginVertical: 4 }}>
                <Image tintColor={'#000000aa'} source={props.checkbox ? checkImg : radioImg} />
                <Text style={{ marginLeft: 8, fontSize: 16 }}>{props.text}</Text>
            </View>
        </TouchableHighlight>
    );
};

export default RadioView;
