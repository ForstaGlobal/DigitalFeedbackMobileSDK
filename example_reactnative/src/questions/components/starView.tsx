import React from 'react';
import { Image, TouchableHighlight } from 'react-native';

interface IStarViewProps {
    fill: boolean;
    code: string;
    onStarPress: (code: string) => void;
}
const StarView = (props: IStarViewProps) => {
    const starEmpty = require('../../img/ic_star_border_black.png');
    const starFilled = require('../../img/ic_star_black.png');
    const image = props.fill ? starFilled : starEmpty;
    const onPress = () => {
        props.onStarPress(props.code);
    };

    return (
        <TouchableHighlight style={{ marginHorizontal: 4 }} underlayColor={'#00000022'} onPress={onPress}>
            <Image tintColor={'#000000aa'} source={image} />
        </TouchableHighlight>
    );
};

export default StarView;
