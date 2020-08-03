import Foundation
import MobileSdk

class SimpleFbController: SurveyLayoutController {
    @IBOutlet var btnClose: UIButton!
    @IBOutlet var scrollView: UIScrollView!
    
    var isInitialized: Bool = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        hideKeyboardWhenTap()
        
        SimpleFbStyle.styleCloseButton(btnClose)
        
        scrollView.delegate = self
        scrollView.bounces = false
        scrollView.showsVerticalScrollIndicator = false
        scrollView.showsHorizontalScrollIndicator = false
        scrollView.isScrollEnabled = false
        
        loadFrame(surveyFrameDelegate: self)
    }
    
    func setupPages(pages: [UIView]) {
        scrollView.frame = CGRect(x: 0, y: 0, width: view.frame.width, height: view.frame.height)
        scrollView.contentSize = CGSize(width: scrollView.frame.width * CGFloat(pages.count), height: scrollView.frame.height)
        scrollView.isPagingEnabled = true
        
        for i in 0 ..< pages.count {
            pages[i].frame = CGRect(x: view.frame.width * CGFloat(i), y: 0, width: view.frame.width, height: view.frame.height)
            scrollView.addSubview(pages[i])
        }
    }
    
    @IBAction func onCloseClicked(_ sender: Any) {
        _ = surveyFrame.quit(upload: AppConfigs.uploadImcomplete)
    }
}

extension SimpleFbController: SurveyFrameDelegate {
    func onSurveyPageReady(page: SurveyPage) {
        let errorMessage = "Survey must consist of 2 questions with [Single, Text] combination."
        
        if isInitialized {
            errorCloseSurvey(message: errorMessage)
            return
        }
        
        let questions = page.questions
        if questions.count != 2 {
            errorCloseSurvey(message: errorMessage)
            return
        }
        
        guard let singleQuestion = questions[0] as? SingleQuestion else {
            errorCloseSurvey(message: errorMessage)
            return
        }
        
        guard let textQuestion = questions[1] as? TextQuestion else {
            errorCloseSurvey(message: errorMessage)
            return
        }
        
        let ratingView: SimpleFbRatingView = Utils.fromNib()
        ratingView.setup(surveyFrame: surveyFrame, singleQuestion: singleQuestion)
        ratingView.delegate = self
        
        let textView: SimpleFbOpenTextView = Utils.fromNib()
        textView.setup(surveyFrame: surveyFrame, textQuestion: textQuestion)
        textView.delegate = self
        
        setupPages(pages: [ratingView, textView])
        
        isInitialized = true
    }
    
    func onSurveyErrored(page: SurveyPage, values: [String: String?], error: Error) {
        errorCloseSurvey(error: error)
    }
    
    func onSurveyFinished(page: SurveyPage, values: [String: String?]) {
        closeSurvey()
    }
    
    func onSurveyQuit(values: [String: String?]) {
        closeSurvey()
    }
}

extension SimpleFbController: SimpleFbRatingViewDelegate {
    func onFaceChanged(face: SimpleFbFaceIcon) {
        if let backgroundView = self.view as? SimpleFbBackgroundView {
            backgroundView.updateColor(face: face)
        }
    }
    
    func onNextClicked(question: SingleQuestion) -> [ValidationQuestionError] {
        do {
            let result = try question.validate()
            if result.count == 0 {
                scrollView.setContentOffset(CGPoint(x: scrollView.frame.width, y: 0), animated: true)
            }
            return result
        } catch let error {
            errorCloseSurvey(error: error)
            return []
        }
    }
}

extension SimpleFbController: SimpleFbOpenTextViewDelegate {
    func onSubmitClicked(question: TextQuestion) -> [ValidationQuestionError] {
        do {
            let result = try question.validate()
            if result.count == 0 {
                _ = surveyFrame.next()
            }
            return result
        } catch let error {
            errorCloseSurvey(error: error)
            return []
        }
    }
}

extension SimpleFbController: UIScrollViewDelegate {}
