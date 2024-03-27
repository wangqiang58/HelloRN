package cn.xdf.ucan.troy.aispeak.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import cn.xdf.ucan.troy.aispeak.Constant
import cn.xdf.ucan.troy.aispeak.ModuleMainApplication.mPresenter
import cn.xdf.ucan.troy.aispeak.bean.NextCommandData
import cn.xdf.ucan.troy.aispeak.bean.NextCommandRequestBean
import cn.xdf.ucan.troy.aispeak.bean.NextCommandResponseBean
import cn.xdf.ucan.troy.aispeak.bean.StudentInputBean
import cn.xdf.ucan.troy.aispeak.listener.RecognizedListener
import cn.xdf.ucan.troy.aispeak.main.databinding.ActivityChatBinding
import cn.xdf.ucan.troy.aispeak.mvvm.vm.ChatViewModel
import cn.xdf.ucan.troy.aispeak.view.SpaceItemDecoration
import cn.xdf.ucan.troy.lib.utils.DisplayUtils
import cn.xdf.ucan.troy.lib.utils.NetWorkUtils
import com.microsoft.cognitiveservices.speech.CancellationErrorCode
import com.microsoft.cognitiveservices.speech.ResultReason
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID


class ChatActivity : BaseActivity() {
    companion object {
        const val TAG = "ChatActivity"
        var studentInputBean = StudentInputBean("", "")
    }

