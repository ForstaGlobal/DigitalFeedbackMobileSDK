// @ts-ignore
import { IDefaultQuestion, ISurveyErrored, ISurveyFinished, ISurveyModel, NodeType, SurveyFrameManager } from '@forstaglobal/react-native-mobilesdk';
import React, { JSX, useEffect, useState } from 'react';
import { Button, EmitterSubscription, ScrollView, StyleSheet, Text, View } from 'react-native';
import QuestionInfoView from './questionInfoView';
import QuestionMultiView from './questionMultiView';
import QuestionNumericView from './questionNumericView';
import QuestionSingleView from './questionSingleView';
import QuestionTextView from './questionTextView';
import { PageControl } from '../../../src/survey/pageControl';

interface IDialogViewProps {
    onClose: () => void;
    hidden: boolean;
    config: ISurveyModel;
}

interface IFinishedConfig {
    finished: boolean;
    message: string;
}

const DialogView = (props: IDialogViewProps) => {
    const [infos, setInfos] = useState<IDefaultQuestion[]>([]);
    const [showFinished, setShowFinished] = useState<IFinishedConfig>({ finished: false, message: '' });
    const [pageControl, setPageControl] = useState<PageControl>();

    const onSurveyQuit = (_: { [key: string]: string }) => {
        props.onClose();
    };

    const onSurveyErrored = async (_: ISurveyErrored) => {
        setShowFinished({
            finished: true,
            message: 'Sorry an error happen'
        });
    };

    const onSurveyFinished = async (_: ISurveyFinished) => {
        setShowFinished({
            finished: true,
            message: 'Thank you!'
        });
    };

    const onSurveyPageReady = async (control: PageControl) => {
        setShowFinished({
            finished: false,
            message: ''
        });
        setPageControl(control);

        // load questions
        const questions = await control.getQuestion();
        setInfos(questions);
    };

    useEffect(() => {
        const pageReady: EmitterSubscription = SurveyFrameManager.setOnSurveyPageReady(onSurveyPageReady);
        const finished: EmitterSubscription = SurveyFrameManager.setOnSurveyFinished(onSurveyFinished);
        const errored: EmitterSubscription = SurveyFrameManager.setOnSurveyErrored(onSurveyErrored);
        const quit: EmitterSubscription = SurveyFrameManager.setOnSurveyQuit(onSurveyQuit);

        return () => {
            pageReady.remove();
            finished.remove();
            errored.remove();
            quit.remove();
        };
    });

    const onCloseAction = async () => {
        if (!pageControl) {
            return;
        }

        if (!showFinished.finished) {
            await pageControl.quit(true);
        } else {
            props.onClose();
        }
    };

    const onBackAction = async () => {
        if (!pageControl) {
            return;
        }

        await pageControl.back();
    };

    const onNextAction = async () => {
        if (!pageControl) {
            return;
        }

        await pageControl.next();
    };

    const renderFinishedPage = () => {
        return (
            <View>
                <Text style={styles.title}>{showFinished.message}</Text>
            </View>
        );
    };

    const renderQuestion = () => {
        if (!pageControl) {
            return [];
        }

        const elements: JSX.Element[] = [];
        for (const info of infos) {
            switch (info.nodeType) {
                case NodeType.Text: {
                    elements.push(<QuestionTextView key={info.id} info={info} pageControl={pageControl} />);
                    break;
                }
                case NodeType.Info:
                    elements.push(<QuestionInfoView key={info.id} info={info} pageControl={pageControl} />);
                    break;
                case NodeType.Numeric: {
                    elements.push(<QuestionNumericView key={info.id} info={info} pageControl={pageControl} />);
                    break;
                }
                case NodeType.Single: {
                    elements.push(<QuestionSingleView key={info.id} info={info} pageControl={pageControl} />);
                    break;
                }
                case NodeType.Multi: {
                    elements.push(<QuestionMultiView key={info.id} info={info} pageControl={pageControl} />);
                    break;
                }
            }
        }
        return elements;
    };

    const render = () => {
        if (props.hidden || !pageControl) {
            return <View />;
        }

        return (
            <View style={styles.container}>
                <View style={styles.topView}>
                    <Button title={'Close'} color={'#ff0000'} onPress={onCloseAction} />
                </View>
                <View style={styles.fillView}>
                    <View style={{ display: showFinished.finished ? 'flex' : 'none', justifyContent: 'center', alignItems: 'center' }}>{renderFinishedPage()}</View>
                    <ScrollView style={{ paddingHorizontal: 16, display: showFinished.finished ? 'none' : 'flex' }}>{renderQuestion()}</ScrollView>
                </View>
                <View style={styles.bottomView}>
                    <View>{!showFinished.finished && pageControl.showBackward && <Button testID={'btnBack'} onPress={onBackAction} title={pageControl.backwardText} />}</View>
                    <View>{!showFinished.finished && pageControl.showForward && <Button testID={'btnForward'} onPress={onNextAction} title={pageControl.forwardText} />}</View>
                </View>
            </View>
        );
    };

    return render();
};
export default DialogView;
const styles = StyleSheet.create({
    container: {
        position: 'absolute',
        top: 0,
        bottom: 0,
        left: 0,
        right: 0,
        flex: 1,
        flexDirection: 'column',
        justifyContent: 'space-between',
        alignItems: 'stretch',
        backgroundColor: '#fff'
    },
    topView: {
        padding: 16,
        justifyContent: 'center',
        alignItems: 'flex-end'
    },
    bottomView: {
        padding: 16,
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center'
    },
    fillView: {
        flex: 1,
        justifyContent: 'flex-start',
        alignItems: 'stretch'
    },
    title: {
        fontSize: 16,
        color: '#000000',
        fontWeight: '500',
        justifyContent: 'center',
        alignItems: 'center'
    }
});
