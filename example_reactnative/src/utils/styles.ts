import { StyleSheet } from 'react-native';

module.exports = StyleSheet.create({
    questionTitle: {
        fontSize: 16,
        color: '#000000',
        fontWeight: '500'
    },
    questionBottom: {
        marginBottom: 16
    },
    questionError: {
        fontSize: 16,
        fontWeight: '500',
        color: '#ff0000',
        marginVertical: 4
    },
    questionDivider: {
        width: '100%',
        borderBottomWidth: 1,
        borderColor: 'black',
        marginVertical: 4
    },
    questionHeader: {
        fontSize: 14,
        fontWeight: '500',
        marginStart: 4
    }
});
