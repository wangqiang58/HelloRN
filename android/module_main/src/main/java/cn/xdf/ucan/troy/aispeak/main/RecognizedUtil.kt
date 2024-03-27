package cn.xdf.ucan.troy.aispeak.main

import android.media.AudioTrack
import android.media.MediaPlayer
import android.util.Log
import cn.xdf.ucan.troy.aispeak.ModuleMainApplication
import cn.xdf.ucan.troy.aispeak.listener.RecognizedListener
import cn.xdf.ucan.troy.aispeak.mvp.AIPresenter
import com.microsoft.cognitiveservices.speech.AudioDataStream
import com.microsoft.cognitiveservices.speech.CancellationDetails
import com.microsoft.cognitiveservices.speech.CancellationReason
import com.microsoft.cognitiveservices.speech.Connection
import com.microsoft.cognitiveservices.speech.ResultReason
import com.microsoft.cognitiveservices.speech.SpeechConfig
import com.microsoft.cognitiveservices.speech.SpeechRecognizer
import com.microsoft.cognitiveservices.speech.SpeechSynthesisCancellationDetails
import com.microsoft.cognitiveservices.speech.SpeechSynthesisEventArgs
import com.microsoft.cognitiveservices.speech.SpeechSynthesisOutputFormat
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult
import com.microsoft.cognitiveservices.speech.SpeechSynthesisWordBoundaryEventArgs
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer
import com.microsoft.cognitiveservices.speech.audio.AudioConfig
import java.util.concurrent.Executors


class RecognizedUtil {
    companion object {
        // Replace below with your own subscription key
        private const val speechSubscriptionKey =
            "721e1d2ce5a34c0b9fa3e314d3e97aa8"//  207b8a8a604f4f829e47f3d6f96820b7
//        private const val speechSubscriptionKey2 = "db6b927bbc3f4510b65eec62c7e19c08"
//        private const val speechSubscriptionKey3 = "6f161f0d1e904319835652a31c102b60"

        // Replace below with your own service region (e.g., "westus").
        private const val speechRegion = "chinaeast2"
//        private const val speechRegion2_3 = "eastasia"

        private const val TAG = "RecognizedUtil"

        private val executorService = Executors.newCachedThreadPool()
        private var singleThreadExecutor = Executors.newSingleThreadExecutor()

    }

    private var mSpeechConfig: SpeechConfig? = null
    private var mSynthesizer: SpeechSynthesizer? = null
    var mRecognizedListener: RecognizedListener? = null
    private var audioTrack: AudioTrack? = null
    private var mMediaPlayer = MediaPlayer()
    private var audioDataStream: AudioDataStream? = null
    private var connection: Connection? = null
    private val synchronizedObj = Any()
    private var stopped = false
    private var microphoneStream: MicrophoneStream? = null
    private var mPlayContentText: String = ""
    private var mSpeechRecognizer: SpeechRecognizer? = null
    private val content = ArrayList<String>()
    private val subscriptionKeys = ArrayList<String>()
    private var currentKeyIndex = 0;

    init {
//        initSubscriptionKeys()
        mSpeechConfig = SpeechConfig.fromSubscription(speechSubscriptionKey, speechRegion)
        initSynthPressed()
        initRecognized()
    }

//    private fun initSubscriptionKeys() {
//        subscriptionKeys.add(speechSubscriptionKey)
//        subscriptionKeys.add(speechSubscriptionKey2)
//        subscriptionKeys.add(speechSubscriptionKey3)
//    }

//    private fun getNextSubscriptionKey() : Pair<String, String>{
//        currentKeyIndex += 1
//        currentKeyIndex = if (currentKeyIndex == 3) 0 else currentKeyIndex
//        return Pair<String, String>(subscriptionKeys[currentKeyIndex],
//            if (currentKeyIndex % 3 == 0) speechRegion else speechRegion2_3)
//    }

