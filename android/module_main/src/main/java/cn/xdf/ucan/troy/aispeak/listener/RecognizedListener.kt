package cn.xdf.ucan.troy.aispeak.listener

import com.microsoft.cognitiveservices.speech.CancellationErrorCode

interface RecognizedListener {

    abstract fun recognizedResult(result: String?)

    abstract fun recognizedError(result: CancellationErrorCode?)
    abstract fun playSpeakEnd(content: String?)
    abstract fun playSpeakError(content: String?)
}