import { IDefaultQuestion } from 'react-native-mobilesdk';
import { PageControl } from '../../../src/survey/pageControl';

export interface IQuestionProps {
    pageControl: PageControl;
    info: IDefaultQuestion;
}