    private fun initSynthPressed() {
        if (mSynthesizer != null) {
            mSpeechConfig!!.close()
            mSynthesizer!!.close()
            connection?.close()
        }

//        audioTrack = AudioTrack(
//            AudioAttributes.Builder()
//                .setUsage(AudioAttributes.USAGE_MEDIA)
//                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
//                .build(),
//            AudioFormat.Builder()
//                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
//                .setSampleRate(24000)
//                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
//                .build(),
//            AudioTrack.getMinBufferSize(
//                24000,
//                AudioFormat.CHANNEL_OUT_MONO,
//                AudioFormat.ENCODING_PCM_16BIT
//            ) * 2,
//            AudioTrack.MODE_STREAM,
//            AudioManager.AUDIO_SESSION_ID_GENERATE
//        )
        // Reuse the synthesizer to lower the latency.
        // I.e. create one synthesizer and speak many times using it.

        // Use 24k Hz format for higher quality.
        mSpeechConfig?.setSpeechSynthesisOutputFormat(SpeechSynthesisOutputFormat.Raw24Khz16BitMonoPcm)
        // Set voice name.
        mSpeechConfig?.speechSynthesisVoiceName = "en-US-JennyNeural"
        val audioConfig = AudioConfig.fromDefaultSpeakerOutput()
        mSynthesizer = SpeechSynthesizer(mSpeechConfig, audioConfig)
//        connection = Connection.fromSpeechSynthesizer(mSynthesizer)

        mMediaPlayer.isLooping = false
        // we force a restart audio stream on the next play button press
        if (stopped) {
            audioDataStream?.close()
            audioDataStream = null
        }
        setupLogs()
    }

    private fun initRecognized() {
        destroyMicrophoneStream() // in case it was previously initialized
        microphoneStream = MicrophoneStream()
        mSpeechRecognizer = SpeechRecognizer(
            mSpeechConfig,
            AudioConfig.fromStreamInput(MicrophoneStream.create())
        )
        setRecognizedEvent()
    }

//    fun switchSpeechConfigAndStartRecognize() {
//        speechRecon?.close()
//        speechConfig?.close()
//        val nextSubscriptionKeyPair = getNextSubscriptionKey()
//        Log.i(TAG, " switchSpeechConfigAndStartRecognize nextSubscriptionKeyPair ${nextSubscriptionKeyPair.first}, ${nextSubscriptionKeyPair.second}")
//        speechConfig = SpeechConfig.fromSubscription(nextSubscriptionKeyPair.first, nextSubscriptionKeyPair.second)
////        initSynthPressed()
//        startRecognized()
//    }

    fun getCurrentSubscriptionKey() = subscriptionKeys[currentKeyIndex]

    private fun setRecognizedEvent() {
        mSpeechRecognizer?.recognized?.addEventListener { sender, e ->
//            if (e.result.reason == ResultReason.RecognizedSpeech) {
//                var finalResult = e.result.text
//                content.add(finalResult)
//                var stringContent :String  = TextUtils.join(" ", content)
//                Log.i(activityTag, "Continuous recognition endRecognized " + stringContent)
////                stopRecon()
//                recognizedListener?.recognizedResult(stringContent)
//            }
            Log.i(TAG, " recognition recognized ${e}")

        }
        mSpeechRecognizer?.recognizing?.addEventListener { sender, e ->
            var s = e.result.text
            content.add(s);
//            Log.i(TAG, "recognition recognizing " + TextUtils.join(" ", content))
            content.removeAt(content.size - 1)
        }
        mSpeechRecognizer?.canceled?.addEventListener { sender, e ->
            Log.i(TAG, "recognition canceled ${e}")
        }
        mSpeechRecognizer?.sessionStopped?.addEventListener { sender, e ->
            Log.i(TAG, "recognition sessionStopped ${e}")
        }
        mSpeechRecognizer?.sessionStarted?.addEventListener { sender, e ->
            Log.i(TAG, "recognition sessionStarted ${e}")
        }
    }