    private var mAdapter: MessageDetailAdapter? = null
    var mList = ArrayList<NextCommandResponseBean>()
    private var microsoftServices: MicrosoftRecognizedServices? = null
    private var mTopicId: Long = -1
    private var mVersionId: Long = -1
    private var mSessionId: String = ""
    private var mFirstCommandBean: NextCommandResponseBean? = null
    private var mSecondCommandBean: NextCommandResponseBean? = null
    private var mCurrentCommandBean: NextCommandResponseBean? = null
    private var mInputOrScore: String? = ""
    private var mChatViewModel: ChatViewModel? = null
    private lateinit var mChatBinding: ActivityChatBinding
    private var count = 0
    private var mPlaying = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTopicId = intent.getLongExtra(Constant.TOPICID, -1)
        Log.i(TAG, " ChatActivity onCreate topicId is $mTopicId")
        mChatBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(mChatBinding.root)
        mPresenter.attachActivityContext(this)
        initViews()
        mChatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                mChatViewModel!!.requestNextCommand(
                    NextCommandRequestBean(
                        "",
                        "",
                        1,
                        1,
                        null
                    )
                )
            }
        }
        lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(state = Lifecycle.State.RESUMED) {
                mChatViewModel!!.mShareFLow.collect {
                    refreshListview(it.data)
                }
            }
        }
        lifecycleScope.launch(Dispatchers.Main){
            mChatViewModel!!.mSSEUIContentFlow.collect{
                refreshCurrentCommandContent(it.content)
                Log.i("niufeiting", " collect refreshCurrentCommandContent:${it.content}")
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(state = Lifecycle.State.RESUMED){
                mChatViewModel!!.mSSEContentFlow.collect {
                    if (microsoftServices === null) {
                        setRecognizedListener()
                    }
                    var result = microsoftServices!!.recognizedUtil!!.playDemo(it.content)
                    if (result.reason == ResultReason.SynthesizingAudioCompleted) {
                        Log.i(
                            "niufeiting",
                            "collect Synthesis completed content:${it.content}"
                        )

                        if(it.type == 1){
                            microsoftServices!!.recognizedUtil!!.mRecognizedListener!!.playSpeakEnd(it.content)
                        }
                    }
                }
            }
        }
    }

    private fun clear() {
        mChatBinding.voiceRecognitionButton.visibility = View.GONE
        mList.clear()
        mCurrentCommandBean = null
        studentInputBean.text = ""
        studentInputBean.evaluateResult = ""
        mVersionId = -1
        mSessionId = ""
        count = 0
        mPlaying = false
        refreshListview()
    }

    private fun initViews() {
        initButton()
        initRecycle()
        clear()
//        mPresenter?.getNextCommandAndRefreshView("", "", mVersionId, mTopicId, studentInputBean)
    }

    private fun initButton() {
        mChatBinding.topicButton.setOnClickListener {
            var intent = Intent(this@ChatActivity, TopicActivity::class.java)
            intent.putExtra(Constant.TOPICID, mTopicId)
            this@ChatActivity.startActivity(intent)
            return@setOnClickListener
        }
    }

    private fun initTitle() {
        mChatBinding.voiceRecognitionButton.visibility = View.VISIBLE
        mChatBinding.voiceRecognitionButton.setOnClickListener {
            refreshCurrentCommandContent("hello niufeiting \t\t")
            return@setOnClickListener
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        clear()
        mTopicId = intent!!.getLongExtra(Constant.TOPICID, -1)
        mPresenter?.getNextCommandAndRefreshView("", "", mVersionId, mTopicId, studentInputBean)
    }


    override fun <T> refreshListview(data: T) {
        if (data == null || data !is NextCommandData) return
        var nextCommandData = data as NextCommandData
        mVersionId = nextCommandData.version
        mSessionId = nextCommandData.sessionId
        mFirstCommandBean = nextCommandData.firstCommandContent
        mSecondCommandBean = nextCommandData.secondCommandContent
        studentInputBean.text = " "
        studentInputBean.evaluateResult = " "
        Log.i(
            TAG,
            "请求的指令结果 指令一 ${mFirstCommandBean.toString()} \n  指令二 ${mSecondCommandBean.toString()}"
        )
        operationCommand(mFirstCommandBean)
        mFirstCommandBean = null
    }

    private fun setRecognizedListener() {
        microsoftServices = MicrosoftRecognizedServices()
        microsoftServices!!.recognizedUtil!!.mRecognizedListener = object : RecognizedListener {
            override fun recognizedError(result: CancellationErrorCode?) {}
            override fun recognizedResult(result: String?) {
                if (mPresenter.mContext == null || mPresenter.mContext!!.get() !is ChatActivity) return
                runOnUiThread {
                    if (result.isNullOrEmpty()) {
                        if (count == 2) {
                            Toast.makeText(
                                this@ChatActivity,
                                "没有听清楚，再说一遍",
                                Toast.LENGTH_LONG
                            ).show()
                            count = 0
                        }
                        startRecognizer()
                        count++
                    } else {
                        count = 0
                        studentInputBean.text = result
                        mChatBinding.voiceRecognitionButton.visibility = View.GONE
                        //TODO 下面这行报过空指针，需要处理
                        mCurrentCommandBean!!.commandContentDetail!!.text = result
                        refreshListview()
                        Log.d(
                            TAG,
                            "识别刷新UI结束 执行下一个指令 "
                        )
                        operationCommand(mSecondCommandBean) //开始消费下一个指令
                        mSecondCommandBean = null
                        mInputOrScore = "69"
                    }
                }
            }

            override fun playSpeakError(content: String?) {
                runOnUiThread {
                    Toast.makeText(
                        this@ChatActivity,
                        "文本合成错误啦！:${content}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun playSpeakEnd(content: String?) {
                Log.i(TAG, " 播放结束，执行下一个指令 ")
                if (mPresenter.mContext == null || mPresenter.mContext!!.get() !is ChatActivity) return
                mCurrentCommandBean!!.alreadyPlay = true
                mPlaying = false
                operationCommand(mSecondCommandBean)
                mSecondCommandBean = null
            }
        }
    }

    /**
     *
     * 当command 为null时，代表2个指令都消费流，发起请求
     * 当command 不为null时 将指令给mCurrentCommandBean，开始执行mCurrentCommandBean
     */
    private fun operationCommand(command: NextCommandResponseBean?) {
        if (command == null) {
            Log.i(TAG, " 指令: null，发起请求 携带参数")
            lifecycleScope.launch(Dispatchers.IO) {
                mChatViewModel!!.requestNextCommand(
                    NextCommandRequestBean(
                        "",
                        mSessionId,
                        mVersionId,
                        mTopicId,
                        studentInputBean
                    )
                )
            }
            return
        }
        mCurrentCommandBean = command
        mCurrentCommandBean!!.alreadyPlay = false
        Log.i(TAG, " 当前指令： ${mCurrentCommandBean.toString()}")
        if (mCurrentCommandBean != null) {

            if (mCurrentCommandBean!!.commandContentDetail!!.text.isNullOrEmpty()) {
                mCurrentCommandBean!!.commandContentDetail!!.text = ""
            }
            mCurrentCommandBean!!.commandUUID = UUID.randomUUID().toString()
        }
        when (mCurrentCommandBean!!.actionRole) {
            Constant.TEACHER -> {
                when (mCurrentCommandBean!!.commandType.toInt()) {
                    Constant.SHOW_AND_READ -> {//显示并朗读
                        Log.i(
                            TAG,
                            " 执行：老师朗读 刷新ui"
                        )
                        if (!mCurrentCommandBean!!.requestId.isNullOrEmpty()) {
                            Log.i(
                                TAG,
                                " content is empty 发起请求 requsetId   == ${mCurrentCommandBean!!.requestId!!}"
                            )
                            lifecycleScope.launch(Dispatchers.IO) {
                                mChatViewModel!!.getSSEContent(mCurrentCommandBean!!.requestId!!)
                            }
                        } else {
                            refreshListview()
                        }
                    }

                    Constant.ORAL_LANGUAGE_EVALUATION -> {//口语测评
                        if (mSecondCommandBean == null) {
                            studentInputBean.evaluateResult = mInputOrScore.toString()//
                            Log.i(
                                TAG,
                                " 执行：老师测评指令 ；发起请求  携带分数 == ${studentInputBean.evaluateResult}"
                            )
                            lifecycleScope.launch(Dispatchers.IO) {
                                mChatViewModel!!.requestNextCommand(
                                    nextCommandRequestBean = NextCommandRequestBean(
                                        "",
                                        mSessionId,
                                        mVersionId,
                                        mTopicId,
                                        studentInputBean
                                    )
                                )
                            }
                            mInputOrScore = ""
                        }
                    }

                    Constant.SHOW_AND_PLAY_AUDIO -> {//老师播放音频
                        Log.i(TAG, " 执行：老师音频播放 ； 刷新ui")
                        refreshListview()
                    }
                }
            }

            Constant.STUDENT -> {
                when (mCurrentCommandBean!!.commandType.toInt()) {
                    Constant.WAIT_AUDIO_INPUT, Constant.EVALUATION_FOLLOW_READ -> {//等待音频输入 学生音频输入+测评
                        Log.i(TAG, " 执行：学生输入指令 ； 刷新ui")
                        runOnUiThread {
                            mChatBinding.voiceRecognitionButton.visibility = View.VISIBLE
                            startRecognizer()
                        }
                    }
                }
            }
        }
    }

    private fun startRecognizer() {
        if (!checkNet()) {
            Log.d(TAG, "无网络")
            return
        }
        if (microsoftServices === null) {
            setRecognizedListener()
        }
        microsoftServices!!.recognizedUtil!!.startRecognized()
    }

    public fun play(content: String) {
        if (!checkNet() || (mCurrentCommandBean != null && mCurrentCommandBean!!.alreadyPlay)) {
            Log.d(TAG, "无网络")
            return
        }
        if (microsoftServices === null) {
            setRecognizedListener()
        }
        microsoftServices!!.recognizedUtil!!.play(content)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
        if (microsoftServices != null) {
            microsoftServices!!.recognizedUtil!!.onDestroy()
        }
    }

    override fun onPause() {
        super.onPause()
        mChatViewModel!!.closeSSE()
        if (microsoftServices != null) {
            microsoftServices!!.recognizedUtil!!.pausePlayback()
            microsoftServices!!.recognizedUtil!!.stopRecon()
        }
    }

    private fun checkNet(): Boolean {
        return NetWorkUtils.isNetworkConnected(this)
    }

    private fun initRecycle() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mChatBinding.messageDetailRecycleList.layoutManager = layoutManager
        mChatBinding.messageDetailRecycleList.addItemDecoration(
            SpaceItemDecoration(
                DisplayUtils.dp2px(
                    this,
                    20f
                )
            )
        )
        mAdapter = MessageDetailAdapter(this)
        mChatBinding.messageDetailRecycleList.adapter = mAdapter
        mAdapter!!.commandListener = object : MessageDetailAdapter.CommandListener {
            override fun onItemCommand(
                v: View,
                position: Int,
                items: MutableList<NextCommandResponseBean>
            ) {
                when (mCurrentCommandBean!!.actionRole) {
                    Constant.TEACHER -> {
                        when (mCurrentCommandBean!!.commandType.toInt()) {
                            Constant.SHOW_AND_READ -> {//显示并朗读 播放音频
                                if (mCurrentCommandBean!!.requestId.isNullOrEmpty()) {
                                    play(mCurrentCommandBean!!.commandContentDetail!!.text)
                                }
                            }

                            Constant.SHOW_AND_PLAY_AUDIO -> {
                                play(mCurrentCommandBean!!.commandContentDetail!!.audioUrl)
                            }
                        }
                    }
                }
            }
        }
    }


    private fun refreshListview() {
        if (mCurrentCommandBean != null && !mList.contains(mCurrentCommandBean)) {
            mList.add(mCurrentCommandBean!!)
        }
        runOnUiThread {
            mAdapter!!.notifyDataSetChanged()
            if (mList.size > 1) {
                mChatBinding.messageDetailRecycleList.smoothScrollToPosition(mList.size - 1)
            }
        }
    }


    fun refreshCurrentCommandContent(text: String) {
        if (mCurrentCommandBean != null && mCurrentCommandBean!!.commandContentDetail != null) {
            mCurrentCommandBean!!.appendText(text)
        }
        refreshListview()
    }


    fun playCurrentCommand(content: String): Boolean {
        var result = mPlaying
        if (!mCurrentCommandBean!!.alreadyPlay && !mPlaying) {
            play(content)
            mPlaying = true
        }
        return result
    }
    /**
     * 获取假的数据
     */
    private fun genMockData(): MutableList<NextCommandResponseBean> {
        val mList: MutableList<NextCommandResponseBean> = ArrayList()
        val date = SimpleDateFormat("MM-dd HH:mm").format(Date())

//        mList.add(NextCommandResponseBean("TEACHER", "你好啊", "", ""))
//        mList.add(NextCommandResponseBean("TEACHER", "请问在吗", "", ""))
//        mList.add(NextCommandResponseBean("STUDENT", "在的呢", "", ""))
//        mList.add(NextCommandResponseBean("STUDENT", "你好", "", ""))
        /**
         *     val userId: String = "",
         *     val sessionId: String = "",
         *     val linkId: Int = 0,
         *     val studentInput: String = "",
         *     val taskStep: Int = 0
         */
        return mList
    }
}
