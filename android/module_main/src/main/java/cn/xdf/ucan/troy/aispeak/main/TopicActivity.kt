package cn.xdf.ucan.troy.aispeak.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import cn.xdf.ucan.troy.aispeak.Constant
import cn.xdf.ucan.troy.aispeak.bean.TopicInfoBean
import cn.xdf.ucan.troy.aispeak.bean.TopicRequestBean
import cn.xdf.ucan.troy.aispeak.main.databinding.ActivityTopicBinding
import cn.xdf.ucan.troy.aispeak.mvvm.vm.TopicViewModel
import cn.xdf.ucan.troy.aispeak.view.SpaceItemDecoration
import cn.xdf.ucan.troy.lib.utils.DisplayUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TopicActivity : BaseActivity() {

    companion object {
        const val TAG = "TopicActivity"
    }

    private lateinit var mTopicActivityView: ActivityTopicBinding
    private var mAdapter: TopicMessageAdapter? = null
    var mList = ArrayList<TopicInfoBean>()
    private var mCurrentTopicValue: Long = -1
    private var mViewModel:TopicViewModel? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCurrentTopicValue = intent.getLongExtra(Constant.TOPICID, -1)
        Log.i(TAG, "TopicActivity onCreate  mCurrentTopicValue is $mCurrentTopicValue")
        mTopicActivityView = ActivityTopicBinding.inflate(layoutInflater)
        setContentView(mTopicActivityView.root)
        initView()
        mViewModel = ViewModelProvider(this).get(TopicViewModel::class.java)
        lifecycleScope.launch(Dispatchers.IO){
            repeatOnLifecycle(state = Lifecycle.State.CREATED){
                mViewModel!!.requestTopicList(TopicRequestBean(""))
            }
        }
        lifecycleScope.launch(Dispatchers.Main){
            repeatOnLifecycle(state = Lifecycle.State.RESUMED){
                mViewModel!!.mShareFLow.collect(){
                    refreshListview(it.data)
                }
            }
        }
    }

    private fun initView() {
        mTopicActivityView.startBt.setOnClickListener {
            if (mList != null) {
                mList?.get(0).let { it1 -> startActivity(it1!!.id) }
            }
            return@setOnClickListener
        }

        initRecycle()
    }

    private fun initRecycle() {
        // 1) 添加布局管理器
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mTopicActivityView.topicRecycleList.layoutManager = layoutManager
        mTopicActivityView.topicRecycleList.addItemDecoration(SpaceItemDecoration(DisplayUtils.dp2px(this, 24f)))
        mAdapter = TopicMessageAdapter(this)
        mTopicActivityView.topicRecycleList.adapter = mAdapter
        mAdapter!!.mClickListener = object : TopicMessageAdapter.OnItemClickListener {
            override fun itemClick(topicId: Long) {
                Log.i(TAG, "TopicActivity item click id is $topicId")
                startActivity(topicId)
                finish()
            }
        }
    }

    override fun <T> refreshListview(data: T) {
        if (data == null || data !is ArrayList<*> || data.isEmpty()) {
            return
        }
        mList.clear()
        mList = data as ArrayList<TopicInfoBean>
        mAdapter!!.notifyDataSetChanged()
    }

    fun startActivity(topicId: Long) {
        Log.i(TAG, " startActivity topicValue is $topicId")

        var intent = Intent(this@TopicActivity, ChatActivity::class.java)
        intent.putExtra(Constant.TOPICID, topicId)
        this@TopicActivity.startActivity(intent)
    }
}