    public fun stopRecon() {
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer?.canceled
            mSpeechRecognizer?.stopContinuousRecognitionAsync()
        }
    }

    fun onDestroy() {
        destroyMicrophoneStream()
        stopRecon()
        if (mSpeechRecognizer != null) {
            mSpeechConfig?.close()
            mSpeechConfig = null
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.release()
        }
    }

    private fun destroyMicrophoneStream() {
        if (microphoneStream != null) {
            microphoneStream?.close()
            microphoneStream = null
        }
    }

    fun startRecognized() {
        mSpeechRecognizer = SpeechRecognizer(
            mSpeechConfig,
            AudioConfig.fromStreamInput(MicrophoneStream.create())
        )
        val task = mSpeechRecognizer?.recognizeOnceAsync()
        executorService.submit {
            val result = task?.get()
            if (result!!.reason === ResultReason.RecognizedSpeech) {
                Log.i(TAG, "speechRecon get() ==  ${result} ")
                mRecognizedListener?.recognizedResult(result!!.text)
//                Log.i(TAG, "RECOGNIZED: Text=" + result!!.getText())
            } else if (result!!.reason === ResultReason.NoMatch) {
                Log.i(TAG, "NOMATCH: Speech could not be recognized.")
                mRecognizedListener?.recognizedResult(result!!.text)
            } else if (result!!.reason === ResultReason.Canceled) {
                val cancellation = CancellationDetails.fromResult(result)
                Log.i(TAG, "CANCELED: Reason=" + cancellation.reason)
                if (cancellation.reason == CancellationReason.Error) {
                    Log.i(TAG, "CANCELED: ErrorCode=" + cancellation.errorCode)
                    Log.i(TAG, "CANCELED: ErrorDetails=" + cancellation.errorDetails)
                    mRecognizedListener?.recognizedError(cancellation.errorCode)
                }
            }
            mSpeechRecognizer?.close()
        }
    }

    public fun playDemo(content: String):SpeechSynthesisResult{
        var task = mSynthesizer!!.SpeakTextAsync(content)
        return task!!.get()
    }
    private fun playSpeak(content: String) {
        var task = mSynthesizer!!.SpeakTextAsync(content)
        singleThreadExecutor!!.execute {
            var result: SpeechSynthesisResult = task!!.get()
            if (result.reason == ResultReason.SynthesizingAudioCompleted) {
                var size = ModuleMainApplication.mPresenter.mQueueList
                if (ModuleMainApplication.mPresenter.mCloseSSE && size.isEmpty()) {//列表中的流氏数据消费完，代表播放完毕
                    mRecognizedListener?.playSpeakEnd(content)
                    Log.i(TAG, "Synthesis completed content:${content}")
                    return@execute
                }
                var queueItem = ModuleMainApplication.mPresenter.removeQueue()
                if (!queueItem.isNullOrEmpty() || !ModuleMainApplication.mPresenter.mCloseSSE) {
                    if (!queueItem.isNullOrEmpty()) {
                        playSpeak(queueItem!!)
                    }
                }
            } else if (result.reason == ResultReason.Canceled) {
                val cancellation = SpeechSynthesisCancellationDetails.fromResult(result)
                Log.i(TAG, "Synthesis CANCELED: Reason= ${cancellation.reason}")
                if (cancellation.reason == CancellationReason.Error) {
                    mRecognizedListener?.playSpeakError(cancellation.errorDetails)
                }
            }
        }
    }

    fun play(urlOrContent: String) {
        Log.i(TAG,"开始播放 ： urlOrContent ：$urlOrContent")
        if (urlOrContent.startsWith("https") || urlOrContent.startsWith("http:")) {
            playAudio(urlOrContent)
        } else {
            playSpeak(urlOrContent)
        }
    }

    fun pausePlayback() {
//        if (audioTrack != null) {
//            synchronized(synchronizedObj) { stopped = true }
//            audioTrack!!.pause()
//        }
        mMediaPlayer.pause()
        mMediaPlayer.stop()
        mSynthesizer!!.StopSpeakingAsync()
    }

    private fun playAudio(url: String) {
        mMediaPlayer.setOnCompletionListener {
            mRecognizedListener?.playSpeakEnd(mPlayContentText)
            Log.d(
                TAG,
                "---MediaPlayer--- onCompletion: "
            )
        }
        mMediaPlayer.setOnErrorListener { mp, what, extra ->
            Log.d(TAG, "---MediaPlayer--- onError: $what, $extra")
            false
        }
        Thread {
            try {
                mMediaPlayer.reset()
                mMediaPlayer.setDataSource(url)
                mMediaPlayer.setOnPreparedListener { mp: MediaPlayer? ->
                    mMediaPlayer.start()
                    Log.d(TAG, "---MediaPlayer--- Prepared")
                }
                mMediaPlayer.prepare()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun startAudioStream(content: String) {
        synchronized(synchronizedObj) { stopped = false }
        val result = mSynthesizer!!.StartSpeakingTextAsync(content).get()
        audioDataStream = AudioDataStream.fromResult(result)

        // Set the chunk size to 50 ms. 24000 * 16 * 0.05 / 8 = 2400
        readAudioData()
        mRecognizedListener?.playSpeakEnd(mPlayContentText)
        Log.d("AUDIOTRACK", "Finished reading from audio stream result")
    }

    private fun readAudioData() {
        val buffer = ByteArray(2400)
        while (!stopped) {
            val len = audioDataStream!!.readData(buffer)
            if (len == 0L) {
                break
            }
            val bytesWritten = audioTrack!!.write(buffer, 0, len.toInt())
            Log.d("AUDIOTRACK", "$bytesWritten bytes")
        }
    }

    private fun setupLogs() {
        connection?.connected?.addEventListener { _, _ ->
//            Log.d(
//                TAG,
//                "Connection established"
//            )
        }
        connection?.disconnected?.addEventListener { _, _ ->
//            Log.d(
//                TAG,
//                "Disconnected"
//            )
        }
        mSynthesizer!!.SynthesisStarted.addEventListener { _, e: SpeechSynthesisEventArgs ->
//            Log.d(
//                TAG,
//                "Synthesis started. Result Id: ${e.result.resultId}"
//            )
            e.close()
        }
        mSynthesizer!!.Synthesizing.addEventListener { _, e: SpeechSynthesisEventArgs ->
//            Log.d(
//                TAG,
//                "Synthesizing. received ${e.result.audioLength} bytes."
//            )
            e.close()
        }
        mSynthesizer!!.SynthesisCompleted.addEventListener { _, e: SpeechSynthesisEventArgs ->
//            Log.d(
//                TAG,
//                "First byte latency: ${
//                    e.result.properties.getProperty(
//                        PropertyId.SpeechServiceResponse_SynthesisFirstByteLatencyMs
//                    )
//                }"
//            )
//            Log.d(
//                TAG,
//                "Finish latency: ${
//                    e.result.properties.getProperty(
//                        PropertyId.SpeechServiceResponse_SynthesisFinishLatencyMs
//                    )
//                } ms."
//            )
            e.close()
        }
        mSynthesizer!!.SynthesisCanceled.addEventListener { _, e: SpeechSynthesisEventArgs ->
            val cancellationDetails =
                SpeechSynthesisCancellationDetails.fromResult(e.result).toString()
            Log.e(
                TAG,
                "Error synthesizing. Result ID: ${e.result.resultId}. Error detail:$cancellationDetails"
            )
            e.close()
        }
        mSynthesizer!!.WordBoundary.addEventListener { _, e: SpeechSynthesisWordBoundaryEventArgs ->
//            Log.d(
//                TAG,
//                "Word boundary. Text offset ${e.textOffset}, length ${e.wordLength}; audio offset ${e.audioOffset / 10000} ms."
//            )
        }
    }

}